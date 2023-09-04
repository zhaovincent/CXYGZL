package com.cxygzl.core.listeners;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

/**
 * 流程监听器
 */
@Slf4j
public class TaskCompleteEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {

        if (event.getType().toString().equals(FlowableEngineEventType.TASK_COMPLETED.toString())) {

            TaskService taskService = SpringUtil.getBean(TaskService.class);

            //任务完成
            FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
            TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
            //执行人id
            String assignee = task.getAssignee();

            //nodeid
            String nodeId = task.getTaskDefinitionKey();

            //实例id
            String processInstanceId = task.getProcessInstanceId();

            String processDefinitionId = task.getProcessDefinitionId();
            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);
            ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
            processNodeRecordAssignUserParamDto.setFlowId(flowId);
            processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
         //   processNodeRecordAssignUserParamDto.setParentExecutionId();
            processNodeRecordAssignUserParamDto.setData(JSON.toJSONString(taskService.getVariables(task.getId())));
            processNodeRecordAssignUserParamDto.setLocalData(JSON.toJSONString(taskService.getVariablesLocal(task.getId())));
            processNodeRecordAssignUserParamDto.setNodeId(nodeId);
            processNodeRecordAssignUserParamDto.setUserId((assignee));
            processNodeRecordAssignUserParamDto.setTaskId(task.getId());
            processNodeRecordAssignUserParamDto.setNodeName(task.getName());
            processNodeRecordAssignUserParamDto.setFlowUniqueId(task.getVariableLocal(ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID,String.class));
            String taskType = task.getVariableLocal(ProcessInstanceConstant.VariableKey.TASK_TYPE, String.class);
            //RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
            processNodeRecordAssignUserParamDto.setTaskType(taskType);




            processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());

            BizHttpUtil.taskCompletedEvent(processNodeRecordAssignUserParamDto);

        }

    }


    /**
     * @return whether or not the current operation should fail when this listeners execution throws an exception.
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * @return Returns whether this event listener fires immediately when the event occurs or
     * on a transaction lifecycle event (before/after commit or rollback).
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    /**
     * @return if non-null, indicates the point in the lifecycle of the current transaction when the event should be fired.
     */
    @Override
    public String getOnTransaction() {
        return null;
    }
}
