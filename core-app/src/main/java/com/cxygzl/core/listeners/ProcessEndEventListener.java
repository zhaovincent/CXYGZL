package com.cxygzl.core.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


            ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
            processInstanceParamDto.setProcessInstanceId(processInstanceId);
            processInstanceParamDto.setCancel(MapUtil.getBool(variables,
                    ProcessInstanceConstant.VariableKey.CANCEL
                    , false));
            CoreHttpUtil.endProcessEvent(processInstanceParamDto);
            {
                String flowId = entity.getProcessDefinitionKey();

                {
                    //判断后置事件
                    FlowSettingDto flowSettingDto = CoreHttpUtil.queryProcessSetting(flowId).getData();
                    if (flowSettingDto != null) {
                        HttpSetting backNotify = flowSettingDto.getBackNotify();
                        if (backNotify != null && backNotify.getEnable()) {

                            Map<String, String> headerParamMap = new HashMap<>();
                            {
                                List<HttpSettingData> headerSetting = backNotify.getHeader();
                                for (HttpSettingData httpSettingData : headerSetting) {
                                    if (httpSettingData.getValueMode()) {
                                        headerParamMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                    } else {
                                        Object object = variables.get(httpSettingData.getValue());


                                        headerParamMap.put(httpSettingData.getField(), object == null ? null : (object instanceof String ? Convert.toStr(object) : JSON.toJSONString(object)));

                                    }
                                }

                            }


                            Map<String, Object> bodyMap = new HashMap<>();
                            {
                                //存入默认值
                                bodyMap.put("flowId", flowId);
                                bodyMap.put("cancel", MapUtil.getBool(variables,
                                        ProcessInstanceConstant.VariableKey.CANCEL
                                        , false));
                                bodyMap.put("processInstanceId", processInstanceId);
                                List<HttpSettingData> bodySetting = backNotify.getBody();
                                for (HttpSettingData httpSettingData : bodySetting) {
                                    if (httpSettingData.getValueMode()) {
                                        bodyMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                    } else {
                                        bodyMap.put(httpSettingData.getField(), (variables.get(httpSettingData.getValue())));
                                    }
                                }

                            }
                            log.info("后置事件url：{} 请求头：{} 请求体：{} ", backNotify.getUrl(), JSON.toJSONString(headerParamMap), JSON.toJSONString(bodyMap));


                            String result = null;
                            try {
                                result = HttpRequest.post(backNotify.getUrl())
                                        .header(Header.USER_AGENT, "CXYGZL")//头信息，多个头信息多次调用此方法即可
                                        .headerMap(headerParamMap, true)
                                        .body(JSON.toJSONString(bodyMap))
                                        .timeout(10000)//超时，毫秒
                                        .execute().body();
                                log.info(" 返回值:{}", result);
                            } catch (Exception ex) {
                                log.error("后置事件异常", e);
                            }

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
