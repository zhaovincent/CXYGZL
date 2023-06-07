package com.cxygzl.common.dto;

import lombok.Data;

@Data
public class PageDto {
    /**
     * 页码
     */
    private Integer page;
    /**
     * 每页的数量
     */
    private Integer count;

}
