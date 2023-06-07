package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程节点数据
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("process_node_data")
public class ProcessNodeData {

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
     * 表单数据
     */
    private String data;
    private String nodeId;
}
