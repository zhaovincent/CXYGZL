package com.cxygzl.core.listeners.event_listener_impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_RESULT;

/**
 * 流程取消了
 * @author Huijun Zhao
 * @description
 * @date 2023-10-10 10:12
 */
@Slf4j
@Component
public class ProcessCancelEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {

        //流程开完成
        org.flowable.engine.delegate.event.impl.FlowableProcessCancelledEventImpl e = (org.flowable.engine.delegate.event.impl.FlowableProcessCancelledEventImpl) event;
        DelegateExecution execution = e.getExecution();
        String processInstanceId = e.getProcessInstanceId();

        String processDefinitionId = e.getProcessDefinitionId();
        String flowId = NodeUtil.getFlowId(processDefinitionId);

        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);


        //结果
        Integer finalResult = execution.getVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT), Integer.class);


        ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
        processInstanceParamDto.setProcessInstanceId(processInstanceId);
        processInstanceParamDto.setCancel(true);
        processInstanceParamDto.setResult(finalResult);
        processInstanceParamDto.setFlowId(flowId);
        processInstanceParamDto.setParamMap(variables);
        BizHttpUtil.endProcessEvent(processInstanceParamDto);
        {

            {
                //判断后置事件
                FlowSettingDto flowSettingDto = BizHttpUtil.queryProcessSetting(flowId).getData();
                if (flowSettingDto != null) {
                    HttpSetting backNotify = flowSettingDto.getBackNotify();
                    if (backNotify != null && backNotify.getEnable()) {

                        String result = HttpUtil.flowExtenstionHttpRequest(backNotify, variables, flowId, processInstanceId, null, processInstanceParamDto.getCancel());



                        if (StrUtil.isNotBlank(result)) {
                            Map<String, Object> resultMap = JsonUtil.parseObject(result, new JsonUtil.TypeReference<Map<String, Object>>() {
                            });
                            List<HttpSettingData> resultSetting = backNotify.getResult();
                            for (HttpSettingData httpSettingData : resultSetting) {
                                execution.setVariable(httpSettingData.getValue(), resultMap.get(httpSettingData.getField()));
                            }
                        }

                    }
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.PROCESS_CANCELLED.toString());

    }
}
