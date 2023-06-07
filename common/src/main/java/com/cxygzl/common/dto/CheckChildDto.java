package com.cxygzl.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 检查是否是给定的子级
 *
 */
@Data
public class CheckChildDto {

    private Long childId;

    private List<Long> deptIdList;

}
