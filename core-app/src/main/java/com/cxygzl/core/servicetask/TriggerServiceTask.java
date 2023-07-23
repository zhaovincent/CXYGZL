package com.cxygzl.core.servicetask;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.core.node.NodeDataStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触发器任务处理器--java服务任务
 */
@Slf4j
public class TriggerServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String nodeId = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();
        String processInstanceId = entity.getProcessInstanceId();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);



        Map<String, Object> variables = execution.getVariables();


        //判断后置事件

        HttpSetting backNotify = node.getHttpSetting();

        Map<String, String> headerParamMap = new HashMap<>();
        {
            List<HttpSettingData> headerSetting = backNotify.getHeader();
            for (HttpSettingData httpSettingData : headerSetting) {
                String field = httpSettingData.getField();
                if(StrUtil.isNotBlank(field)){
                    if (httpSettingData.getValueMode()) {
                        headerParamMap.put(field, httpSettingData.getValue());
                    } else {
                        Object object = variables.get(httpSettingData.getValue());


                        headerParamMap.put(field, object == null ? null : (object instanceof String ? Convert.toStr(object) : JSON.toJSONString(object)));

                    }
                }

            }

        }


        Map<String, Object> bodyMap = new HashMap<>();
        {
            //存入默认值
            bodyMap.put("flowId", flowId);
            bodyMap.put("processInstanceId", processInstanceId);
            List<HttpSettingData> bodySetting = backNotify.getBody();
            for (HttpSettingData httpSettingData : bodySetting) {
                String field = httpSettingData.getField();
                if(StrUtil.isNotBlank(field)){
                    if (httpSettingData.getValueMode()) {
                        bodyMap.put(field, httpSettingData.getValue());
                    } else {
                        bodyMap.put(field, (variables.get(httpSettingData.getValue())));
                    }
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
            log.info(" 返回值:{}",  result);
        } catch (Exception e) {
           log.error("触发器异常",e);
        }

        if (StrUtil.isNotBlank(result)) {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            List<HttpSettingData> resultSetting = backNotify.getResult();
            for (HttpSettingData httpSettingData : resultSetting) {
                String value = httpSettingData.getValue();
                if(StrUtil.isNotBlank(value)){
                    execution.setVariable(value, resultMap.get(httpSettingData.getField()));
                }
            }
        }


    }
}
