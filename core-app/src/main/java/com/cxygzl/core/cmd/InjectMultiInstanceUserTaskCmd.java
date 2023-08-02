package com.cxygzl.core.cmd;


import cn.hutool.core.util.StrUtil;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.InjectUserTaskInProcessInstanceCmd;
import org.flowable.engine.impl.dynamic.BaseDynamicSubProcessInjectUtil;
import org.flowable.engine.impl.dynamic.DynamicUserTaskBuilder;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-02 14:00
 */

public class InjectMultiInstanceUserTaskCmd extends InjectUserTaskInProcessInstanceCmd {
    private String nodeId;
    private String oriNodeId;
    private UserTask currentUserTask;
    private String processInstanceId;
    private DynamicUserTaskBuilder dynamicUserTaskBuilder;

    public InjectMultiInstanceUserTaskCmd(
            String processInstanceId,
            String nodeId,
            String oriNodeId,
            DynamicUserTaskBuilder dynamicUserTaskBuilder) {
        super(processInstanceId, dynamicUserTaskBuilder);
        this.processInstanceId = processInstanceId;
        this.nodeId = nodeId;
        this.oriNodeId = oriNodeId;
        this.dynamicUserTaskBuilder = dynamicUserTaskBuilder;

    }

    @Override
    protected void updateBpmnProcess(CommandContext commandContext, Process process, BpmnModel bpmnModel,
                                     ProcessDefinitionEntity originalProcessDefinitionEntity, DeploymentEntity newDeploymentEntity) {
        // 创建动态节点
        UserTask userTask = new UserTask();

        userTask.setId(dynamicUserTaskBuilder.getId());

        dynamicUserTaskBuilder.setDynamicTaskId(userTask.getId());
        userTask.setName(dynamicUserTaskBuilder.getName());
//        userTask.setAssignee(dynamicUserTaskBuilder.getAssignee());
        // 创建多实例对象，设置多实例条件
        MultiInstanceLoopCharacteristics characteristics = new MultiInstanceLoopCharacteristics();
        // 设置多实例用户变量
        characteristics.setElementVariable(StrUtil.format("{}_assignee_temp", userTask.getId()));

        // 设置获取用户信息的方法处理器
        // 这里不能使用setCollectionString()方法，根据官方说法，
        // 当使用的表达式或者类获取用户列表是，应当使用setInputDataItem代替
        // 具体handler可以搜一下，还是很简单
        characteristics.setInputDataItem(StrUtil.format("${{}_assignee_list}", userTask.getId()));
        // 设置是并行还是串行
        characteristics.setSequential(false);
        // 设置任务完成条件
//        characteristics.setCompletionCondition("${nrOfCompletedInstances>=nrOfInstances}");
        characteristics.setCompletionCondition("${multiInstanceHandler.completionCondition(execution)}");


        // 从多实例变量中取审批人
        userTask.setAssignee(StrUtil.format("${{}_assignee_temp}", userTask.getId()));

        userTask.setLoopCharacteristics(characteristics);
        Map<String, List<ExtensionAttribute>> attributeMap = new HashMap<>();
        // 设置节点属性, 这个根据代码处理逻辑而定
        if(StrUtil.isNotBlank(oriNodeId)) {
            userTask.setExtensionId(oriNodeId);
        }
        process.addFlowElement(userTask);
        // 获取当前节点
        this.currentUserTask = (UserTask) process.getFlowElement(nodeId);
        // 获取出口
        List<SequenceFlow> outgoingFlows = this.currentUserTask.getOutgoingFlows();
        // 出线口指向当前节点
        // 线条的改变规则其实很简单
        // prev -> next
        // 修改为 prev -> dynamicTask -> next
        for (SequenceFlow flow : outgoingFlows) {
            SequenceFlow newFlow = new SequenceFlow();
            newFlow.setSourceRef(userTask.getId());
            newFlow.setTargetRef(flow.getTargetRef());
            newFlow.setTargetFlowElement(flow.getTargetFlowElement());
            process.addFlowElement(newFlow);
            flow.setTargetRef(userTask.getId());
            flow.setTargetFlowElement(userTask);
        }
        BaseDynamicSubProcessInjectUtil.processFlowElements(commandContext, process,
                bpmnModel, originalProcessDefinitionEntity,
                newDeploymentEntity);
    }

}