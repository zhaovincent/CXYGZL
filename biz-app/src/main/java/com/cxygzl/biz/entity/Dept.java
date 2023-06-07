package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
@Getter
@Setter
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门名
     */
    private String name;

    /**
     * 上级部门id
     */
    private Long parentId;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField( fill = FieldFill.INSERT)
    private Boolean delFlag;

    /**
     * 主管user_id
     */
    private Long leaderUserId;

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
}
