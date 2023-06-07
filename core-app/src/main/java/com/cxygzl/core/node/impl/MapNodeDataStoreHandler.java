package com.cxygzl.core.node.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.core.node.INodeDataStoreHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存存储数据处理器
 */
@Component("mapNodeDataStore")
@Slf4j
@Lazy
public class MapNodeDataStoreHandler implements INodeDataStoreHandler {

    private Map<String, String> map = new ConcurrentHashMap<>();

    /**
     * 节点数据存储
     *
     * @param processId 流程id
     * @param nodeId    节点id
     * @param data      数据
     */
    @Override
    public void save(String processId, String nodeId, String data) {
        log.debug("processId={} nodeId={} data={}",processId,nodeId,data);
        map.put(StrUtil.format("{}-{}", processId, nodeId), data);
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
        String s = map.get(StrUtil.format("{}-{}", processId, nodeId));
        log.debug("processId={} nodeId={} data={}",processId,nodeId,s);


        return s;
    }
}
