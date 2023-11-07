package com.cxygzl.common.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * 审批结果
 */
@Getter
public enum ApproveResultEnum {

    PASS( "同意",1),
    REFUSE( "拒绝",2),
    CANCEL( "撤销",3),

    ;

    public static ApproveResultEnum getByValue(int value){
        return Arrays.stream(ApproveResultEnum.values()).filter(w->w.getValue()==(value)).findAny().orElse(null);
    }


    ApproveResultEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;


}
