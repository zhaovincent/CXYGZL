package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessNodeData;
import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;

/**
 * <p>
 * 流程节点数据 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
public interface IProcessNodeDataService extends IService<ProcessNodeData> {

    /**
     * 保存流程节点数据
     * @param processNodeDataDto
     * @return
     */
    R saveNodeData(ProcessNodeDataDto processNodeDataDto);

    /***
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    R<String> getNodeData(String flowId,String nodeId);

    R<Node> getNode(String flowId, String nodeId);

}
