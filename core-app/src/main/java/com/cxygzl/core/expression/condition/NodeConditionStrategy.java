package com.cxygzl.core.expression.condition;

import com.cxygzl.common.dto.flow.Condition;
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
        NodeExpressionStrategyFactory.register(key, this);
    }

    /**
     * 抽象方法 处理表达式
     *
     * @param nodeConditionDto
     */
    String handle(Condition condition);


}
