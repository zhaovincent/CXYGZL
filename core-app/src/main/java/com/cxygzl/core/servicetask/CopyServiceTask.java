package com.cxygzl.core.servicetask;

import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessInstanceCopyDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.node.AssignUserStrategyFactory;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 抄送任务处理器--java服务任务
 */
public class CopyServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity= (ExecutionEntityImpl) execution;
        String nodeId = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);

//发起人
        Object rootUserObj = execution.getVariable(ProcessInstanceConstant.VariableKey.STARTER);
        NodeUser rootUser = JsonUtil.parseArray(JsonUtil.toJSONString(rootUserObj), NodeUser.class).get(0);
        String rootUserId = rootUser.getId();
        if (node != null) {

            Map<String, Object> variables = execution.getVariables();

            Integer assignedType = node.getAssignedType();

            List<String> userIdList = AssignUserStrategyFactory.getStrategy(assignedType).handle(node, rootUser, variables);



            for (String userIds : userIdList) {
                //发送抄送任务
                ProcessInstanceCopyDto processInstanceCopyDto = new ProcessInstanceCopyDto();
                processInstanceCopyDto.setNodeTime(new Date());
                processInstanceCopyDto.setStartUserId((rootUser.getId()));
                processInstanceCopyDto.setFlowId(flowId);
                processInstanceCopyDto.setProcessInstanceId(execution.getProcessInstanceId());
                processInstanceCopyDto.setNodeId(nodeId);
                processInstanceCopyDto.setNodeName(node.getNodeName());
                processInstanceCopyDto.setFormData(JsonUtil.toJSONString(variables));
                processInstanceCopyDto.setUserId((userIds));

                BizHttpUtil.saveCC(processInstanceCopyDto);
            }
        }







    }
}
