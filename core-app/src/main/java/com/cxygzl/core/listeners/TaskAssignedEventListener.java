package com.cxygzl.core.listeners;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ApproveDescTypeEnum;
import com.cxygzl.common.constants.MessageTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.common.dto.SimpleApproveDescDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Comment;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程监听器
 */
@Slf4j
public class TaskAssignedEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {

        if (event.getType().toString().equals(FlowableEngineEventType.TASK_ASSIGNED.toString())) {

            {
                //任务被指派了人员
                FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;


                TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();


                //执行人id
                String assignee = task.getAssignee();
                //任务拥有者
                String owner = task.getOwner();
                //
                String delegationStateString = task.getDelegationStateString();


                //nodeid
                String taskDefinitionKey = task.getTaskDefinitionKey();

                //实例id
                String processInstanceId = task.getProcessInstanceId();

                String processDefinitionId = task.getProcessDefinitionId();
                //流程id
                String flowId = NodeUtil.getFlowId(processDefinitionId);
                ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
                processNodeRecordAssignUserParamDto.setFlowId(flowId);
                processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
              //  processNodeRecordAssignUserParamDto.setParentExecutionId();
//        processNodeRecordAssignUserParamDto.setData();
                processNodeRecordAssignUserParamDto.setNodeId(taskDefinitionKey);
                processNodeRecordAssignUserParamDto.setUserId((assignee));
                processNodeRecordAssignUserParamDto.setTaskId(task.getId());
                processNodeRecordAssignUserParamDto.setNodeName(task.getName());
                processNodeRecordAssignUserParamDto.setFlowUniqueId(
                        task.getVariable(ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID,String.class));

                String taskType = task.getVariableLocal(ProcessInstanceConstant.VariableKey.TASK_TYPE, String.class);

                processNodeRecordAssignUserParamDto.setTaskType(taskType);
                processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());


                List<SimpleApproveDescDto> simpleApproveDescDtoList = getSimpleApproveDescDtoList(task);

                processNodeRecordAssignUserParamDto.setSimpleApproveDescDtoList(simpleApproveDescDtoList);

                CoreHttpUtil.startAssignUser(processNodeRecordAssignUserParamDto);

            }
            {
                //待办消息

                //任务被创建了
                org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl flowableEntityEvent = (org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl) event;

                TaskEntity taskEntity = (TaskEntity) flowableEntityEvent.getEntity();


                String processInstanceId = taskEntity.getProcessInstanceId();
                String processDefinitionId = taskEntity.getProcessDefinitionId();

                String flowId = NodeUtil.getFlowId(processDefinitionId);

                String taskId = taskEntity.getId();
                String assignee = taskEntity.getAssignee();
                MessageDto messageDto = MessageDto.builder()
                        .userId(assignee)
                        .flowId(flowId)
                        .processInstanceId(processInstanceId)

                        .uniqueId(taskId)
                        .param(JSON.toJSONString(taskEntity.getVariables()))

                        .type(MessageTypeEnum.TODO_TASK.getType())
                        .readed(false).build();
                CoreHttpUtil.saveMessage(messageDto);
            }

        }

    }

    private static List<SimpleApproveDescDto> getSimpleApproveDescDtoList(TaskEntityImpl task) {
        TaskService taskService = SpringUtil.getBean(TaskService.class);

        List<Comment> taskComments = new ArrayList<>();

        for (String s : ApproveDescTypeEnum.getTypeList()) {
            List<Comment> approveDescList = taskService.getTaskComments(task.getId(),s);
            taskComments.addAll(approveDescList);
        }


        List<SimpleApproveDescDto> simpleApproveDescDtoList = new ArrayList<>();
        for (Comment comment : taskComments) {
            String id = comment.getId();
            Date time = comment.getTime();
            String fullMessage = comment.getFullMessage();

            SimpleApproveDescDto simpleApproveDescDto = new SimpleApproveDescDto();
            simpleApproveDescDto.setDate(time);
            simpleApproveDescDto.setMsgId(id);
            simpleApproveDescDto.setType(comment.getType());
            simpleApproveDescDto.setMessage(fullMessage);
            simpleApproveDescDtoList.add(simpleApproveDescDto);
        }
        return simpleApproveDescDtoList;
    }

    /**
     * @return whether or not the current operation should fail when this listeners execution throws an exception.
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * @return Returns whether this event listener fires immediately when the event occurs or
     * on a transaction lifecycle event (before/after commit or rollback).
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    /**
     * @return if non-null, indicates the point in the lifecycle of the current transaction when the event should be fired.
     */
    @Override
    public String getOnTransaction() {
        return null;
    }
}
