package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_instance_record`")
public class ProcessInstanceRecord  extends BaseEntity {



    /**
     * 流程名字
     */
    @TableField("`name`")
    private String name;

    /**
     * 头像
     */
    @TableField("`logo`")
    private String logo;

    /**
     * 用户id
     */
    @TableField("`user_id`")
    private Long userId;


    /**
     * 流程id
     */
    @TableField("`flow_id`")
    private String flowId;

    /**
     * 流程实例id
     */
    @TableField("`process_instance_id`")
    private String processInstanceId;

    /**
     * 表单数据
     */
    @TableField("`form_data`")
    private String formData;

    /**
     * 组id
     */
    @TableField("`group_id`")
    private Long groupId;

    /**
     * 组名称
     */
    @TableField("`group_name`")
    private String groupName;

    /**
     * 状态
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 结束时间
     */
    @TableField("`end_time`")
    private Date endTime;

    /**
     * 上级流程实例id
     */
    @TableField("`parent_process_instance_id`")
    private String parentProcessInstanceId;
}
