package com.cxygzl.core.listeners.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 任务完成了
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
        String taskDefinitionKey = task.getTaskDefinitionKey();

        //实例id
        String processInstanceId = task.getProcessInstanceId();

        String processDefinitionId = task.getProcessDefinitionId();
        //流程id
        String flowId =com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);
        ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
        processInstanceAssignUserRecordParamDto.setFlowId(flowId);
        processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
        processInstanceAssignUserRecordParamDto.setData(JSON.toJSONString(taskService.getVariables(task.getId())));
        processInstanceAssignUserRecordParamDto.setLocalData(JSON.toJSONString(taskService.getVariablesLocal(task.getId())));
        processInstanceAssignUserRecordParamDto.setNodeId(taskDefinitionKey);
        processInstanceAssignUserRecordParamDto.setUserId((assignee));
        processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
        processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
        processInstanceAssignUserRecordParamDto.setTaskType("COMPLETE");
        processInstanceAssignUserRecordParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
        processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());

        BizHttpUtil.taskEndEvent(processInstanceAssignUserRecordParamDto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.TASK_COMPLETED.toString());

    }
}
