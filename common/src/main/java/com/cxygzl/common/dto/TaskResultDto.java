package com.cxygzl.common.dto;

import lombok.Data;

import java.util.Map;

/**
 * 任务结果对象
 *
 */
@Data
public class TaskResultDto {

    private Boolean currentTask;

    /**
     * 流程id
     */
    private String flowId;
    private String userId;



    private String nodeId;
    private String executionId;
    /**
     * 实例id
     */
    private String processInstanceId;
    /**
     * 委派状态
     */
    private String delegationState;
    /**
     * 是否是委派的任务
     */
    private Boolean frontJoinTask;
    /**
     * 是否允许继续委派
     */
    private Boolean delegate;
    /**
     * 所有变量
     */
    private Map<String, Object> variableAll;

    private String flowUniqueId;
}
