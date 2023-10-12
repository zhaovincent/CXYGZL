package com.cxygzl.core.listeners;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.RuntimeService;

import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID;

/**
 * 节点单个条件处理器
 */
public interface EventListenerStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        EventListenerStrategyFactory.register(key, this);
    }


    /**
     * 处理数据
     * @param event
     * @return
     */
    void handle(FlowableEvent event);



    default void saveStartEventContent(String flowId, String processInstanceId,
                                              String activityId, String activityName, String executionId) {
        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);

        Map<String, Object> processVariables = runtimeService.getVariables(executionId);



        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, activityId);
        ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
        processInstanceNodeRecordParamDto.setFlowId(flowId);
        processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setChildExecutionId(childExecutionIdList);
        processInstanceNodeRecordParamDto.setData(JsonUtil.toJSONString(processVariables));
        processInstanceNodeRecordParamDto.setNodeId(activityId);
//        processInstanceNodeRecordParamDto.setFromNodeId();
        processInstanceNodeRecordParamDto.setParentNodeId(MapUtil.getStr(processVariables, StrUtil.format("{}_parent_id", activityId)));
        processInstanceNodeRecordParamDto.setFlowUniqueId(MapUtil.getStr(processVariables, FLOW_UNIQUE_ID));
        if (node != null) {

            processInstanceNodeRecordParamDto.setNodeType((node.getType()));

        }
        processInstanceNodeRecordParamDto.setNodeName(activityName);
        processInstanceNodeRecordParamDto.setExecutionId(executionId);
        BizHttpUtil.startNodeEvent(processInstanceNodeRecordParamDto);


        //清除变量
        runtimeService.removeVariable(executionId, StrUtil.format("{}_parent_id", activityId));
    }

}
