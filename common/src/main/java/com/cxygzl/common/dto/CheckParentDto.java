package com.cxygzl.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 检查是否是给定的父级
 *
 */
@Data
public class CheckParentDto {

    private String parentId;

    private List<String> deptIdList;

}
