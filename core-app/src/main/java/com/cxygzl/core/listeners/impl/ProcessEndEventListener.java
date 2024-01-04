package com.cxygzl.core.listeners.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_RESULT;

/**
 * 流程结束了
 * @author Huijun Zhao
 * @description
 * @date 2023-10-10 10:12
 */
@Slf4j
@Component
public class ProcessEndEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {

        //流程开完成
        FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
        DelegateExecution execution = e.getExecution();
        String processInstanceId = e.getProcessInstanceId();
        ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();
        String flowId = entity.getProcessDefinitionKey();

        Boolean finalResult = execution.getVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT), Boolean.class);



        ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
        processInstanceParamDto.setProcessInstanceId(processInstanceId);
        processInstanceParamDto.setResult(finalResult);
        BizHttpUtil.endProcessEvent(processInstanceParamDto);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString());

    }
}
