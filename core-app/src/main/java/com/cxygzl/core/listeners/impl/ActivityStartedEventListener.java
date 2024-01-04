package com.cxygzl.core.listeners.impl;

import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.node.NodeDataStoreFactory;
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
public class ActivityStartedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {
        //节点开始执行
        //org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl
        FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
        String activityId = flowableActivityEvent.getActivityId();
        String activityName = flowableActivityEvent.getActivityName();
        log.debug("节点id：{} 名字:{}", activityId, activityName);


        String processInstanceId = flowableActivityEvent.getProcessInstanceId();

        String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
        String flowId =com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, activityId);


        ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
        processInstanceNodeRecordParamDto.setFlowId(flowId);
        processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
        processInstanceNodeRecordParamDto.setNodeId(activityId);
        if (node!=null) {

            processInstanceNodeRecordParamDto.setNodeType(String.valueOf(node.getType()));

        }
        processInstanceNodeRecordParamDto.setNodeName(activityName);
        processInstanceNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
        BizHttpUtil.startNodeEvent(processInstanceNodeRecordParamDto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.ACTIVITY_STARTED.toString());

    }
}
