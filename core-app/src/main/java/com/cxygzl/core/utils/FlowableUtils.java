package com.cxygzl.core.utils;


import cn.hutool.extra.spring.SpringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.flowable.engine.repository.ProcessDefinition;

import java.util.*;

/**
 * 流程引擎工具类封装
 * @author: linjinp
 * @create: 2019-12-24 13:51
 **/
public class FlowableUtils {
    /**
     * 获取流程名称
     * @param processDefinitionId
     * @return
     */
    public static String getFlowName(String processDefinitionId){
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        return processDefinition.getName();
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


    public static ExtensionElement generateFlowNodeIdExtension(String nodeId) {
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setElementText(nodeId);
        extensionElement.setName("nodeId");
        extensionElement.setNamespacePrefix("flowable");
        extensionElement.setNamespace("nodeId");
        return extensionElement;
    }


    public static String getNodeIdFromExtension(FlowElement flowElement) {

        Map<String, List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();

        return extensionElements.get("nodeId").get(0).getElementText();
    }

}
