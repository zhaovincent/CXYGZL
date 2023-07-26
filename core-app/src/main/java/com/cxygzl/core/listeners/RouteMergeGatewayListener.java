package com.cxygzl.core.listeners;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 路由聚合网关
 */
@Slf4j
public class RouteMergeGatewayListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        log.info("-------开始执行网关");
    }
}
