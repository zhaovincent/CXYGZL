package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程操作记录
 * </p>
 *
 * @author Vincent
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_oper_record`")
public class ProcessOperRecord  extends BaseEntity {


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

    /**
     * 表单数据
     */
    @TableField("`data`")
    private String data;
    @TableField("`node_id`")
    private String nodeId;

    /**
     * 任务id
     */
    @TableField("`task_id`")
    private String taskId;

    /**
     * 用户id
     */
    @TableField("`user_id`")
    private String userId;

    /**
     * 节点名字
     */
    @TableField("`node_name`")
    private String nodeName;

    /**
     * 任务状态
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 开始时间
     */
    @TableField("`start_time`")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("`end_time`")
    private Date endTime;

    /**
     * 执行id
     */
    @TableField("`execution_id`")
    private String executionId;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;
}
