package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户字段
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`user_field`")
public class UserField  extends BaseEntity {


    /**
     * 用户名
     */
    @TableField("`name`")
    private String name;


    /**
     * 字段类型
     */
    @TableField("`type`")
    private String type;

    /**
     * 是否必填
     */
    @TableField("`required`")
    private Boolean required;

    /**
     * 配置json字符串
     */
    @TableField("`configuration`")
    private String configuration;

    /**
     * 字段
     */
    @TableField("`key`")
    private String key;
}
