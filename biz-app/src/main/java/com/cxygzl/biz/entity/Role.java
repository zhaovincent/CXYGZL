package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author Vincent
 * @since 2023-06-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class Role extends BaseEntity {




    /**
     * 角色名字
     */
    private String name;
    /**
     * 角色key
     */
    @TableField("`key`")
    private String key;
    /**
     * 角色描述
     */
    @TableField("`desc`")
    private String desc;

    private Long userId;
    private Integer status;
    private Integer sort;
    private Integer dataScope;
}
