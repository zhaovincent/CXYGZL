package com.cxygzl.biz.vo;

import com.cxygzl.biz.entity.Process;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProcessVO extends Process {
    /**
     * 需要发起人选择的节点id
     */
    private List<String> selectUserNodeId;

    private Map<String, Object> variableMap;
}
