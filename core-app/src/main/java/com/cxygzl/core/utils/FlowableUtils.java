package com.cxygzl.core.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FlowableUtils {

    public static Map<String, List<ExtensionElement>> generateFlowNodeIdExtensionMap(String nodeId) {
        Map<String, List<ExtensionElement>> extensionElements = new HashMap<>();
        ExtensionElement extensionElement = generateFlowNodeIdExtension(nodeId);
        extensionElements.put(ProcessInstanceConstant.VariableKey.SYS_CODE, CollUtil.newArrayList(extensionElement));
        return extensionElements;
    }


    public static ExtensionElement generateFlowNodeIdExtension(String nodeId) {
        ExtensionElement extensionElement=new ExtensionElement();
        extensionElement.setElementText(nodeId);
        extensionElement.setName("nodeId");
        extensionElement.setNamespacePrefix("flowable");
        extensionElement.setNamespace("nodeId");
        return extensionElement;
    }


    public static String getNodeIdFromExtension(FlowElement flowElement){

        Map<String, List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();

        return  extensionElements.get("nodeId").get(0).getElementText();
    }



    public static ExtensionAttribute generate(String key, String val) {
        ExtensionAttribute ea = new ExtensionAttribute();
        ea.setNamespace("flowable");
        ea.setName(key);
        ea.setNamespacePrefix("custom");
        ea.setValue(val);
        return ea;
    }

    /**
     * 获取节点数据
     *
     * @param processInstanceId
     * @param nodeId
     * @return
     */
    public static FlowNode getFlowNode(String processInstanceId, String nodeId) {

        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);

        String definitionld = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();        //获取bpm（模型）对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionld);
        //传节点定义key获取当前节点
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(nodeId);
        return flowNode;
    }

}
