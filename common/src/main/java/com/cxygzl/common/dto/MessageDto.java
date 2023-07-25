package com.cxygzl.common.dto;

import lombok.*;

import java.io.Serializable;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto   implements Serializable {

    private static final long serialVersionUID = 1L;




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
    private String flowId;
    private String processInstanceId;
}
