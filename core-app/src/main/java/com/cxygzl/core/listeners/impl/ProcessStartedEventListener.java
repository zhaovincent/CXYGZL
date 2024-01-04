package com.cxygzl.core.listeners.impl;

import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.ProcessInstanceRecordParamDto;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 任务完成了
 */
@Slf4j
@Component
public class ProcessStartedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {
        //流程开始了
        FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

        ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
        DelegateExecution execution = flowableProcessStartedEvent.getExecution();
        String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();
        {
            //上级实例id
            String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();

            String flowId = entity.getProcessDefinitionKey();

            Object variable = execution.getVariable(
                    "root");
            String startUserId =  (JSON.parseArray(JSON.toJSONString(variable), NodeUser.class).get(0).getId());
            Map<String, Object> variables = execution.getVariables();

            ProcessInstanceRecordParamDto processInstanceRecordParamDto = new ProcessInstanceRecordParamDto();
            processInstanceRecordParamDto.setUserId(startUserId);
            processInstanceRecordParamDto.setParentProcessInstanceId(nestedProcessInstanceId);
            processInstanceRecordParamDto.setFlowId(flowId);
            processInstanceRecordParamDto.setProcessInstanceId(processInstanceId);
            processInstanceRecordParamDto.setFormData(JSON.toJSONString(variables));
            BizHttpUtil.createProcessEvent(processInstanceRecordParamDto);
        }


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.PROCESS_STARTED.toString());

    }
}
