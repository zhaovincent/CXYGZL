package com.cxygzl.core.listeners.event_listener_impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.impl.FlowableSequenceFlowTakenEventImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID;

/**
 * 分支执行
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-10-10 10:12
 */
@Slf4j
@Component
public class SequenceFlowTakenEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {

        org.flowable.engine.delegate.event.impl.FlowableSequenceFlowTakenEventImpl e = (FlowableSequenceFlowTakenEventImpl) event;

        String executionId = e.getExecutionId();
        String activityId = e.getId();
        String processInstanceId = e.getProcessInstanceId();
        String processDefinitionId = e.getProcessDefinitionId();
        String flowId = NodeUtil.getFlowId(processDefinitionId);
        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);


        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, activityId);
        if (node == null) {
            return;
        }


        Map<String, Object> processVariables = runtimeService.getVariables(executionId);

        {
            ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
            processInstanceNodeRecordParamDto.setFlowId(flowId);
            processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setChildExecutionId(childExecutionIdList);
            processInstanceNodeRecordParamDto.setData(JsonUtil.toJSONString(processVariables));
            processInstanceNodeRecordParamDto.setNodeId(activityId);
//        processInstanceNodeRecordParamDto.setFromNodeId();
            processInstanceNodeRecordParamDto.setParentNodeId(MapUtil.getStr(processVariables, StrUtil.format("{}_parent_id", activityId)));
            processInstanceNodeRecordParamDto.setFlowUniqueId(MapUtil.getStr(processVariables, FLOW_UNIQUE_ID));


            processInstanceNodeRecordParamDto.setNodeType((node.getType()));

            processInstanceNodeRecordParamDto.setNodeName(node.getNodeName());
            processInstanceNodeRecordParamDto.setExecutionId(executionId);
            BizHttpUtil.startNodeEvent(processInstanceNodeRecordParamDto);
        }
        {

            ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
            processInstanceNodeRecordParamDto.setFlowId(flowId);
            processInstanceNodeRecordParamDto.setExecutionId(executionId);
            processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
            processInstanceNodeRecordParamDto.setData(JsonUtil.toJSONString(processVariables));
            processInstanceNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processInstanceNodeRecordParamDto.setNodeName(node.getNodeName());

            BizHttpUtil.endNodeEvent(processInstanceNodeRecordParamDto);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.SEQUENCEFLOW_TAKEN.toString());

    }
}
