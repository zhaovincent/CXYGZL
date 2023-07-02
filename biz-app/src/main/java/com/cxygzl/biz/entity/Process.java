package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Process implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 表单ID
     */
    private String flowId;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 图标配置
     */
    private String logo;

    /**
     * 设置项
     */
    private String settings;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 表单设置内容
     */
    private String formItems;

    /**
     * 流程设置内容
     */
    private String process;

    /**
     * 备注
     */
    private String remark;

    private Integer sort;

    /**
     * 0 正常 1=停用 2=已删除
     */
    private Boolean isStop;
    /**
     * 是否隐藏
     */
    private Boolean isHidden;

    /**
     * 流程管理员
     */
    private Long adminId;
    /**
     * 唯一id
     */
    private String uniqueId;
    private String admin;
    /**
     * 范围显示
     */
    private String rangeShow;
}
