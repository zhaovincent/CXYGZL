package com.cxygzl.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.ApproveAttachmentTypeEnum;
import com.cxygzl.common.constants.ApproveDescTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.constants.TaskTypeEnum;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.UploadValue;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.cmd.InjectRevokeGatewayCmd;
import com.cxygzl.core.service.ITaskService;
import com.cxygzl.core.utils.NodeUtil;
import com.cxygzl.core.vo.TaskCommentDto;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.cxygzl.common.constants.ProcessInstanceConstant.MERGE_GATEWAY_FLAG;
import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-16 16:59
 */
@Component
@Slf4j
public class TaskServiceImpl implements ITaskService {

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
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R complete(TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        boolean approveResult = taskParamDto.getApproveResult();
        runtimeService.setVariableLocal(task.getExecutionId(), ProcessInstanceConstant.VariableKey.APPROVE_RESULT,
                approveResult);
        //保存任务类型


        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                approveResult ? TaskTypeEnum.PASS.getValue() : TaskTypeEnum.REFUSE.getValue());

        String descType = approveResult ? ApproveDescTypeEnum.PASS.getType() : ApproveDescTypeEnum.REFUSE.getType();
        String commentId=null;
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(descType,
                    taskParamDto.getApproveDesc(),
                    taskParamDto.getUserId(), "提交任务并添加了评论",task.getId() ,task.getProcessInstanceId() );
            commentId=comment.getId();
        } else {
            Comment comment = saveSysCommentToTask(task, descType, "提交任务", taskParamDto.getUserId());
            commentId=comment.getId();

        }
//保存图片和文件
        saveAttachment(taskParamDto, commentId,task.getId() , task.getProcessInstanceId());

        Map<String, Object> paramMap = taskParamDto.getParamMap();
        taskService.complete(task.getId(), paramMap);

        return R.success();
    }

    /**
     * 委派任务
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R delegateTask(TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        String commentId=null;

        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.FRONT_JOIN.getType(),
                    taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    StrUtil.format("委派任务给:[{}]并添加了评论",
                            taskParamDto.getTargetUserName()
                    ),task.getId() ,task.getProcessInstanceId() );
            commentId=comment.getId();

        } else {
            Comment comment = saveSysCommentToTask(task, ApproveDescTypeEnum.FRONT_JOIN.getType(), StrUtil.format("委派任务给:{}",
                    taskParamDto.getTargetUserName()
            ), taskParamDto.getUserId());

            commentId=comment.getId();

        }
        saveAttachment(taskParamDto, commentId, task.getId() , task.getProcessInstanceId() );


        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.FRONT_JOIN.getValue()
        );


        //设置变量
        taskService.setVariables(taskParamDto.getTaskId(), taskParamDto.getParamMap());

        taskService.delegateTask(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());
        return R.success();
    }

    /**
     * 加签任务完成
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R resolveTask(TaskParamDto taskParamDto) {

        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }


        String commentId=null;
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.RESOLVE.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    "完成任务并添加了评论", task.getId() ,task.getProcessInstanceId() );
            commentId=comment.getId();

        } else {
            Comment comment = saveSysCommentToTask(task, ApproveDescTypeEnum.RESOLVE.getType(), StrUtil.format("完成任务"
            ), taskParamDto.getUserId());
            commentId=comment.getId();

        }

        saveAttachment(taskParamDto, commentId,task.getId() , task.getProcessInstanceId() );


        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.RESOLVE.getValue()
        );
        //不能搞 因为涉及多实例
        // taskService.setVariable(task.getId(), FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());

        taskService.resolveTask(taskParamDto.getTaskId(), taskParamDto.getParamMap());
        return R.success();
    }

    /**
     * 转交
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R setAssignee(TaskParamDto taskParamDto) {


        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.BACK_JOIN.getValue()
        );
        String commentId=null;
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.BACK_JOIN.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    StrUtil.format("转办任务给:[{}]并添加了评论",
                            taskParamDto.getTargetUserName()
                    ), task.getId() ,task.getProcessInstanceId() );
            commentId=comment.getId();

        } else {
            Comment comment = saveSysCommentToTask(task, ApproveDescTypeEnum.BACK_JOIN.getType(), StrUtil.format("转办任务给:{}",
                    taskParamDto.getTargetUserName()
            ), taskParamDto.getUserId());
            commentId=comment.getId();

        }
        saveAttachment(taskParamDto, commentId,task.getId() , task.getProcessInstanceId());


        //设置变量
        taskService.setVariables(taskParamDto.getTaskId(), taskParamDto.getParamMap());
        taskService.setAssignee(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());

        return R.success();
    }

    /**
     * 删除执行人
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R delAssignee(TaskParamDto taskParamDto) {


        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }

        Map<String, UserTask> allUserTaskMap = getAllUserTaskMap(task.getProcessDefinitionId());
        UserTask userTaskModel = allUserTaskMap.get(task.getTaskDefinitionKey());
        if (!userTaskModel.hasMultiInstanceLoopCharacteristics()) {
            return R.fail("非多实例任务，不能减签");
        }

        MultiInstanceLoopCharacteristics loopCharacteristics = userTaskModel.getLoopCharacteristics();
        if (loopCharacteristics.isSequential()) {
            return R.fail("串行多实例不支持减签");
        }

        taskService.setVariables(task.getId(), taskParamDto.getParamMap());

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.DEL_ASSIGNEE.getValue()
        );
        String userId = taskParamDto.getUserId();
        String targetUserName = CollUtil.join(taskParamDto.getTargetUserNameList(), ",");

        String commentId=null;
        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.DEL_ASSIGNEE.getType(), taskParamDto.getApproveDesc(), userId,
                    StrUtil.format("减签任务:[{}]并添加了评论",
                            targetUserName
                    ),task.getId() ,task.getProcessInstanceId() );
            commentId=comment.getId();

        } else {
            Comment comment = saveSysCommentToTask(task, ApproveDescTypeEnum.DEL_ASSIGNEE.getType(), StrUtil.format("减签任务:{}",
                    targetUserName
            ), userId);
            commentId=comment.getId();

        }
        saveAttachment(taskParamDto, commentId, task.getId() , task.getProcessInstanceId() );

        List<String> targetExecutionIdList = taskParamDto.getTargetExecutionIdList();
        for (String s : targetExecutionIdList) {
            runtimeService.deleteMultiInstanceExecution(s, false);

        }

        return R.success();
    }

    /**
     * 添加执行人
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R addAssignee(TaskParamDto taskParamDto) {


        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        if (task == null) {
            return R.fail("任务不存在");
        }
        Map<String, UserTask> allUserTaskMap = getAllUserTaskMap(task.getProcessDefinitionId());
        UserTask userTaskModel = allUserTaskMap.get(task.getTaskDefinitionKey());
        if (!userTaskModel.hasMultiInstanceLoopCharacteristics()) {
            return R.fail("非多实例任务，不能加签");
        }

        MultiInstanceLoopCharacteristics loopCharacteristics = userTaskModel.getLoopCharacteristics();
        if (loopCharacteristics.isSequential()) {
            return R.fail("串行多实例不支持加签");
        }


        taskService.setVariables(task.getId(), taskParamDto.getParamMap());

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.ADD_ASSIGNEE.getValue()
        );
        String userId = taskParamDto.getUserId();
        String targetUserName = CollUtil.join(taskParamDto.getTargetUserNameList(), ",");
        String commentId =null;

        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.ADD_ASSIGNEE.getType(), taskParamDto.getApproveDesc(), userId,
                    StrUtil.format("加签任务给:[{}]并添加了评论",
                            targetUserName
                    ), task.getId() ,task.getProcessInstanceId() );
              commentId = comment.getId();

        } else {
            Comment comment = saveSysCommentToTask(task, ApproveDescTypeEnum.ADD_ASSIGNEE.getType(), StrUtil.format("加签任务给:{}",
                    targetUserName
            ), userId);
            commentId = comment.getId();

        }

        saveAttachment(taskParamDto, commentId, task.getId() , task.getProcessInstanceId() );

        List<String> targetUserIdList = taskParamDto.getTargetUserIdList();
        for (String s : targetUserIdList) {

            runtimeService.addMultiInstanceExecution(task.getTaskDefinitionKey(), task.getProcessInstanceId(),
                    Collections.singletonMap(loopCharacteristics.getElementVariable(), s)
            );
        }

        return R.success();
    }

    /**
     * 驳回
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R back(TaskParamDto taskParamDto) {

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
        runtimeService.setVariables(task.getExecutionId(), taskParamDto.getParamMap());
        runtimeService.setVariable(task.getExecutionId(), StrUtil.format("{}_parent_id", targetKey), task.getTaskDefinitionKey());
        runtimeService.setVariable(task.getExecutionId(), FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());
        runtimeService.setVariableLocal(task.getExecutionId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.REJECT.getValue());

        taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                TaskTypeEnum.REJECT.getValue()
        );

        String commentId=null;

        if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
            Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.REJECT.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                    "驳回了任务并添加了评论", task.getId() ,task.getProcessInstanceId() );
            commentId=comment.getId();
        } else {
            Comment comment = saveSysCommentToTask(task, ApproveDescTypeEnum.REJECT.getType(), "驳回了任务", taskParamDto.getUserId());
            commentId=comment.getId();

        }
        saveAttachment(taskParamDto, commentId,task.getId() , task.getProcessInstanceId());

        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(taskParamDto.getProcessInstanceId())
                .moveActivityIdTo(taskParamDto.getNodeId(), targetKey)
                .changeState();
        return R.success();
    }

    /**
     * 撤回
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R revoke(TaskParamDto taskParamDto) {


        String targetKey = taskParamDto.getTargetNodeId();

        String processInstanceId = taskParamDto.getProcessInstanceId();

        List<String> taskIdList = taskParamDto.getTaskIdList();

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).taskIds(taskIdList).active().list();
        if (taskList.size() != taskIdList.size()) {
            return R.fail("有已完成的任务，不能撤回");
        }

        for (Task task : taskList) {

            if (task.isSuspended()) {
                return R.fail("有任务处于挂起状态");
            }

        }


        String gatewayId = StrUtil.format("REVOKE_{}{}{}", DateUtil.format(new Date(), "yyyyMMddHHmmss"), RandomUtil.randomString(6), MERGE_GATEWAY_FLAG);

        // 当前任务 task

        if (StrUtil.equals(targetKey, ProcessInstanceConstant.VariableKey.STARTER)) {
            targetKey = StrUtil.format("{}_user_task", targetKey);
            runtimeService.setVariable(processInstanceId,
                    ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE, true);
        }
        if (CollUtil.isNotEmpty(taskParamDto.getParamMap())) {
            runtimeService.setVariables(processInstanceId, taskParamDto.getParamMap());
        }
        runtimeService.setVariable(processInstanceId, StrUtil.format("{}_parent_id", targetKey), gatewayId);
        runtimeService.setVariable(processInstanceId, FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());

        for (Task task : taskList) {
            runtimeService.setVariableLocal(task.getExecutionId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                    TaskTypeEnum.REVOKE.getValue());

            taskService.setVariableLocal(task.getId(), ProcessInstanceConstant.VariableKey.TASK_TYPE,
                    TaskTypeEnum.REVOKE.getValue()
            );


            if (StrUtil.isNotBlank(taskParamDto.getApproveDesc())) {
                saveUserCommentToTask(ApproveDescTypeEnum.REVOKE.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                        "撤回了任务并添加了评论", task.getId() ,task.getProcessInstanceId() );
            } else {
                saveSysCommentToTask(task, ApproveDescTypeEnum.REVOKE.getType(), "撤回了任务", taskParamDto.getUserId());
            }
        }


        //创建节点
        managementService.executeCommand(new InjectRevokeGatewayCmd(processInstanceId, null, targetKey, gatewayId));
        // return R.success();

        List<String> nodeIdList = taskParamDto.getNodeIdList();


        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(processInstanceId)
                .moveActivityIdsToSingleActivityId(nodeIdList, gatewayId)
                .changeState();
        return R.success();
    }

    private Map<String, UserTask> getAllUserTaskMap(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        Process process = bpmnModel.getProcesses().get(0);
        return process.findFlowElementsOfType(UserTask.class)
                .stream().collect(Collectors.toMap(UserTask::getId, a -> a, (k1, k2) -> k1));
    }

    private void saveAttachment(TaskParamDto taskParamDto, String commentId,String taskId,String processInstanceId){

        log.info("保存附件的任务id：{}", taskId);
        List<UploadValue> approveImageList = taskParamDto.getApproveImageList();
        List<UploadValue> approveFileList = taskParamDto.getApproveFileList();
        if(CollUtil.isNotEmpty(approveImageList)){
            for (UploadValue uploadValue : approveImageList) {
                taskService.createAttachment(ApproveAttachmentTypeEnum.IMAGE.getType(), taskId,
                        processInstanceId,uploadValue.getName()
                        ,commentId,uploadValue.getUrl());


            }

        }
        if(CollUtil.isNotEmpty(approveFileList)){
            for (UploadValue uploadValue : approveFileList) {
                taskService.createAttachment(ApproveAttachmentTypeEnum.FILE.getType(), taskId,
                        processInstanceId,uploadValue.getName()
                        ,commentId,uploadValue.getUrl());

            }

        }
    }

    private Comment saveUserCommentToTask(  String type, String desc, String userId, String descTitle,String taskId,String processInstanceId) {

        TaskCommentDto taskCommentDto = TaskCommentDto.builder().content(desc).title(descTitle).sys(false).userId(userId).build();
        Comment comment = taskService.addComment(taskId, processInstanceId,
                type, JsonUtil.toJSONString(taskCommentDto));
        return comment;


    }

    private Comment saveSysCommentToTask(Task task, String type, String desc, String userId) {
        TaskCommentDto taskCommentDto = TaskCommentDto.builder().content(desc).sys(true).userId(userId).build();

        Comment comment = taskService.addComment(task.getId(), task.getProcessInstanceId(),
                type, JsonUtil.toJSONString(taskCommentDto));
        return comment;


    }

    /**
     * 查询任务
     *
     * @param taskId
     * @param userId
     * @return
     */
    @Override
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
        String assignee = null;
        String nodeName = null;

        boolean taskExist = true;

        {
            TaskQuery taskQuery = taskService.createTaskQuery();

            Task task = taskQuery.taskId(taskId).singleResult();
            if (task == null) {
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
                if (historicTaskInstance == null) {
                    return R.fail("任务不存在");
                }
                taskExist = false;
                taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
                nodeName = historicTaskInstance.getName();
                processInstanceId = historicTaskInstance.getProcessInstanceId();
                executionId = historicTaskInstance.getExecutionId();
                assignee = historicTaskInstance.getAssignee();
                processDefinitionId = historicTaskInstance.getProcessDefinitionId();
                HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .taskId(taskId)
                        .variableName(FLOW_UNIQUE_ID).singleResult();
                flowUniqueId = historicVariableInstance == null ? null : Convert.toStr(historicVariableInstance.getValue());
            } else {
                processDefinitionId = task.getProcessDefinitionId();
                taskDefinitionKey = task.getTaskDefinitionKey();
                delegationState = task.getDelegationState();
                processInstanceId = task.getProcessInstanceId();
                executionId = task.getExecutionId();
                nodeName=task.getName();
                assignee = task.getAssignee();
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

        }


        TaskResultDto taskResultDto = new TaskResultDto();
        taskResultDto.setFlowId(flowId);
        taskResultDto.setUserId(assignee);
        taskResultDto.setNodeId(taskDefinitionKey);
        taskResultDto.setNodeName(nodeName);
        taskResultDto.setCurrentTask(taskExist && StrUtil.equals(userId, assignee));
        taskResultDto.setExecutionId(executionId);
        taskResultDto.setDelegate(Convert.toBool(delegateVariable, false));
        taskResultDto.setVariableAll(variableAll);
        taskResultDto.setProcessInstanceId(processInstanceId);
        taskResultDto.setFrontJoinTask(delegationState == null ? false : StrUtil.equals(delegationState.toString(), ProcessInstanceConstant.VariableKey.PENDING));
        taskResultDto.setFlowUniqueId(flowUniqueId);

        return R.success(taskResultDto);
    }

    /**
     * 查询任务评论
     *
     * @param paramDto
     * @return
     */
    @Override
    public R queryTaskComments(VariableQueryParamDto paramDto) {



        String taskId = paramDto.getTaskId();



        List<Comment> taskComments = new ArrayList<>();

        for (String s : ApproveDescTypeEnum.getTypeList()) {
            List<Comment> approveDescList = taskService.getTaskComments(taskId, s);
            taskComments.addAll(approveDescList);


        }

        //查询所有的附件
        List<Attachment> taskAttachments = taskService.getTaskAttachments(taskId);



        List<SimpleApproveDescDto> simpleApproveDescDtoList = new ArrayList<>();


        for (Comment comment : taskComments) {
            String id = comment.getId();
            Date time = comment.getTime();
            String fullMessage = comment.getFullMessage();
            TaskCommentDto taskCommentDto = JsonUtil.parseObject(fullMessage, TaskCommentDto.class);


            String userId = taskCommentDto.getUserId();
            Boolean isSys = taskCommentDto.getSys();


            SimpleApproveDescDto simpleApproveDescDto = new SimpleApproveDescDto();
            simpleApproveDescDto.setDate(time);
            simpleApproveDescDto.setMsgId(id);
            simpleApproveDescDto.setSys(isSys);
            simpleApproveDescDto.setUserId(userId);
            simpleApproveDescDto.setType(comment.getType());
            simpleApproveDescDto.setMessage(fullMessage);


            //图片文件
            {
                List<Attachment> collect = taskAttachments.stream()
                        .filter(w -> StrUtil.equals(w.getDescription(), id))
                        .filter(w -> StrUtil.equals(w.getType(), ApproveAttachmentTypeEnum.IMAGE.getType()))
                        .collect(Collectors.toList());
                List<UploadValue> approveImageList=new ArrayList<>();
                for (Attachment attachment : collect) {
                    UploadValue uploadValue=new UploadValue();
                    uploadValue.setUrl(attachment.getUrl());
                    uploadValue.setName(attachment.getName());
                    approveImageList.add(uploadValue);
                }
                simpleApproveDescDto.setApproveImageList(approveImageList);
            }

            {
                List<Attachment> collect = taskAttachments.stream()
                        .filter(w -> StrUtil.equals(w.getDescription(), id))
                        .filter(w -> StrUtil.equals(w.getType(), ApproveAttachmentTypeEnum.FILE.getType()))
                        .collect(Collectors.toList());
                List<UploadValue> approveImageList=new ArrayList<>();
                for (Attachment attachment : collect) {
                    UploadValue uploadValue=new UploadValue();
                    uploadValue.setUrl(attachment.getUrl());
                    uploadValue.setName(attachment.getName());
                    approveImageList.add(uploadValue);
                }
                simpleApproveDescDto.setApproveFileList(approveImageList);
            }

            simpleApproveDescDtoList.add(simpleApproveDescDto);


        }
        return R.success(simpleApproveDescDtoList);
    }

    /**
     * 查询流程实例评论
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R queryProcessInstanceComments(String processInstanceId) {
        List<Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);


        List<Attachment> taskAttachments = taskService.getProcessInstanceAttachments(processInstanceId);


        List<SimpleApproveDescDto> simpleApproveDescDtoList = new ArrayList<>();


        for (Comment comment : taskComments) {
            String id = comment.getId();
            Date time = comment.getTime();
            String fullMessage = comment.getFullMessage();
            TaskCommentDto taskCommentDto = JsonUtil.parseObject(fullMessage, TaskCommentDto.class);


            String userId = taskCommentDto.getUserId();
            Boolean isSys = taskCommentDto.getSys();


            SimpleApproveDescDto simpleApproveDescDto = new SimpleApproveDescDto();
            simpleApproveDescDto.setDate(time);
            simpleApproveDescDto.setMsgId(id);
            simpleApproveDescDto.setSys(isSys);
            simpleApproveDescDto.setUserId(userId);
            simpleApproveDescDto.setType(comment.getType());
            simpleApproveDescDto.setMessage(fullMessage);


            //图片文件
            {
                List<Attachment> collect = taskAttachments.stream()
                        .filter(w -> StrUtil.equals(w.getDescription(), id))
                        .filter(w -> StrUtil.equals(w.getType(), ApproveAttachmentTypeEnum.IMAGE.getType()))
                        .collect(Collectors.toList());
                List<UploadValue> approveImageList=new ArrayList<>();
                for (Attachment attachment : collect) {
                    UploadValue uploadValue=new UploadValue();
                    uploadValue.setUrl(attachment.getUrl());
                    uploadValue.setName(attachment.getName());
                    approveImageList.add(uploadValue);
                }
                simpleApproveDescDto.setApproveImageList(approveImageList);
            }

            {
                List<Attachment> collect = taskAttachments.stream()
                        .filter(w -> StrUtil.equals(w.getDescription(), id))
                        .filter(w -> StrUtil.equals(w.getType(), ApproveAttachmentTypeEnum.FILE.getType()))
                        .collect(Collectors.toList());
                List<UploadValue> approveImageList=new ArrayList<>();
                for (Attachment attachment : collect) {
                    UploadValue uploadValue=new UploadValue();
                    uploadValue.setUrl(attachment.getUrl());
                    uploadValue.setName(attachment.getName());
                    approveImageList.add(uploadValue);
                }
                simpleApproveDescDto.setApproveFileList(approveImageList);
            }

            simpleApproveDescDtoList.add(simpleApproveDescDto);


        }

        return R.success(simpleApproveDescDtoList);

    }

    /**
     * 查询任务变量
     *
     * @param paramDto
     * @return
     */
    @Override
    public R queryTaskVariables(VariableQueryParamDto paramDto) {


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
     * 提交评论
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R submitComment(TaskParamDto taskParamDto) {


        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();

        if (task == null) {
            return R.fail("任务不存在");
        }
        Comment comment = saveUserCommentToTask(ApproveDescTypeEnum.COMMENT.getType(), taskParamDto.getApproveDesc(), taskParamDto.getUserId(),
                "添加了评论", taskParamDto.getTaskId(), taskParamDto.getProcessInstanceId());

        //保存图片和文件
        saveAttachment(taskParamDto, comment.getId(),taskParamDto.getTaskId() ,taskParamDto.getProcessInstanceId() );

        return R.success();
    }
}
