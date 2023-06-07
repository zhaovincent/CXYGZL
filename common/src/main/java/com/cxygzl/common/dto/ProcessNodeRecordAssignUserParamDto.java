package com.cxygzl.common.dto;

import lombok.Data;

/**
 * 流程节点记录-执行人
 */
@Data
public class ProcessNodeRecordAssignUserParamDto {


    /**
     * 流程id
     */
    private String processId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 表单数据
     */
    private String data;
    private String localData;

    private String nodeId;

    /**
     * 用户id
     */
    private Long userId;
    private String executionId;
    private String taskId;
    private String approveDesc;
    private String nodeName;
    private String taskType;



}
