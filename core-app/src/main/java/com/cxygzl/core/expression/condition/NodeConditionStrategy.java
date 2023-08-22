package com.cxygzl.core.expression.condition;

import com.cxygzl.common.dto.flow.Condition;

import java.util.Map;

/**
 * 节点单个条件处理器
 */
public interface NodeConditionStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        NodeExpressionResultStrategyFactory.register(key, this);
    }

    /**
     * 抽象方法 处理表达式
     *
     * @param condition
     */
    String handleExpression(Condition condition);

    /**
     * 处理数据
     * @param condition
     * @param paramMap
     * @return
     */
    boolean handleResult(Condition condition, Map<String,Object> paramMap);



}
