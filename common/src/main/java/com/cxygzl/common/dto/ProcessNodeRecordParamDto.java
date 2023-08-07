package com.cxygzl.common.dto;

import lombok.Data;

/**
 * 流程节点记录
 */
@Data
public class ProcessNodeRecordParamDto {


    /**
     * 流程id
     */
    private String flowId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 表单数据
     */
    private String data;

    private String nodeId;
    private String parentNodeId;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点名字
     */
    private String nodeName;

    private String executionId;



}
