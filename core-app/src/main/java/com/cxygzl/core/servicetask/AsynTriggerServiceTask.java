package com.cxygzl.core.servicetask;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.FlowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.Map;

/**
 * 异步触发器任务处理器--java服务任务
 */
@Slf4j
public class AsynTriggerServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        ServiceTask currentFlowElement = (ServiceTask) entity.getCurrentFlowElement();


        String nodeId = FlowableUtils.getNodeIdFromExtension(currentFlowElement);
        String flowId = entity.getProcessDefinitionKey();
        String processInstanceId = entity.getProcessInstanceId();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);

        String messageId = StrUtil.format("message_notify_{}", node.getId());

        String executionId = execution.getId();


        Map<String, Object> variables = execution.getVariables();

        String s = IdUtil.fastSimpleUUID();
        NodeDataStoreFactory.getInstance().saveAll("msgId",s,messageId);
        NodeDataStoreFactory.getInstance().saveAll("nodeId",s,node.getId());


        //判断后置事件

        HttpSetting backNotify = node.getHttpSetting();

        String result = null;
        try {

            result = HttpUtil.flowExtenstionHttpRequest(backNotify, variables, flowId, processInstanceId, s);

            log.info(" 返回值:{}", result);
        } catch (Exception e) {
            log.error("触发器异常", e);
        }


    }
}
