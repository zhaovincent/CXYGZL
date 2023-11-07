package com.cxygzl.common.dto;

import lombok.Data;

@Data
public class ProcessDto {

    /**
     * 唯一性id
     */
    private String uniqueId;
    /**
     * 设置项
     */
    private String settings;
}
