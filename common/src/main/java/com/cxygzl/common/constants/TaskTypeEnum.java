package com.cxygzl.common.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * 任务类型
 */
@Getter
public enum TaskTypeEnum {

    PASS( "同意","pass"),
    RESOLVE( "同意","resolve"),
    REFUSE( "拒绝","refuse"),
    REJECT( "驳回","reject"),
    REVOKE( "撤回","revoke"),
    FRONT_JOIN( "委派","frontJoin"),
    BACK_JOIN( "转办","backJoin"),
    CANCEL( "取消","cancel"),
    ADD_ASSIGNEE( "加签","addAssignee"),
    DEL_ASSIGNEE( "减签","delAssignee"),
    ;

    public static TaskTypeEnum getByValue(String value){
        return Arrays.stream(TaskTypeEnum.values()).filter(w->w.getValue().equals(value)).findAny().orElse(null);
    }


    TaskTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private String value;


}
