package com.cxygzl.core.listeners;

import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;

/**
 * 节点取消---驳回跳转节点了
 */
@Slf4j
public class NodeCanceledEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {

        if (
                event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_CANCELLED.toString())
        ) {
            //任务取消了
            org.flowable.engine.delegate.event.impl.FlowableActivityCancelledEventImpl activityCancelledEvent = (org.flowable.engine.delegate.event.impl.FlowableActivityCancelledEventImpl) event;
            String activityId = activityCancelledEvent.getActivityId();
            String activityName = activityCancelledEvent.getActivityName();

            String executionId = activityCancelledEvent.getExecutionId();
            String processInstanceId = activityCancelledEvent.getProcessInstanceId();

            log.info("取消的节点：{}   {} {}  {}", activityId, activityName,processInstanceId,executionId);

            ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();

            processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);

            processNodeRecordAssignUserParamDto.setExecutionId(executionId);
            processNodeRecordAssignUserParamDto.setNodeId(activityId);
            processNodeRecordAssignUserParamDto.setNodeName(activityName);

            processNodeRecordAssignUserParamDto.setTaskType(ProcessInstanceConstant.TaskType.REJECT);

            CoreHttpUtil.taskCancelEvent(processNodeRecordAssignUserParamDto);
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
