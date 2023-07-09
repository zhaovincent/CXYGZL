package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process`")
public class Process  extends BaseEntity {



    /**
     * 表单ID
     */
    @TableField("`flow_id`")
    private String flowId;

    /**
     * 表单名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 图标配置
     */
    @TableField("`logo`")
    private String logo;

    /**
     * 设置项
     */
    @TableField("`settings`")
    private String settings;

    /**
     * 分组ID
     */
    @TableField("`group_id`")
    private Long groupId;

    /**
     * 表单设置内容
     */
    @TableField("`form_items`")
    private String formItems;

    /**
     * 流程设置内容
     */
    @TableField("`process`")
    private String process;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;
    @TableField("`sort`")
    private Integer sort;

    /**
     * 0 正常 1=隐藏
     */
    @TableField("`is_hidden`")
    private Boolean hidden;

    /**
     * 0 正常 1=停用
     */
    @TableField("`is_stop`")
    private Boolean stop;

    /**
     * 流程管理员
     */
    @TableField("`admin_id`")
    private Long adminId;

    /**
     * 唯一性id
     */
    @TableField("`unique_id`")
    private String uniqueId;

    /**
     * 管理员
     */
    @TableField("`admin`")
    private String admin;

    /**
     * 范围描述显示
     */
    @TableField("`range_show`")
    private String rangeShow;
}
