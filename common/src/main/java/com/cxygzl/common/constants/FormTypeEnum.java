package com.cxygzl.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/**
 * 表单类型枚举
 */
@Getter
@AllArgsConstructor
public enum FormTypeEnum {

    INPUT("Input","单行文本",""),
    TEXTAREA("Textarea","多行文本",""),
    NUMBER("Number","数字",null),
    MONEY("Money","金额",null),
    SINGLE_SELECT("SingleSelect","单选",new ArrayList<>()),
    SELECT_DEPT("SelectDept","部门",new ArrayList<>()),
    SELECT_USER("SelectUser","用户",new ArrayList<>()),


    ;





    private String type;

    private String name;
    private Object defaultValue;


}
