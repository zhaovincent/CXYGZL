package com.cxygzl.core.utils;


import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;

@Slf4j
public class FlowableUtils {
    /**
     * 获取节点数据
     * @param processInstanceId
     * @param nodeId
     * @return
     */
    public static FlowNode getFlowNode(String processInstanceId,String nodeId){

        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);

        String definitionld = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();        //获取bpm（模型）对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionld);
        //传节点定义key获取当前节点
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(nodeId);
        return  flowNode;
    }

}
