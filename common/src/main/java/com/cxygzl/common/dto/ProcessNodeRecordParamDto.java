package com.cxygzl.common.dto;

import lombok.Data;

import java.util.List;

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
    /**
     * 跳转标识
     */
    private String flowUniqueId;
    private String nodeId;


    private String parentNodeId;

    /**
     * 节点类型
     */
    private Integer nodeType;

    /**
     * 节点名字
     */
    private String nodeName;

    private String executionId;
    private List<String> childExecutionId;



}
