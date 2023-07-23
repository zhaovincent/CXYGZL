package com.cxygzl.core.node;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AssignUserStrategyFactory {

    private static final Map<Integer, AssignUserStrategy> STRATEGY_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 提供获取策略的方法
     *
     * @param key
     * @return
     */
    public static AssignUserStrategy getStrategy(Integer key) {
        AssignUserStrategy sendService = STRATEGY_CONCURRENT_HASH_MAP.get(key);
        return sendService;
    }

    /**
     * 在Bean属性初始化后执行该方法
     *
     * @param key                  批次类型枚举
     * @param assignUserStrategy 表达式处理接口
     */
    public static void register(Integer key, AssignUserStrategy assignUserStrategy) {
        STRATEGY_CONCURRENT_HASH_MAP.put(key, assignUserStrategy);
    }



}
