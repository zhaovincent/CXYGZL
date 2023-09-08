package com.cxygzl.core.listeners;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.List;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_RESULT;

/**
 * 流程监听器
 */
@Slf4j
public class ProcessEndEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {

        if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString())) {
            //流程开完成
            FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
            DelegateExecution execution = e.getExecution();
            String processInstanceId = e.getProcessInstanceId();
            ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();
            Map<String, Object> variables = execution.getVariables();
            String flowId = entity.getProcessDefinitionKey();


            //结果
            Integer finalResult = execution.getVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT), Integer.class);

            ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
            processInstanceParamDto.setProcessInstanceId(processInstanceId);
            processInstanceParamDto.setCancel(MapUtil.getBool(variables,
                    ProcessInstanceConstant.VariableKey.CANCEL
                    , false));
            processInstanceParamDto.setResult(finalResult);
            BizHttpUtil.endProcessEvent(processInstanceParamDto);
            {

                {
                    //判断后置事件
                    FlowSettingDto flowSettingDto = BizHttpUtil.queryProcessSetting(flowId).getData();
                    if (flowSettingDto != null) {
                        HttpSetting backNotify = flowSettingDto.getBackNotify();
                        if (backNotify != null && backNotify.getEnable()) {

                            String result = HttpUtil.flowExtenstionHttpRequest(backNotify, variables, flowId, processInstanceId);



                            if (StrUtil.isNotBlank(result)) {
                                Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
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
