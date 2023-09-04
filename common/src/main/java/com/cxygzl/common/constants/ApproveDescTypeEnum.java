package com.cxygzl.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批类型枚举
 */
@Getter
@AllArgsConstructor
public enum ApproveDescTypeEnum {


    PASS("passDesc","完成"),
    RESOLVE("resolveDesc","完成"),
    REFUSE("refuseDesc","拒绝"),
    BACK_JOIN("backJoinDesc","转办"),
    ADD_ASSIGNEE("addAssigneeDesc","加签"),
    DEL_ASSIGNEE("delAssigneeDesc","减签"),
    FRONT_JOIN("frontJoinDesc","委派"),
    REJECT("rejectDesc","驳回"),

    ;

    public static List<String> getTypeList(){
        return Arrays.stream(ApproveDescTypeEnum.values()).map(w->w.getType()).collect(Collectors.toList());
    }

    public static ApproveDescTypeEnum get(String type){
        return Arrays.stream(ApproveDescTypeEnum.values()).filter(w->w.getType().equals(type)).findFirst().get();

    }



    private String type;

    private String name;


}
