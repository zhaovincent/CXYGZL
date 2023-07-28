package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通知消息
 * </p>
 *
 * @author Vincent
 * @since 2023-07-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`message`")
public class Message extends BaseEntity {


    /**
     * 类型
     */
    @TableField("`type`")
    private String type;

    /**
     * 是否已读
     */
    @TableField("`readed`")
    private Boolean readed;

    /**
     * 用户id
     */
    @TableField("`user_id`")
    private String userId;

    /**
     * 唯一id
     */
    @TableField("`unique_id`")
    private String uniqueId;

    /**
     * 消息参数
     */
    @TableField("`param`")
    private String param;

    /**
     * 消息内容
     */
    @TableField("`content`")
    private String content;

    /**
     * 消息头
     */
    @TableField("`title`")
    private String title;

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
}
