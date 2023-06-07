package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 流程节点记录-执行人
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Getter
@Setter
@TableName("process_node_record_assign_user")
public class ProcessNodeRecordAssignUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField( fill = FieldFill.INSERT)
    private Boolean delFlag;

    /**
     * 创建时间
     */
    @TableField( fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

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
     *  用户id
     */
    private String userId;

    /**
     * 节点状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
    private String executionId;
    private String taskId;
    private String approveDesc;
    private String nodeName;
    private String taskType;


}
