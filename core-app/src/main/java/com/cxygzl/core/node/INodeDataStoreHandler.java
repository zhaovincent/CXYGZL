package com.cxygzl.core.node;

import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.process.NodeDto;

/**
 * 节点数据处理接口
 */
public interface INodeDataStoreHandler {
    /**
     * 节点数据存储
     * @param processId 流程id
     * @param nodeId 节点id
     * @param data 数据
     */
    void save(String processId,String nodeId,String data);

    /**
     * 获取节点数据
     * @param processId 流程id
     * @param nodeId 节点id
     * @return
     */
    String get(String processId,String nodeId);

    default NodeDto getNodeDto(String processId, String nodeId){
        return JSON.parseObject(this.get(processId, nodeId),NodeDto.class);
    }



}
