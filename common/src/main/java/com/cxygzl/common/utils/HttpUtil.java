package com.cxygzl.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtil {


    private static TLogHutoolhttpInterceptor tLogHutoolhttpInterceptor = new TLogHutoolhttpInterceptor();


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
                    .header(Header.USER_AGENT, ProcessInstanceConstant.VariableKey.SYS_CODE)//头信息，多个头信息多次调用此方法即可
                    .headerMap(headerParamMap, true)
                    .body(JSON.toJSONString(bodyMap))
                    .timeout(60000)//超时，毫秒
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
                .timeout(60000)
                .addInterceptor(tLogHutoolhttpInterceptor).execute().body();


    }

}
