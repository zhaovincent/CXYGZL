package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户任务超时之后的操作
 */
@NoArgsConstructor
@Data
public class NodeTimeLimitHandlerDto {
    /**
     * 操作类型
     */
    private String type;
    /**
     * 超时时间
     */
    private Integer value;
    /**
     * 提醒
     */
    private NotifyDto notify;


    /**
     * 提醒对象
     */
    @Data
    public static class NotifyDto{
        /**
         * 是否一次
         */
        private Boolean once;
        /**
         * 循环 几个小时一次
         */
        private Integer hour;

    }

}
