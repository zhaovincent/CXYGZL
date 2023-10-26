package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程抄送数据--用户和实例唯一值
 * </p>
 *
 * @author Vincent
 * @since 2023-10-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_instance_user_copy`")
public class ProcessInstanceUserCopy extends BaseEntity {



    /**
     * 发起人
     */
    @TableField("`start_user_id`")
    private String startUserId;

    /**
     * 流程id
     */
    @TableField("`flow_id`")
    private String flowId;

    /**
     * 实例id
     */
    @TableField("`process_instance_id`")
    private String processInstanceId;

    /**
     * 分组id
     */
    @TableField("`group_id`")
    private Long groupId;

    /**
     * 分组名称
     */
    @TableField("`group_name`")
    private String groupName;

    /**
     * 流程名称
     */
    @TableField("`process_name`")
    private String processName;



    /**
     * 抄送人id
     */
    @TableField("`user_id`")
    private String userId;
}
