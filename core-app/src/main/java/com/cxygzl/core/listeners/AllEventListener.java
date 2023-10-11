package com.cxygzl.core.listeners;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.cfg.TransactionState;

@Slf4j
public class AllEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {
        log.debug("监听器类型：{} {}",event.getType(),event.getClass().getCanonicalName());
        EventListenerStrategy strategy = EventListenerStrategyFactory.getStrategy(event.getType().name());
        if (strategy != null) {
            strategy.handle(event);
        }
    }


    /**
     * @return whether or not the current operation should fail when this listeners execution throws an exception.
     */
    @Override
    public boolean isFailOnException() {
        //监听器异常 是否失败
        return false;
    }

    /**
     * @return Returns whether this event listener fires immediately when the event occurs or
     * on a transaction lifecycle event (before/after commit or rollback).
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        //true 事务提交之后再触发
        //false 在同一个事务里
        return false;
    }

    /**
     * @return if non-null, indicates the point in the lifecycle of the current transaction when the event should be fired.
     */
    @Override
    public String getOnTransaction() {
        return TransactionState.COMMITTED.name();
    }
}
