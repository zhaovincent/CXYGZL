package com.cxygzl.common.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * 操作类型
 */
@Getter
public enum OperTypeEnum {

    START( "开始","start"),

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
    BACK_JOIN_ADMIN( "管理员转交","backJoinByAdmin"),

    ;

    public static OperTypeEnum getByValue(String value){
        return Arrays.stream(OperTypeEnum.values()).filter(w->w.getValue().equals(value)).findAny().orElse(null);
    }


    OperTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private String value;


}
