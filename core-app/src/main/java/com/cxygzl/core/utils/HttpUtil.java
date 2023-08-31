package com.cxygzl.core.utils;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class HttpUtil {


    /**
     * 流程扩展请求
     * @param httpSetting
     * @param paramMap
     * @param flowId
     * @param processInstanceId
     * @return
     */
    public static String flowExtenstionHttpRequest(HttpSetting httpSetting, Map<String, Object> paramMap, String flowId,
                                                   String processInstanceId) {
        Map<String, String> headerParamMap = new HashMap<>();
        {
            List<HttpSettingData> headerSetting = httpSetting.getHeader();
            for (HttpSettingData httpSettingData : headerSetting) {
                if (httpSettingData.getValueMode()) {
                    headerParamMap.put(httpSettingData.getField(), httpSettingData.getValue());
                } else {
                    Object object = paramMap.get(httpSettingData.getValue());
                    headerParamMap.put(httpSettingData.getField(), object == null ? null : (object instanceof String ? Convert.toStr(object) : JSON.toJSONString(object)));
                }
            }

        }
        Map<String, Object> bodyMap = new HashMap<>();
        {
            //存入默认值
            bodyMap.put("flowId", flowId);
            bodyMap.put("processInstanceId", processInstanceId);
            List<HttpSettingData> bodySetting = httpSetting.getBody();
            for (HttpSettingData httpSettingData : bodySetting) {
                if (httpSettingData.getValueMode()) {
                    bodyMap.put(httpSettingData.getField(), httpSettingData.getValue());
                } else {
                    Object object = paramMap.get(httpSettingData.getValue());
                    bodyMap.put(httpSettingData.getField(), object == null ? null : (object instanceof String ?
                            Convert.toStr(object) : JSON.toJSONString(object)));
                }
            }

        }
        log.info("url：{} 请求头：{} 请求体：{} ", httpSetting.getUrl(), JSON.toJSONString(headerParamMap), JSON.toJSONString(bodyMap));

        return com.cxygzl.common.utils.HttpUtil.post(httpSetting.getUrl(), headerParamMap, bodyMap);

    }
}
