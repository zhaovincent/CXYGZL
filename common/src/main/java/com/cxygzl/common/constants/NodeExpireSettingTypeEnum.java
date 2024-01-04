package com.cxygzl.common.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * 节点的过期设置类型枚举
 */
@Getter
public enum NodeExpireSettingTypeEnum {

    NOTIFY( "通知提醒",1),
    PASS( "自动通过",2),
    REFUSE( "自动拒绝",3),

    ;

    public static NodeExpireSettingTypeEnum getByValue(int value){
        return Arrays.stream(NodeExpireSettingTypeEnum.values()).filter(w->w.getValue()==(value)).findAny().orElse(null);
    }


    NodeExpireSettingTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;


}
