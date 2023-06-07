package com.cxygzl.core.node.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
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

    /**
     * 节点数据存储
     *
     * @param processId 流程id
     * @param nodeId    节点id
     * @param data      数据
     */
    @Override
    public void save(String processId, String nodeId, String data) {
        log.debug("processId={} nodeId={} data={}", processId, nodeId, data);
        ProcessNodeDataDto processNodeDataDto = new ProcessNodeDataDto();
        processNodeDataDto.setProcessId(processId);
        processNodeDataDto.setNodeId(nodeId);
        processNodeDataDto.setData(data);


        String post = CoreHttpUtil.saveNodeOriData(processNodeDataDto);
        log.debug("保存节点数据返回值：{}", post);
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


        String post = CoreHttpUtil.queryNodeOriData(processId, nodeId);


        log.debug("processId={} nodeId={} data={}", processId, nodeId, post);

        R<String> r = JSON.parseObject(post, new TypeReference<R<String>>() {
        });

        return r.getData();
    }
}
