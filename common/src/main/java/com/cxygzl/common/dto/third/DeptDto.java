package com.cxygzl.common.dto.third;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeptDto {
    /**
     * 部门id 不能为空
     */
    private String id;
    /**
     * 部门名字 不能为空
     */
    private String name;
    /**
     * 部门上级id 不能为空 若为顶级 则是0
     */
    private String parentId;
    /**
     * 部门主管的userId 可以为空
     */
    private List<String> leaderUserIdList;
    /**
     * 部门状态 0 禁用 1启用
     */
    private Integer status;


}
