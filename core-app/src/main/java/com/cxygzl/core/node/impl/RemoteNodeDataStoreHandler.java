package com.cxygzl.core.node.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.core.node.INodeDataStoreHandler;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 远程存储数据处理器
 */
@Component("remoteNodeDataStore")
@Slf4j
@Lazy
public class RemoteNodeDataStoreHandler implements INodeDataStoreHandler {

    private LRUCache<String, String> cache = CacheUtil.newLRUCache(1000);


    /**
     * 节点数据存储
     *
     * @param flowId 流程id
     * @param nodeId 节点id
     * @param data   数据
     */
    @Override
    public void save(String flowId, String nodeId, Node data) {
        log.debug("flowId={} nodeId={} data={}", flowId, nodeId, data);
        ProcessNodeDataDto processNodeDataDto = new ProcessNodeDataDto();
        processNodeDataDto.setFlowId(flowId);
        processNodeDataDto.setNodeId(nodeId);
        processNodeDataDto.setData(CommonUtil.toJson(data));


        CoreHttpUtil.saveNodeOriData(processNodeDataDto);

    }

    /**
     * 获取节点数据
     *
     * @param flowId 流程id
     * @param nodeId 节点id
     * @return
     */
    @Override
    public String get(String flowId, String nodeId) {

        String o = cache.get(StrUtil.format("{}||{}", flowId, nodeId));
        if (StrUtil.isNotBlank(o)) {
            log.debug("从缓存获取到数据 :{}  {} {}", flowId, nodeId, o);
            return o;
        }


        R<String> r = CoreHttpUtil.queryNodeOriData(flowId, nodeId);


        log.debug("flowId={} nodeId={} data={}", flowId, nodeId, JSON.toJSONString(r));


        String data = r.getData();

        cache.put(StrUtil.format("{}||{}", flowId, nodeId), data);

        return data;
    }
}
