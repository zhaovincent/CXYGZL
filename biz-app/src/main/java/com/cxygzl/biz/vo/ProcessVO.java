package com.cxygzl.biz.vo;

import com.cxygzl.biz.entity.Process;
import com.cxygzl.common.dto.flow.NodeUser;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProcessVO extends Process {
    /**
     * 需要发起人选择的节点id
     */
    private List<String> selectUserNodeId;
    /**
     * 发起人范围
     */
    private List<NodeUser> rangeList;

    private Map<String, Object> variableMap;
}
