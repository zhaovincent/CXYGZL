package com.cxygzl.core.listeners;


import org.flowable.common.engine.api.delegate.event.FlowableEvent;


/**
 * 节点单个条件处理器
 */
public interface EventListenerStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        EventListenerStrategyFactory.register(key, this);
    }


    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    void handle(FlowableEvent event);

}
