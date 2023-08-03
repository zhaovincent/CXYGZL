package com.cxygzl.core.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.ProcessInstanceRecordParamDto;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程监听器
 */
@Slf4j
public class ProcessStartEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {


        if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_STARTED.toString())) {
            //流程开始了
            FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

            ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
            DelegateExecution execution = flowableProcessStartedEvent.getExecution();
            String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();
            String flowId = entity.getProcessDefinitionKey();
            Object variable = execution.getVariable(
                    ProcessInstanceConstant.VariableKey.STARTER);
            String startUserId = (JSON.parseArray(JSON.toJSONString(variable), NodeUser.class).get(0).getId());
            //上级实例id
            String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();

            Map<String, Object> variables = execution.getVariables();
            log.info("流程开始了变量是：{}", JSON.toJSONString(variables));

            {


                ProcessInstanceRecordParamDto processInstanceRecordParamDto = new ProcessInstanceRecordParamDto();
                processInstanceRecordParamDto.setUserId(startUserId);
                processInstanceRecordParamDto.setParentProcessInstanceId(nestedProcessInstanceId);
                processInstanceRecordParamDto.setFlowId(flowId);
                processInstanceRecordParamDto.setProcessInstanceId(processInstanceId);
                processInstanceRecordParamDto.setFormData(JSON.toJSONString(variables));
                CoreHttpUtil.startProcessEvent(processInstanceRecordParamDto);
            }
            {
                //判断前置事件
                FlowSettingDto flowSettingDto = CoreHttpUtil.queryProcessSetting(flowId).getData();
                if (flowSettingDto != null) {
                    HttpSetting frontNotify = flowSettingDto.getFrontNotify();
                    if (frontNotify != null && frontNotify.getEnable()) {

                        Map<String, String> headerParamMap = new HashMap<>();
                        {
                            List<HttpSettingData> headerSetting = frontNotify.getHeader();
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
                            bodyMap.put("processInstanceId", processInstanceId);
                            List<HttpSettingData> bodySetting = frontNotify.getBody();
                            for (HttpSettingData httpSettingData : bodySetting) {
                                if (httpSettingData.getValueMode()) {
                                    bodyMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                } else {
                                    bodyMap.put(httpSettingData.getField(), JSON.toJSONString(variables.get(httpSettingData.getValue())));
                                }
                            }

                        }

                        log.info("前置事件url：{} 请求头：{} 请求体：{} ", frontNotify.getUrl(), JSON.toJSONString(headerParamMap), JSON.toJSONString(bodyMap));

                        String result = null;
                        try {
                            result = HttpRequest.post(frontNotify.getUrl())
                                    .header(Header.USER_AGENT, "CXYGZL")//头信息，多个头信息多次调用此方法即可
                                    .headerMap(headerParamMap, true)
                                    .body(JSON.toJSONString(bodyMap))
                                    .timeout(10000)//超时，毫秒
                                    .execute().body();
                            log.info("  返回值:{}", result);
                        } catch (Exception e) {
                            log.error("前置事件异常", e);
                        }

                        if (StrUtil.isNotBlank(result)) {
                            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
                            });
                            List<HttpSettingData> resultSetting = frontNotify.getResult();
                            for (HttpSettingData httpSettingData : resultSetting) {
                                execution.setVariable(httpSettingData.getValue(), resultMap.get(httpSettingData.getField()));
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
