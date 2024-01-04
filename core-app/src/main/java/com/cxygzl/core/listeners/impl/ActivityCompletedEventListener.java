package com.cxygzl.core.listeners.impl;

import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 节点开始了
 */
@Slf4j
@Component
public class ActivityCompletedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {
        FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
        String activityId = flowableActivityEvent.getActivityId();
        String activityName = flowableActivityEvent.getActivityName();
        log.debug("节点id：{} 名字:{}", activityId, activityName);


        String processInstanceId = flowableActivityEvent.getProcessInstanceId();

        String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
        String flowId =com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);

        ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
        processInstanceNodeRecordParamDto.setFlowId(flowId);
        processInstanceNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
        processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
        processInstanceNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
        processInstanceNodeRecordParamDto.setNodeName(activityName);

        BizHttpUtil.endNodeEvent(processInstanceNodeRecordParamDto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.ACTIVITY_COMPLETED.toString());

    }
}
