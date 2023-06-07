package com.cxygzl.common.dto;

import com.cxygzl.common.dto.process.NodeDto;
import lombok.Data;

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
    /**
     * 当前任务节点对象
     */
    private NodeDto taskNodeDto;
    /**
     * 实例id
     */
    private String processInstanceId;
    /**
     * 委派状态
     */
    private String delegationState;
    /**
     * 是否允许继续委派
     */
    private Boolean delegate;

}
