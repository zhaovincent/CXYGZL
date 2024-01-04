package com.cxygzl.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    TODO_TASK("TodoTask","待办任务"),
    EXPIRED_TASK("ExpiredTask","超时提醒任务"),

    ;





    private String type;

    private String name;


}
