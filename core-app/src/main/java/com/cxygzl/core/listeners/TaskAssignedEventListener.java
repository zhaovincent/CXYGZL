package com.cxygzl.core.listeners;

import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.MessageTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

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
                String flowId = com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);
                ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
                processInstanceAssignUserRecordParamDto.setFlowId(flowId);
                processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
              //  processNodeRecordAssignUserParamDto.setParentExecutionId();
//        processNodeRecordAssignUserParamDto.setData();
                processInstanceAssignUserRecordParamDto.setNodeId(taskDefinitionKey);
                processInstanceAssignUserRecordParamDto.setUserId((assignee));
                processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
                processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
                processInstanceAssignUserRecordParamDto.setFlowUniqueId(
                        task.getVariable(ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID,String.class));

                String taskType = task.getVariableLocal(ProcessInstanceConstant.VariableKey.TASK_TYPE, String.class);

                processInstanceAssignUserRecordParamDto.setTaskType(taskType);
                processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());




                BizHttpUtil.startAssignUser(processInstanceAssignUserRecordParamDto);

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
                BizHttpUtil.saveMessage(messageDto);
            }

        }

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
