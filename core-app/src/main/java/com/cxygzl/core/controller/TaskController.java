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
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        taskLocalParamMap.put(ProcessInstanceConstant.VariableKey.APPROVE_RESULT,taskParamDto.getParamMap().get(ProcessInstanceConstant.VariableKey.APPROVE_RESULT));
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
//            taskService.setVariablesLocal(ProcessInstanceConstant.VariableKey.APPROVE_RESULT, taskLocalParamMap);
//            taskService.addComment(taskParamDto.getTaskId(),taskParamDto.getProcessInstanceId(),
//                    ProcessInstanceConstant.VariableKey.APPROVE_DESC, MapUtil.getStr(taskLocalParamMap,"approveDesc"));
        }

        taskService.complete(taskParamDto.getTaskId(), taskParamDto.getParamMap());

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

        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }
//        taskService.setOwner(taskParamDto.getTaskId(), taskParamDto.getUserId());

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
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

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
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

//        taskService.setOwner(taskParamDto.getTaskId(), taskParamDto.getUserId());
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


        if (taskService.createTaskQuery().taskId(taskId).singleResult().isSuspended()) {
            return R.fail("任务处于挂起状态");
        }


        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (StrUtil.equals(targetKey, ProcessInstanceConstant.VariableKey.STARTER)) {
            targetKey = StrUtil.format("{}_user_task", targetKey);
            runtimeService.setVariable(task.getExecutionId(),
                    ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE, true);
        }

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(taskParamDto.getProcessInstanceId())
                .moveActivityIdTo(taskParamDto.getNodeId(), targetKey)
                .changeState();
        return R.success();
    }


}
