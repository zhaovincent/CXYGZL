package com.cxygzl.common.dto;

import cn.hutool.core.lang.Dict;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务对象
 */
@Data
public class TaskDto {
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 参数集合
     */
    private Map<String,Object> paramMap;
    /**
     *  表单值显示
     */
    private List<Dict> formValueShowList;

    /**
     * 实例id
     */
    private String processInstanceId;
    /**
     * 执行id
     */
    private String executionId;
    /**
     * 耗时
     */
    private Long durationInMillis;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 执行人
     */
    private String assign;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 任务创建时间
     */
    private Date taskCreateTime;
    private Date taskEndTime;
    /**
     * 流程组名字
     */
    private String groupName;
    /**
     * 发起人id
     */
    private String rootUserId;
    /**
     * 发起人名字
     */
    private String rootUserName;
    /**
     * 发起人头像
     */
    private String rootUserAvatarUrl;
    /**
     * 发起时间
     */
    private Date startTime;
    /**
     * 流程实例业务编码
     */
    private String processInstanceBizCode;
    /**
     * 流程名称
     */
    private String processName;
    /**
     * 是否是子流程发起人任务
     */
    private Boolean subProcessStarterTask;
    /**
     * 需要发起人选择的节点id
     */
    private List<String> selectUserNodeId;

    private Integer processInstanceResult;
    private String userName;


}
