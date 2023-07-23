package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`menu`")
public class Menu  extends BaseEntity {

    /**
     * 父菜单ID
     */
    @TableField("`parent_id`")
    private Long parentId;

    /**
     * 父节点ID路径
     */
    @TableField("`tree_path`")
    private String treePath;

    /**
     * 菜单名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 菜单类型(1:菜单；2:目录；3:外链；4:按钮)
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    @TableField("`path`")
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    @TableField("`component`")
    private String component;

    /**
     * 权限标识
     */
    @TableField("`perm`")
    private String perm;

    /**
     * 显示状态(1-显示;0-隐藏)
     */
    @TableField("`visible`")
    private Boolean visible;

    /**
     * 排序
     */
    @TableField("`sort`")
    private Integer sort;

    /**
     * 菜单图标
     */
    @TableField("`icon`")
    private String icon;

    /**
     * 跳转路径
     */
    @TableField("`redirect`")
    private String redirect;

}
