package com.cxygzl.biz.api;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ApiStrategyFactory {

    private static final Map<String, ApiStrategy> STRATEGY_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 提供获取策略的方法
     *
     * @param key
     * @return
     */
    public static ApiStrategy getStrategy() {
        Environment environment = SpringUtil.getBean(Environment.class);
        String property = environment.getProperty("api.ori", "local");
        ApiStrategy sendService = STRATEGY_CONCURRENT_HASH_MAP.get(property);
        return sendService;
    }

    /**
     * 在Bean属性初始化后执行该方法
     *
     * @param key                  批次类型枚举
     * @param nodeConditionHandler 表达式处理接口
     */
    public static void register(String key, ApiStrategy nodeConditionHandler) {
        STRATEGY_CONCURRENT_HASH_MAP.put(key, nodeConditionHandler);
    }




}
