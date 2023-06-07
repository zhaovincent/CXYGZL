package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程记录
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("process_instance_record")
public class ProcessInstanceRecord {

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程名字
     */
    private String name;

    /**
     * 头像
     */
    private String logo;

    /**
     * 用户id
     */
    private Long userId;

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
     * 流程id
     */
    private String processId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 表单数据
     */
    private String formData;
    /**
     * 组id
     */
    private Long groupId;
    /**
     * 组名字
     */
    private String groupName;
    /**
     * 状态
     */
    @TableField("`status`")
    private  Integer status;
    /**
     * 结束时间
     */
    private Date endTime;
    private String parentProcessInstanceId;

}
