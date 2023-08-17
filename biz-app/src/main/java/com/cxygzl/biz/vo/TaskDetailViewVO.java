package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.flow.FormItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-17 14:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailViewVO {

    private String processInstanceId;
    private String node;
    private String nodeId;
    /**
     *
     */
    private String starterAvatarUrl;
    /**
     * 发起人名字
     */
    private String starterName;
    /**
     * 发起时间
     */
    private Date startTime;
    /**
     * 任务是否存在
     */
    private Boolean taskExist;
    private String processName;
    private String nodeName;
    private String flowId;
    private String process;
    /**
     * 是否再次委派
     */
    private Boolean delegateAgain;
    /**
     * 是否是委派任务
     */
    private Boolean delegationTask;
    /**
     * 是否是子流程发起任务
     */
    private Boolean subProcessStarterTask;
    private List<String> selectUserNodeId;
    private List<FormItemVO> formItems;
    /**
     * 流程实例状态
     */
    private Integer processInstanceStatus;
    /**
     * 流程实例结果
     */
    private Integer processInstanceResult;
}
