package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程节点记录-执行人-审批意见
 * </p>
 *
 * @author Vincent
 * @since 2023-08-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_node_record_approve_desc`")
public class ProcessNodeRecordApproveDesc extends BaseEntity {


    /**
     * 流程id
     */
    @TableField("`flow_id`")
    private String flowId;

    /**
     * 流程实例id
     */
    @TableField("`process_instance_id`")
    private String processInstanceId;
    @TableField("`node_id`")
    private String nodeId;

    /**
     *  用户id
     */
    @TableField("`user_id`")
    private String userId;

    /**
     * 执行id
     */
    @TableField("`execution_id`")
    private String executionId;

    /**
     *  任务id
     */
    @TableField("`task_id`")
    private String taskId;

    /**
     * 审批意见
     */
    @TableField("`approve_desc`")
    private String approveDesc;

    /**
     *  节点名称
     */
    @TableField("`node_name`")
    private String nodeName;
    /**
     *  评论时间
     */
    @TableField("`desc_date`")
    private Date descDate;
    /**
     *  消息id
     */
    @TableField("`desc_id`")
    private String descId;
    /**
     *  消息类型
     */
    @TableField("`desc_type`")
    private String descType;
}
