package com.cxygzl.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpUtil {


    private static TLogHutoolhttpInterceptor tLogHutoolhttpInterceptor = new TLogHutoolhttpInterceptor();
    /**
     * 请求超时时间
     */
    public static final int TIME_OUT=60000;


    public static String post(Object object, String url, String baseUrl) {


        return post(StrUtil.format("{}{}", baseUrl, url), null, object);

    }


    public static String post(String url, Map<String, String> headerParamMap, Object bodyMap) {
        if (headerParamMap == null) {
            headerParamMap = new HashMap<>();
        }
        String result = null;
        try {
            result = HttpRequest.post(url)
                    //头信息，多个头信息多次调用此方法即可
                    .header(Header.USER_AGENT, ProcessInstanceConstant.VariableKey.SYS_CODE)
                    .headerMap(headerParamMap, true)
                    .body(JSON.toJSONString(bodyMap))
                    //超时，毫秒
                    .timeout(TIME_OUT)
                    .addInterceptor(tLogHutoolhttpInterceptor)
                    .execute().body();
            log.info("  返回值:{}", result);
        } catch (Exception e) {
            log.error("前置检查事件异常", e);
        }
        return result;
    }


    public static String get(String url, String baseUrl) {

        return HttpRequest
                .get(StrUtil.format("{}{}", baseUrl, url))
                .timeout(TIME_OUT)
                .addInterceptor(tLogHutoolhttpInterceptor).execute().body();


    }


    /**
     * 流程扩展请求
     *
     * @param httpSetting
     * @param paramMap
     * @param flowId
     * @param processInstanceId
     * @param messageNotifyId
     * @return
     */
    public static String flowExtenstionHttpRequest(HttpSetting httpSetting, Map<String, Object> paramMap, String flowId,
                                                   String processInstanceId,String messageNotifyId) {
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
            bodyMap.put("messageNotifyId", messageNotifyId);
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
