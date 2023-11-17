package com.cxygzl.core.node.impl;

import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.node.IDataStoreHandler;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 远程存储数据处理器
 */
@Component("remoteDateStore")
@Slf4j
@Lazy
public class RemoteDataStoreHandler implements IDataStoreHandler {

    /**
     * 节点数据存储
     *
     * @param flowId 流程id
     * @param nodeId 节点id
     * @param data   数据
     */
    @Override
    public void saveAll(String flowId, String nodeId, Object data) {
        ProcessNodeDataDto processNodeDataDto = new ProcessNodeDataDto();
        processNodeDataDto.setFlowId(flowId);
        processNodeDataDto.setNodeId(nodeId);
        processNodeDataDto.setData(JsonUtil.toJSONString(data));
        BizHttpUtil.saveNodeOriData(processNodeDataDto);
    }

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
        saveAll(flowId,nodeId,data);

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





        R<String> r = BizHttpUtil.queryNodeOriData(flowId, nodeId);


        log.debug("flowId={} nodeId={} data={}", flowId, nodeId, JsonUtil.toJSONString(r));


        String data = r.getData();


        return data;
    }
}
