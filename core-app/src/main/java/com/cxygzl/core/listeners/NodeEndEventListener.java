package com.cxygzl.core.listeners;

import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableMultiInstanceActivityCompletedEventImpl;

/**
 * 流程监听器
 */
@Slf4j
public class NodeEndEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {

        if (
                event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION.toString())
                        ||
                        event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED.toString())
        ) {
            //多实例任务
            FlowableMultiInstanceActivityCompletedEventImpl flowableActivityEvent = (FlowableMultiInstanceActivityCompletedEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();

            log.info("实例完成1 节点id：{} 名字:{}", activityId, activityName);


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
            processNodeRecordParamDto.setFlowId(flowId);
            processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processNodeRecordParamDto.setNodeName(activityName);

            CoreHttpUtil.endNodeEvent(processNodeRecordParamDto);


        }
        if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_COMPLETED.toString())) {
            //节点完成执行

            FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.info("实例完成2 节点id：{} 名字:{}", activityId, activityName);

            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
            processNodeRecordParamDto.setFlowId(flowId);
            processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processNodeRecordParamDto.setNodeName(activityName);

            CoreHttpUtil.endNodeEvent(processNodeRecordParamDto);

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
