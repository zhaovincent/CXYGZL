package com.cxygzl.biz.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author Vincent
 * @since 2023-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
public class Menu extends BaseEntity {


    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 父节点ID路径
     */
    private String treePath;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单类型(1:菜单；2:目录；3:外链；4:按钮)
     */
    private Integer type;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    private String component;

    /**
     * 权限标识
     */
    private String perm;

    /**
     * 显示状态(1-显示;0-隐藏)
     */
    private Boolean visible;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 跳转路径
     */
    private String redirect;


}
