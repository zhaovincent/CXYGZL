package com.cxygzl.core.cmd;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.InjectUserTaskInProcessInstanceCmd;
import org.flowable.engine.impl.dynamic.BaseDynamicSubProcessInjectUtil;
import org.flowable.engine.impl.dynamic.DynamicUserTaskBuilder;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;

import java.util.List;

/**
 * 添加撤回网关节点
 */
@Slf4j
public class InjectRevokeGatewayCmd extends InjectUserTaskInProcessInstanceCmd {

    private String targetNodeId;


    private String gatewayId;

    public InjectRevokeGatewayCmd(String processInstanceId, DynamicUserTaskBuilder dynamicUserTaskBuilder,String targetNodeId,String gatewayId) {
        super(processInstanceId, dynamicUserTaskBuilder);


        this.targetNodeId = targetNodeId;

        this.gatewayId = gatewayId;

    }


    @Override
    protected void updateBpmnProcess(CommandContext commandContext, Process process, BpmnModel bpmnModel,
                                     ProcessDefinitionEntity originalProcessDefinitionEntity, DeploymentEntity newDeploymentEntity) {


        log.info("创建临时节点撤回合并网关：{}",gatewayId);

        //合并网关
        InclusiveGateway gateway = new InclusiveGateway();
        gateway.setId(gatewayId);
        gateway.setName(StrUtil.format("{}_撤回合并网关", "临时节点"));


        process.addFlowElement(gateway);

        //网关->撤回的任务节点连线
        UserTask userTask = (UserTask) process.getFlowElement(targetNodeId);
        SequenceFlow newFlow = new SequenceFlow();
        newFlow.setSourceRef(gatewayId);
        newFlow.setTargetRef(targetNodeId);
        newFlow.setTargetFlowElement(userTask);
        process.addFlowElement(newFlow);


        BaseDynamicSubProcessInjectUtil.processFlowElements(commandContext, process,
                bpmnModel, originalProcessDefinitionEntity,
                newDeploymentEntity);
    }

    /**
     * https://blog.csdn.net/zhuzhoulin/article/details/105516564
     * @param commandContext
     * @param processDefinitionEntity
     * @param processInstance
     * @param childExecutions
     */
    @Override
    protected void updateExecutions(CommandContext commandContext, ProcessDefinitionEntity processDefinitionEntity, ExecutionEntity processInstance, List<ExecutionEntity> childExecutions) {
       // ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager(commandContext);
       // ExecutionEntity execution = executionEntityManager.createChildExecution(processInstance);

       // BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(processDefinitionEntity.getId());
      //  InclusiveGateway flowElement = (InclusiveGateway) bpmnModel.getProcessById(processDefinitionEntity.getKey()).getFlowElement(gatewayId);
        //execution.setCurrentFlowElement(flowElement);

     //   Context.getAgenda().planContinueProcessOperation(execution);
    }

}