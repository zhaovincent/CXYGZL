package com.cxygzl.common.constants;

import lombok.Getter;

/**
 * 节点枚举
 */
@Getter
public enum NodeTypeEnum {

    ROOT("ROOT","根节点",0),

    APPROVAL("APPROVAL","审批节点",1),
    CC("CC","抄送节点",2),
    EXCLUSIVE_GATEWAY("CONDITIONS","条件分支",4),
    EMPTY("EMPTY","空",3),

    ;


    NodeTypeEnum(String key, String name, Integer value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    private String key;

    private String name;
    private Integer value;


}
