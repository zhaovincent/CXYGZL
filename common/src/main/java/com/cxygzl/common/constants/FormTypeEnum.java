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
    AREA("Area","地区",""),
    NUMBER("Number","数字",null),
    DATE("Date","日期",null),
    DATE_TIME("DateTime","日期时间",null),
    LAYOUT("Layout","明细",null),
    TIME("Time","时间",null),
    MONEY("Money","金额",null),
    SCORE("Score","金额",null),
    SINGLE_SELECT("SingleSelect","单选",""),
    MULTI_SELECT("MultiSelect","多选",new ArrayList<>()),
    SELECT_DEPT("SelectDept","单部门",new ArrayList<>()),
    SELECT_USER("SelectUser","单用户",new ArrayList<>()),
    SELECT_MULTI_DEPT("SelectMultiDept","多部门",new ArrayList<>()),
    SELECT_MULTI_USER("SelectMultiUser","多用户",new ArrayList<>()),


    ;





    private String type;

    private String name;
    private Object defaultValue;


}
