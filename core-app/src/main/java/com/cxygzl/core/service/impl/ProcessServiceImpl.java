package com.cxygzl.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.R;
import com.cxygzl.core.cmd.ExpressionCmd;
import com.cxygzl.core.service.IProcessService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 16:40
 */
//@Component
@Slf4j
public class ProcessServiceImpl implements IProcessService {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

//    @Autowired
//    ModelService modelService;

    @Autowired
    ManagementService managementService;

    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    /**
     * 1. 首先拿到BpmnModel，所有流程定义信息都可以通过BpmnModel获取；若流程尚未发起，则用modelId查询最新部署的流程定义数据；
     * 若流程已经发起，可以通过流程实例的processDefinitionId查询流程定义的历史数据。
     * @param variableMap 流程变量，用于计算条件分支
     */
    public R<List<FlowElement>> calApprovePath(String processInstanceId, String modelId, Map<String, Object> variableMap){


        BpmnModel bpmnModel;
        if(StrUtil.isNotBlank(processInstanceId)){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        }else {
//            Model model = modelService.getModel(modelId);
//            bpmnModel = modelService.getBpmnModel(model);
        }
//        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
//        List<FlowElement> passElements = new ArrayList<>();
//        this.dueStartElement(passElements, flowElements, variableMap);
//        return R.success(passElements);
        return null;
    }

    /**
     * 2. 找到开始节点，通过它的目标节点，然后再不断往下找。
     */
    private void dueStartElement(List<FlowElement> passElements, Collection<FlowElement> flowElements, Map<String, Object> variableMap){
        Optional<FlowElement> startElementOpt = flowElements.stream().filter(flowElement -> flowElement instanceof StartEvent).findFirst();
        startElementOpt.ifPresent(startElement -> {
            flowElements.remove(startElement);
            List<SequenceFlow> outgoingFlows = ((StartEvent) startElement).getOutgoingFlows();
            String targetRef = outgoingFlows.get(0).getTargetRef();
            // 根据ID找到FlowElement
            FlowElement targetElementOfStartElement = getFlowElement(flowElements, targetRef);
            if (targetElementOfStartElement instanceof UserTask) {
                this.getPassElementList(passElements, flowElements, targetElementOfStartElement, variableMap);
            }
        });
    }

    /**
     * 3. 我只用到了UserTask、ExclusiveGateway、ParallelGateway，所以代码里只列举了这三种，如果用到了其他的，可以再自己补充
     */
    private void getPassElementList(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap){
        // 任务节点
        if (curFlowElement instanceof UserTask) {
            this.dueUserTaskElement(passElements, flowElements, curFlowElement, variableMap);
            return;
        }
        // 排他网关
        if (curFlowElement instanceof ExclusiveGateway) {
            this.dueExclusiveGateway(passElements, flowElements, curFlowElement, variableMap);
            return;
        }
        // 并行网关
        if(curFlowElement instanceof ParallelGateway){
            this.dueParallelGateway(passElements, flowElements, curFlowElement, variableMap);
        }
    }

    private void dueUserTaskElement(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap){
        passElements.add(curFlowElement);
        List<SequenceFlow> outgoingFlows = ((UserTask) curFlowElement).getOutgoingFlows();
        String targetRef = outgoingFlows.get(0).getTargetRef();
        if (outgoingFlows.size() > 1) {
            // 找到表达式成立的sequenceFlow
            SequenceFlow sequenceFlow = getSequenceFlow(variableMap, outgoingFlows);
            targetRef = sequenceFlow.getTargetRef();
        }
        // 根据ID找到FlowElement
        FlowElement targetElement = getFlowElement(flowElements, targetRef);
        this.getPassElementList(passElements, flowElements, targetElement, variableMap);
    }

    private void dueExclusiveGateway(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap){
        // 获取符合条件的sequenceFlow的目标FlowElement
        List<SequenceFlow> exclusiveGatewayOutgoingFlows = ((ExclusiveGateway) curFlowElement).getOutgoingFlows();
        flowElements.remove(curFlowElement);
        // 找到表达式成立的sequenceFlow
        SequenceFlow sequenceFlow = getSequenceFlow(variableMap, exclusiveGatewayOutgoingFlows);
        // 根据ID找到FlowElement
        FlowElement targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
        this.getPassElementList(passElements, flowElements, targetElement, variableMap);
    }

    private void dueParallelGateway(List<FlowElement> passElements, Collection<FlowElement> flowElements, FlowElement curFlowElement, Map<String, Object> variableMap){
        FlowElement targetElement;
        List<SequenceFlow> parallelGatewayOutgoingFlows = ((ParallelGateway) curFlowElement).getOutgoingFlows();
        for(SequenceFlow sequenceFlow : parallelGatewayOutgoingFlows){
            targetElement = getFlowElement(flowElements, sequenceFlow.getTargetRef());
            this.getPassElementList(passElements, flowElements, targetElement, variableMap);
        }
    }

    private FlowElement getFlowElement(Collection<FlowElement> flowElements, String targetRef) {
        return flowElements.stream().filter(flowElement -> targetRef.equals(flowElement.getId())).findFirst().orElse(null);
    }

    /**
     * 4. 根据传入的变量，计算出表达式成立的那一条SequenceFlow
     * @param variableMap
     * @param outgoingFlows
     * @return
     */
    private SequenceFlow getSequenceFlow(Map<String, Object> variableMap, List<SequenceFlow> outgoingFlows) {
        Optional<SequenceFlow> sequenceFlowOpt = outgoingFlows.stream().filter(item -> {
            try {
                return this.getElValue(item.getConditionExpression(), variableMap);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }).findFirst();
        return sequenceFlowOpt.orElse(outgoingFlows.get(0));
    }

    private boolean getElValue(String exp, Map<String, Object> variableMap){
        return managementService.executeCommand(new ExpressionCmd(runtimeService, processEngineConfiguration, null, exp, variableMap));
    }
}
