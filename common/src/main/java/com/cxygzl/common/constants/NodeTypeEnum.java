package com.cxygzl.common.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * 节点枚举
 */
@Getter
public enum NodeTypeEnum {

    ROOT( "根节点",0,false),
    END( "结束节点",-1,false),
    MERGE_GATEWAY( "聚合网关",-2,false),

    APPROVAL( "审批节点",1,false),
    CC( "抄送节点",2,false),
    EXCLUSIVE_GATEWAY( "条件分支",4,true),
    INCLUSIVE_GATEWAY( "包容分支",8,true),
    PARALLEL_GATEWAY( "并行分支",5,true),
    EMPTY( "空",3,false),
    TRIGGER( "触发器",6,false),
    DELAY( "延时器",7,false),
    SUB_PROCESS( "子流程",9,false),
    ROUTE( "路由",10,false),
    ASYN_TRIGGER( "异步触发器",11,false),

    ;


    public static NodeTypeEnum getByValue(int value){
        return Arrays.stream(NodeTypeEnum.values()).filter(w->w.getValue()==value).findAny().orElse(null);
    }


    NodeTypeEnum(String name, Integer value, Boolean branch) {
        this.name = name;
        this.value = value;
        this.branch = branch;
    }

    private String name;
    private Integer value;
    private Boolean branch;


}
