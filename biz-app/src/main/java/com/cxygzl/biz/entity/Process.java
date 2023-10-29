package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cxygzl.biz.constants.ValidGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotBlank(groups = {ValidGroup.Crud.Create.class},message = "表单名称不能为空")
    @TableField("`name`")
    private String name;

    /**
     * 图标配置
     */
    @NotBlank(groups = {ValidGroup.Crud.Create.class},message = "表单头像不能为空")
    @TableField("`logo`")
    private String logo;

    /**
     * 设置项
     */
    @NotBlank(groups = {ValidGroup.Crud.Create.class},message = "设置不能为空")
    @TableField("`settings`")
    private String settings;

    /**
     * 分组ID
     */
    @NotNull(groups = {ValidGroup.Crud.Create.class},message = "请设置分组")
    @TableField("`group_id`")
    private Long groupId;

    /**
     * 表单设置内容
     */
    @NotBlank(groups = {ValidGroup.Crud.Create.class},message = "请设置表单")
    @TableField("`form_items`")
    private String formItems;

    /**
     * 流程设置内容
     */
    @NotBlank(groups = {ValidGroup.Crud.Create.class},message = "请设置流程")
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
    private String adminId;

    /**
     * 唯一性id
     */
    @TableField("`unique_id`")
    private String uniqueId;

    /**
     * 管理员
     */
    @NotBlank(groups = {ValidGroup.Crud.Create.class},message = "管理员不能为空")
    @TableField("`admin`")
    private String admin;

    /**
     * 范围描述显示
     */
    @TableField("`range_show`")
    private String rangeShow;
}
