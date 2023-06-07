package com.cxygzl.core.node.impl;

import com.cxygzl.core.node.INodeDataStoreHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * redis存储数据处理器
 */
@Component
@Slf4j
@Lazy
public class RedisNodeDataStoreHandler implements INodeDataStoreHandler {
    /**
     * 节点数据存储
     *
     * @param processId 流程id
     * @param nodeId    节点id
     * @param data      数据
     */
    @Override
    public void save(String processId, String nodeId, String data) {

    }

    /**
     * 获取节点数据
     *
     * @param processId 流程id
     * @param nodeId    节点id
     * @return
     */
    @Override
    public String get(String processId, String nodeId) {
        return null;
    }
}
