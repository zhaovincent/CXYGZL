package com.cxygzl.core.node;

import com.cxygzl.common.dto.flow.Node;

/**
 * 用户为空策略处理器
 */
public interface EmptyUserStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        EmptyUserStrategyFactory.register(key, this);
    }

    /**
     * 抽象方法 处理表达式
     *
     * @param node
     * @param flowId
     * @param taskId
     */
    void handle(Node node,String flowId,String taskId);


}
