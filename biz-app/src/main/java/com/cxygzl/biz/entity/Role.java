package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`role`")
public class Role  extends BaseEntity {


    /**
     * 角色名字
     */
    @TableField("`name`")
    private String name;

    /**
     * 创建人
     */
    @TableField("`user_id`")
    private Long userId;
    @TableField("`key`")
    private String key;
    @TableField("`desc`")
    private String desc;
    @TableField("`status`")
    private Integer status;
    @TableField("`sort`")
    private Integer sort;
    @TableField("`data_scope`")
    private Integer dataScope;
}
