package com.cxygzl.core.servicetask;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.Refuse;
import com.cxygzl.core.node.NodeDataStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_RESULT;


/**
 * 审批任务处理器--java服务任务
 */
@Slf4j
public class ApproveServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String nodeIdO = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();
        String processInstanceId = entity.getProcessInstanceId();




        String nodeId = StrUtil.subAfter(nodeIdO,"approve_service_task_",true);

        Boolean approve = execution.getVariable(StrUtil.format("{}_approve_condition", nodeId), Boolean.class);

        if (approve != null) {

            //判断整体流程是通过还是拒绝
            execution.setVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT),
                    approve);


            RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);


            if (!approve) {
                //跳转

                Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);
                Refuse refuse = node.getRefuse();
                if (refuse != null) {
                    String handler = refuse.getHandler();
                    if (StrUtil.equals(handler, "TO_NODE")) {
                        runtimeService.createChangeActivityStateBuilder()
                                .processInstanceId(processInstanceId)
                                .moveActivityIdTo(nodeIdO, refuse.getNodeId())
                                .changeState();
                    }else{
                        runtimeService.createChangeActivityStateBuilder()
                                .processInstanceId(processInstanceId)
                                .moveActivityIdTo(nodeIdO, "end")
                                .changeState();
                    }
                }



            }

        }


    }


}
