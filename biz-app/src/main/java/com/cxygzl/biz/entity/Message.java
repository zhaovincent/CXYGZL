package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 通知消息
 * </p>
 *
 * @author cxygzl
 * @since 2023-07-25
 */
@Getter
@Setter
public class Message extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String flowId;
    private String processInstanceId;

    /**
     * 类型
     */
    private String type;

    /**
     * 是否已读
     */
    private Boolean readed;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 唯一id
     */
    private String uniqueId;

    /**
     * 消息参数
     */
    private String param;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息头
     */
    private String title;
}
