package com.cxygzl.core.node;

import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.CommonUtil;

/**
 * 节点数据处理接口
 */
public interface INodeDataStoreHandler {
    /**
     * 节点数据存储
     * @param flowId 流程id
     * @param nodeId 节点id
     * @param data 数据
     */
    void save(String flowId,String nodeId,Node data);
    /**
     * 获取节点数据
     * @param flowId 流程id
     * @param nodeId 节点id
     * @return
     */
    @Deprecated
    String get(String flowId,String nodeId);




    default Node getNode(String flowId, String nodeId){
        String text = this.get(flowId, nodeId);
        return CommonUtil.toObj(text,Node.class);
    }



}
