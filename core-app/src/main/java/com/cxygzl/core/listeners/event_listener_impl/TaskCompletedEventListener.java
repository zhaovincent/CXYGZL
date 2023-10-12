package com.cxygzl.core.listeners.event_listener_impl;

import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 任务完成
 * @author Huijun Zhao
 * @description
 * @date 2023-10-10 10:12
 */
@Slf4j
@Component
public class TaskCompletedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {

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
        ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
        processInstanceAssignUserRecordParamDto.setFlowId(flowId);
        processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
        //   processNodeRecordAssignUserParamDto.setParentExecutionId();
        processInstanceAssignUserRecordParamDto.setData(JsonUtil.toJSONString(taskService.getVariables(task.getId())));
        processInstanceAssignUserRecordParamDto.setLocalData(JsonUtil.toJSONString(taskService.getVariablesLocal(task.getId())));
        processInstanceAssignUserRecordParamDto.setNodeId(nodeId);
        processInstanceAssignUserRecordParamDto.setUserId((assignee));
        processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
        processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
        processInstanceAssignUserRecordParamDto.setFlowUniqueId(task.getVariableLocal(ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID,String.class));
        String taskType = task.getVariableLocal(ProcessInstanceConstant.VariableKey.TASK_TYPE, String.class);
        //RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        processInstanceAssignUserRecordParamDto.setTaskType(taskType);




        processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());

        BizHttpUtil.taskCompletedEvent(processInstanceAssignUserRecordParamDto);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.TASK_COMPLETED.toString());

    }
}
