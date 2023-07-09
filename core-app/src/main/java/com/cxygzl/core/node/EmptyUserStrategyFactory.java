package com.cxygzl.core.node;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmptyUserStrategyFactory {

    private static final Map<String, EmptyUserStrategy> STRATEGY_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 提供获取策略的方法
     *
     * @param key
     * @return
     */
    public static EmptyUserStrategy getStrategy(String key) {
        EmptyUserStrategy sendService = STRATEGY_CONCURRENT_HASH_MAP.get(key);
        return sendService;
    }

    /**
     * 在Bean属性初始化后执行该方法
     *
     * @param key                  批次类型枚举
     * @param emptyUserStrategy 表达式处理接口
     */
    public static void register(String key, EmptyUserStrategy emptyUserStrategy) {
        STRATEGY_CONCURRENT_HASH_MAP.put(key, emptyUserStrategy);
    }



}
