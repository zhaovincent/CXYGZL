package com.cxygzl.common.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 任务完成参数对象
 *
 */
@Data
public class TaskParamDto {

    /**
     * 实例id
     */
    private String processInstanceId;
    private Boolean approveResult;
    private String approveDesc;
    private List<String> processInstanceIdList;

    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 添加子流程发起人
     */
    private Boolean appendChildProcessRootId;

    /**
     * 任务id
     */
    private String taskId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 模板用户id
     */
    private String targetUserId;
    private List<String> targetUserIdList;
    private List<String> targetExecutionIdList;
    private List<String> targetUserNameList;
    private String targetUserName;
    /**
     * 参数
     */
    private Map<String,Object> paramMap;
    /**
     * 任务本地变量
     */
    private Map<String,Object> taskLocalParamMap;


    /**
     * 目标节点
     */
    private String targetNodeId;

    private List<String> nodeIdList;
    private List<String> taskIdList;
    /**
     * 执行id
     */
    private String executionId;

}
