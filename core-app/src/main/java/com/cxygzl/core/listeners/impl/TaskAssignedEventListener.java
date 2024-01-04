package com.cxygzl.core.listeners.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.task.api.DelegationState;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 任务完成了
 */
@Slf4j
@Component
public class TaskAssignedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {
        //任务被指派了人员
        FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
        TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
        //执行人id
        String assignee = task.getAssignee();
        //任务拥有者
        String owner = task.getOwner();
        //
        String delegationStateString = task.getDelegationStateString();


        //nodeid
        String taskDefinitionKey = task.getTaskDefinitionKey();

        //实例id
        String processInstanceId = task.getProcessInstanceId();

        String processDefinitionId = task.getProcessDefinitionId();
        //流程id
        String flowId =com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);
        ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
        processInstanceAssignUserRecordParamDto.setFlowId(flowId);
        processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
//        processNodeRecordAssignUserParamDto.setData();
        processInstanceAssignUserRecordParamDto.setNodeId(taskDefinitionKey);
        processInstanceAssignUserRecordParamDto.setUserId((assignee));
        processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
        processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
        processInstanceAssignUserRecordParamDto.setTaskType(StrUtil.equals(DelegationState.PENDING.toString(), delegationStateString) ? "DELEGATION" : (StrUtil.equals(DelegationState.RESOLVED.toString(), delegationStateString) ? "RESOLVED" : ""));
        processInstanceAssignUserRecordParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
        processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());

        BizHttpUtil.startAssignUser(processInstanceAssignUserRecordParamDto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.TASK_ASSIGNED.toString());

    }
}
