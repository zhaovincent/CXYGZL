package com.cxygzl.core.listeners;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

/**
 * 实体删除
 */
@Slf4j
public class EntityDeleteEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {


        if (event.getType().toString().equals(FlowableEngineEventType.ENTITY_DELETED.toString())) {
            //流程开始了
            Object entity = null;
            if (event instanceof org.flowable.common.engine.impl.event.FlowableEntityEventImpl) {
                org.flowable.common.engine.impl.event.FlowableEntityEventImpl f = (org.flowable.common.engine.impl.event.FlowableEntityEventImpl) event;
                entity = f.getEntity();
            } else if (event instanceof org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl) {
                org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl f = (FlowableEntityEventImpl) event;
                entity = f.getEntity();
            }


            if (entity != null && entity instanceof TaskEntityImpl) {

                TaskService taskService = SpringUtil.getBean(TaskService.class);

                //任务完成
                FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
                TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
                String assignee = task.getAssignee();


                //nodeid
                String nodeId = task.getTaskDefinitionKey();

                //实例id
                String processInstanceId = task.getProcessInstanceId();

                String processDefinitionId = task.getProcessDefinitionId();
                //流程id
                String flowId = NodeUtil.getFlowId(processDefinitionId);
                ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
                processInstanceAssignUserRecordParamDto.setFlowId(flowId);
                processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
                //   processNodeRecordAssignUserParamDto.setParentExecutionId();
                processInstanceAssignUserRecordParamDto.setData(JSON.toJSONString(taskService.getVariables(task.getId())));
                processInstanceAssignUserRecordParamDto.setLocalData(JSON.toJSONString(taskService.getVariablesLocal(task.getId())));
                processInstanceAssignUserRecordParamDto.setNodeId(nodeId);
                processInstanceAssignUserRecordParamDto.setUserId((assignee));
                processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
                processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
                processInstanceAssignUserRecordParamDto.setFlowUniqueId(task.getVariableLocal(ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID, String.class));
                String taskType = task.getVariableLocal(ProcessInstanceConstant.VariableKey.TASK_TYPE, String.class);
                //RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
                processInstanceAssignUserRecordParamDto.setTaskType(taskType);


                processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());

                BizHttpUtil.taskEndEvent(processInstanceAssignUserRecordParamDto);


            }


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