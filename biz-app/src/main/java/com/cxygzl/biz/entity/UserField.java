package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户字段
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-17
 */
@Getter
@Setter
@TableName("user_field")
public class UserField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String name;


    /**
     * 字段
     */
    @TableField("`key`")
    private String key;


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
     * 字段类型
     */
    private String type;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 配置json字符串
     */
    private String configuration;
}
