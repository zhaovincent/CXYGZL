package com.cxygzl.core.listeners.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.common.constants.MessageTypeEnum;
import com.cxygzl.common.constants.NodeExpireSettingTypeEnum;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.ExpireSetting;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.FlowableUtils;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.job.service.impl.persistence.entity.JobEntityImpl;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务创建了
 */
@Slf4j
@Component
public class TaskCreatedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {


        //任务被创建了
        org.flowable.common.engine.impl.event.FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;

        TaskEntity taskEntity = (TaskEntity) flowableEntityEvent.getEntity();


        String processInstanceId = taskEntity.getProcessInstanceId();
        String processDefinitionId = taskEntity.getProcessDefinitionId();

        String flowId =com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);

        String taskId = taskEntity.getId();
        String assignee = taskEntity.getAssignee();
        MessageDto messageDto = MessageDto.builder()
                .userId(assignee)
                .flowId(flowId)
                .processInstanceId(processInstanceId)

                .uniqueId(taskId)
                .param(JSON.toJSONString(taskEntity.getVariables()))

                .type(MessageTypeEnum.TODO_TASK.getType())
                .readed(false).build();
        BizHttpUtil.saveMessage(messageDto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.TASK_CREATED.toString());

    }
}
