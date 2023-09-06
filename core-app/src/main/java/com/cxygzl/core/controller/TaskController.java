package com.cxygzl.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ApproveDescTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.*;
import com.cxygzl.core.utils.NodeUtil;
import com.cxygzl.core.vo.TaskCommentDto;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID;

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
     * 查询任务评论
     *
     * @param paramDto
     * @return
     */
    @PostMapping("queryTaskComments")
    public R queryTaskComments(@RequestBody VariableQueryParamDto paramDto) {
        String taskId = paramDto.getTaskId();

        List<Comment> taskComments = new ArrayList<>();

        for (String s : ApproveDescTypeEnum.getTypeList()) {
            List<Comment> approveDescList = taskService.getTaskComments(taskId, s);
            taskComments.addAll(approveDescList);
        }



        List<SimpleApproveDescDto> simpleApproveDescDtoList = new ArrayList<>();
        for (Comment comment : taskComments) {
            String id = comment.getId();
            Date time = comment.getTime();
            String fullMessage = comment.getFullMessage();
            TaskCommentDto taskCommentDto = JSON.parseObject(fullMessage, TaskCommentDto.class);


            String userId = taskCommentDto.getUserId();
            Boolean isSys = taskCommentDto.getSys();


            SimpleApproveDescDto simpleApproveDescDto = new SimpleApproveDescDto();
            simpleApproveDescDto.setDate(time);
            simpleApproveDescDto.setMsgId(id);
            simpleApproveDescDto.setSys(isSys);
            simpleApproveDescDto.setUserId(userId);
            simpleApproveDescDto.setType(comment.getType());
            simpleApproveDescDto.setMessage(fullMessage);
            simpleApproveDescDtoList.add(simpleApproveDescDto);
        }
        return R.success(simpleApproveDescDtoList);

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
        String flowUniqueId = null;

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
                HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .taskId(taskId)
                        .variableName(FLOW_UNIQUE_ID).singleResult();
                flowUniqueId =historicVariableInstance==null?null:Convert.toStr(historicVariableInstance.getValue());
            } else {
                processDefinitionId = task.getProcessDefinitionId();
                taskDefinitionKey = task.getTaskDefinitionKey();
                delegationState = task.getDelegationState();
                processInstanceId = task.getProcessInstanceId();
                executionId = task.getExecutionId();
                delegateVariable = taskService.getVariableLocal(taskId, "delegate");

                flowUniqueId = taskService.getVariable(taskId, FLOW_UNIQUE_ID, String.class);

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
        taskResultDto.setFrontJoinTask(delegationState == null ? false : StrUtil.equals(delegationState.toString(), ProcessInstanceConstant.VariableKey.PENDING));
        taskResultDto.setFlowUniqueId(flowUniqueId);

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

        boolean approveResult = taskParamDto.getApproveResult();
        runtimeService.setVariableLocal(task.getExecutionId(), ProcessInstanceConstant.VariableKey.APPROVE_RESULT,
                approveResult);
        //保存任务类型


        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                approveResult ? ProcessInstanceConstant.TaskType.PASS : ProcessInstanceConstant.TaskType.REFUSE);

        String descType = approveResult ? ApproveDescTypeEnum.PASS.getType() : ApproveDescTypeEnum.REFUSE.getType();
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            saveUserCommentToTask(task, descType,
                    taskParamDto.getApproveDesc(),
                    taskParamDto.getUserId(), "提交任务并添加了评论");
        } else {
            saveSysCommentToTask(task, descType, "提交任务", taskParamDto.getUserId());
        }
        Map<String, Object> paramMap = taskParamDto.getParamMap();
        taskService.complete(task.getId(), paramMap);

        return R.success();
    }

    /**
     * 前加签
     *
     * @return
     */
    @PostMapping("delegateTask")
    public R delegateTask(@RequestBody TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }


        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            saveUserCommentToTask(task, ApproveDescTypeEnum.FRONT_JOIN.getType(),
                    taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    StrUtil.format("委派任务给:[{}]并添加了评论",
                            taskParamDto.getTargetUserName()
                    ));

        } else {
            saveSysCommentToTask(task, ApproveDescTypeEnum.FRONT_JOIN.getType(), StrUtil.format("委派任务给:{}",
                    taskParamDto.getTargetUserName()
            ), taskParamDto.getUserId());
        }


        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.FRONT_JOIN
        );


        //设置变量
        taskService.setVariables(taskParamDto.getTaskId(),taskParamDto.getParamMap());

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
            saveUserCommentToTask(task, ApproveDescTypeEnum.RESOLVE.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    "完成任务并添加了评论");

        } else {
            saveSysCommentToTask(task, ApproveDescTypeEnum.RESOLVE.getType(), StrUtil.format("完成任务"
            ), taskParamDto.getUserId());
        }

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.RESOLVE
        );
        //不能搞 因为涉及多实例
        // taskService.setVariable(task.getId(), FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());

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
            saveUserCommentToTask(task, ApproveDescTypeEnum.BACK_JOIN.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    StrUtil.format("转办任务给:[{}]并添加了评论",
                            taskParamDto.getTargetUserName()
                    ));

        } else {
            saveSysCommentToTask(task, ApproveDescTypeEnum.BACK_JOIN.getType(), StrUtil.format("转办任务给:{}",
                    taskParamDto.getTargetUserName()
            ), taskParamDto.getUserId());
        }

        //设置变量
        taskService.setVariables(taskParamDto.getTaskId(),taskParamDto.getParamMap());
        taskService.setAssignee(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());

        return R.success();
    }


    /**
     * 添加执行人
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("addAssignee")
    public R addAssignee(@RequestBody TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        taskService.setVariables(task.getId(),taskParamDto.getParamMap());

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.ADD_ASSIGNEE
        );
        String userId = taskParamDto.getUserId();
        String targetUserName = CollUtil.join(taskParamDto.getTargetUserNameList(),",");
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            saveUserCommentToTask(task, ApproveDescTypeEnum.ADD_ASSIGNEE.getType(), taskParamDto.getApproveDesc(), userId,
                    StrUtil.format("加签任务给:[{}]并添加了评论",
                            targetUserName
                    ));

        } else {
            saveSysCommentToTask(task, ApproveDescTypeEnum.ADD_ASSIGNEE.getType(), StrUtil.format("加签任务给:{}",
                    targetUserName
            ), userId);
        }

        List<String> targetUserIdList = taskParamDto.getTargetUserIdList();
        for (String s : targetUserIdList) {
            runtimeService.addMultiInstanceExecution(task.getTaskDefinitionKey(),task.getProcessInstanceId(),
                    Collections.singletonMap(StrUtil.format("{}_assignee_temp",task.getTaskDefinitionKey()),s)
            );
        }

        return R.success();
    }

    /**
     * 删除执行人
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("delAssignee")
    public R delAssignee(@RequestBody TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }
        taskService.setVariables(task.getId(),taskParamDto.getParamMap());

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.DEL_ASSIGNEE
        );
        String userId = taskParamDto.getUserId();
        String targetUserName = CollUtil.join(taskParamDto.getTargetUserNameList(),",");
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            saveUserCommentToTask(task, ApproveDescTypeEnum.DEL_ASSIGNEE.getType(), taskParamDto.getApproveDesc(), userId,
                    StrUtil.format("减签任务:[{}]并添加了评论",
                            targetUserName
                    ));

        } else {
            saveSysCommentToTask(task, ApproveDescTypeEnum.DEL_ASSIGNEE.getType(), StrUtil.format("减签任务:{}",
                    targetUserName
            ), userId);
        }

        List<String> targetExecutionIdList = taskParamDto.getTargetExecutionIdList();
        for (String s : targetExecutionIdList) {
            runtimeService.deleteMultiInstanceExecution(s,false);

        }

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

        if (task == null) {
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
        runtimeService.setVariables(task.getExecutionId(),taskParamDto.getParamMap());
        runtimeService.setVariable(task.getExecutionId(), StrUtil.format("{}_parent_id", targetKey), task.getTaskDefinitionKey());
        runtimeService.setVariable(task.getExecutionId(), FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());
        runtimeService.setVariableLocal(task.getExecutionId(), ProcessInstanceConstant.VariableKey.TASK_TYPE, ProcessInstanceConstant.TaskType.REJECT);

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                ProcessInstanceConstant.TaskType.REJECT
        );


        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            saveUserCommentToTask(task, ApproveDescTypeEnum.REJECT.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    "驳回了任务并添加了评论");
        } else {
            saveSysCommentToTask(task, ApproveDescTypeEnum.REJECT.getType(), "驳回了任务", taskParamDto.getUserId());
        }

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(taskParamDto.getProcessInstanceId())
                .moveActivityIdTo(taskParamDto.getNodeId(), targetKey)
                .changeState();
        return R.success();
    }

    private void saveUserCommentToTask(Task task, String type, String desc, String userId, String descTitle) {

        TaskCommentDto taskCommentDto = TaskCommentDto.builder().content(desc).title(descTitle).sys(false).userId(userId).build();
        Comment comment = taskService.addComment(task.getId(), task.getProcessInstanceId(),
                type, JSON.toJSONString(taskCommentDto));


    }

    private void saveSysCommentToTask(Task task, String type, String desc, String userId) {
        TaskCommentDto taskCommentDto = TaskCommentDto.builder().content(desc).sys(true).userId(userId).build();

        Comment comment = taskService.addComment(task.getId(), task.getProcessInstanceId(),
                type, JSON.toJSONString(taskCommentDto));


    }


}
