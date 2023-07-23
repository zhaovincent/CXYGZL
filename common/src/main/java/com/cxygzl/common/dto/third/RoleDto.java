package com.cxygzl.common.dto.third;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {

    /**
     * 角色名字
     */
    private String name;
    private String id;

    private String key;

    private Integer status;


}
