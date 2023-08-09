package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程节点记录
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_node_record`")
public class ProcessNodeRecord  extends BaseEntity {


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
     * 跳转标识
     */
    @TableField("`jump_label`")
    private String jumpLabel;

    /**
     * 节点类型
     */
    @TableField("`node_type`")
    private Integer nodeType;

    /**
     * 节点名字
     */
    @TableField("`node_name`")
    private String nodeName;

    /**
     * 节点状态
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
     * 上一层级id
     */
    @TableField("`parent_node_id`")
    private String parentNodeId;


}
