package com.cxygzl.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节点枚举
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {

    ROOT("ROOT","根节点"),
    END("END","结束节点"),
    APPROVAL("APPROVAL","审批节点"),
    DELAY("DELAY","延迟节点"),
    CC("CC","抄送节点"),
    TRIGGER("TRIGGER","触发器节点"),
    INCLUSIVES("INCLUSIVES","包容分支"),
    CONCURRENTS("CONCURRENTS","并行分支"),
    CONDITIONS("CONDITIONS","条件分支"),
    EMPTY("EMPTY","空"),

    ;

    private String key;

    private String name;


}
