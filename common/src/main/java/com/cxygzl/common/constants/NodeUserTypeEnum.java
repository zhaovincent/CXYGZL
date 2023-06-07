package com.cxygzl.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户节点类型枚举
 */
@Getter
@AllArgsConstructor
public enum NodeUserTypeEnum {

    USER("user","用户"),
    DEPT("dept","部门"),
    ;

    private String key;

    private String name;


}
