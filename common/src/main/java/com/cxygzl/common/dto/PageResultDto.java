package com.cxygzl.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResultDto<T> {

    private Long total;


    private List<T> records;
}
