package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户任务限时
 */
@NoArgsConstructor
@Data
public class NodeTimeoutDto {
    /**
     * 超时时间单位
     */
    private String unit;
    /**
     * 超时时间
     */
    private Integer value;

}
