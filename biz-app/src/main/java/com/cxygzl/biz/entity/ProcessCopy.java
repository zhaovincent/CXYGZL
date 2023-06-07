package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程抄送数据
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("process_copy")
public class ProcessCopy {

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
     *  流程发起时间
     */
    private Date startTime;

    /**
     * 当前节点时间
     */
    private Date nodeTime;

    /**
     * 发起人
     */
    private Long startUserId;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 实例id
     */
    private String processInstanceId;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 节点 名称
     */
    private String nodeName;

    /**
     * 表单数据
     */
    private String formData;

    /**
     * 抄送人id
     */
    private Long userId;
}
