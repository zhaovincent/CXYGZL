package com.cxygzl.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.VariableQueryParamDto;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 */
@RestController
@Slf4j
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ManagementService managementService;


    @Resource
    private RuntimeService runtimeService;


    /**
     * 查询任务变量
     *
     * @param paramDto
     * @return
     */
    @PostMapping("queryTaskVariables")
    public R queryTaskVariables(@RequestBody VariableQueryParamDto paramDto) {

        List<String> keyList = paramDto.getKeyList();
        if (CollUtil.isEmpty(keyList)) {
            TaskQuery taskQuery = taskService.createTaskQuery();

            Task task = taskQuery.taskId(paramDto.getTaskId()).singleResult();
            if (task == null) {
                return R.fail("任务不存在");
            }

            Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());

            return R.success(variables);
        }

        Map<String, Object> variables = taskService.getVariables(paramDto.getTaskId(), keyList);
        return R.success(variables);

    }


    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    @GetMapping("queryTask")
    public R queryTask(String taskId, String userId) {

        DelegationState delegationState = null;


        //实例id
        String processInstanceId = null;
        Object delegateVariable = false;


        String processDefinitionId = null;

        //nodeid
        String taskDefinitionKey = null;
        String executionId = null;

        boolean taskExist = true;

        {
            TaskQuery taskQuery = taskService.createTaskQuery();

            Task task = taskQuery.taskId(taskId).taskAssignee(userId).singleResult();
            if (task == null) {
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).taskAssignee(userId).singleResult();
                if (historicTaskInstance == null) {
                    return R.fail("任务不存在");
                }
                taskExist = false;
                taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
                processInstanceId = historicTaskInstance.getProcessInstanceId();
                executionId = historicTaskInstance.getExecutionId();
                processDefinitionId = historicTaskInstance.getProcessDefinitionId();
            } else {
                processDefinitionId = task.getProcessDefinitionId();
                taskDefinitionKey = task.getTaskDefinitionKey();
                delegationState = task.getDelegationState();
                processInstanceId = task.getProcessInstanceId();
                executionId = task.getExecutionId();
                delegateVariable = taskService.getVariableLocal(taskId, "delegate");


            }
        }


        //流程id
        String flowId = NodeUtil.getFlowId(processDefinitionId);

        Map<String, Object> variableAll = new HashMap<>();

        //表单处理


        if (taskExist) {


            Map<String, Object> variables = taskService.getVariables(taskId);
            variableAll.putAll(variables);

        } else {

        }


        TaskResultDto taskResultDto = new TaskResultDto();
        taskResultDto.setFlowId(flowId);
        taskResultDto.setNodeId(taskDefinitionKey);
        taskResultDto.setCurrentTask(taskExist);
        taskResultDto.setExecutionId(executionId);
        taskResultDto.setDelegate(Convert.toBool(delegateVariable, false));
        taskResultDto.setVariableAll(variableAll);
        taskResultDto.setProcessInstanceId(processInstanceId);
        taskResultDto.setDelegationState(delegationState == null ? null : delegationState.toString());


        return R.success(taskResultDto);
    }


    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("/complete")
    public R complete(@RequestBody TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        Boolean approveResult = taskParamDto.getApproveResult();
        runtimeService.setVariableLocal(task.getExecutionId(), ProcessInstanceConstant.VariableKey.APPROVE_RESULT,
                approveResult);
        //保存任务类型
        if (approveResult != null) {


            taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                    approveResult ? ProcessInstanceConstant.TaskType.PASS : ProcessInstanceConstant.TaskType.REFUSE);
        }

        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(),
                    ProcessInstanceConstant.VariableKey.APPROVE_DESC, taskParamDto.getApproveDesc());
        }
        Map<String, Object> paramMap = taskParamDto.getParamMap();
        taskService.complete(task.getId(), paramMap);

        return R.success();
    }

    /**
     * 前加签
     *
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("delegateTask")
    public R delegateTask(@RequestBody TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(),
                    ProcessInstanceConstant.VariableKey.FRONT_JOIN_DESC, taskParamDto.getApproveDesc());
        }

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                 ProcessInstanceConstant.TaskType.FRONT_JOIN
        );

        taskService.delegateTask(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());
        return R.success();
    }

    /**
     * 加签任务完成
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("resolveTask")
    public R resolveTask(@RequestBody TaskParamDto taskParamDto) {
        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }


        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(),
                    ProcessInstanceConstant.VariableKey.APPROVE_DESC, taskParamDto.getApproveDesc());
        }

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.RESOLVE
        );

        taskService.resolveTask(taskParamDto.getTaskId(), taskParamDto.getParamMap());
        return R.success();
    }

    /**
     * 设置执行人 后加签
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("setAssignee")
    public R setAssignee(@RequestBody TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.BACK_JOIN
        );
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(),
                    ProcessInstanceConstant.VariableKey.BACK_JOIN_DESC, taskParamDto.getApproveDesc());
        }
        taskService.setAssignee(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());

        return R.success();
    }


    /**
     * 退回
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("back")
    public R back(@RequestBody TaskParamDto taskParamDto) {
        String taskId = taskParamDto.getTaskId();
        String targetKey = taskParamDto.getTargetNodeId();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if(task==null){
            return R.fail("任务不存在");
        }

        if (task.isSuspended()) {
            return R.fail("任务处于挂起状态");
        }


        // 当前任务 task

        if (StrUtil.equals(targetKey, ProcessInstanceConstant.VariableKey.STARTER)) {
            targetKey = StrUtil.format("{}_user_task", targetKey);
            runtimeService.setVariable(task.getExecutionId(),
                    ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE, true);
        }
        runtimeService.setVariable(task.getExecutionId(), StrUtil.format("{}_parent_id", targetKey), task.getTaskDefinitionKey());

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.REJECT
        );

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(taskParamDto.getProcessInstanceId())
                .moveActivityIdTo(taskParamDto.getNodeId(), targetKey)
                .changeState();
        return R.success();
    }


}
