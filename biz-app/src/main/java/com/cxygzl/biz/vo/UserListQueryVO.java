package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.PageDto;
import lombok.Data;

import java.util.List;

@Data
public class UserListQueryVO extends PageDto {
    /**
     * 用户状态 1在职 2离职
     */
    private Integer status;

    private String name;

    private List<Long> depIdList;

}
