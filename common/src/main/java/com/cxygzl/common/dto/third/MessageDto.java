package com.cxygzl.common.dto.third;

import com.cxygzl.common.constants.MessageTypeEnum;
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


    /**
     * 类型
     * {@link MessageTypeEnum}
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
    /**
     * 流程id
     */
    private String flowId;
    /**
     *  流程实例id
     */
    private String processInstanceId;
}
