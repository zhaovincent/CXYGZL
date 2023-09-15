package com.cxygzl.common.constants;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 表单类型枚举
 */
@Getter
@AllArgsConstructor
public enum FormTypeEnum {

    INPUT("Input","单行文本","","longtext"),
    TEXTAREA("Textarea","多行文本","","longtext"),
    DESCRIPTION("Description","说明","","longtext"),
    AREA("Area","地区",new HashMap<>(),"longtext"),
    NUMBER("Number","数字",null,"decimal(20,10)"),
    DATE("Date","日期",null,"datetime"),
    DATE_TIME("DateTime","日期时间",null,"datetime"),
    LAYOUT("Layout","明细",new ArrayList<>(),"longtext"),
    TIME("Time","时间",null,"datetime"),
    MONEY("Money","金额",null,"decimal(20,5)"),
    SCORE("Score","评分",null,"decimal(5,2)"),
    UPLOAD_FILE("UploadFile","文件",new ArrayList<>(),"longtext"),
    UPLOAD_IMAGE("UploadImage","图片",new ArrayList<>(),"longtext"),
    SINGLE_SELECT("SingleSelect","单选",new ArrayList<>(),"longtext"),
    MULTI_SELECT("MultiSelect","多选",new ArrayList<>(),"longtext"),
    SELECT_DEPT("SelectDept","单部门",new ArrayList<>(),"longtext"),
    SELECT_USER("SelectUser","单用户",new ArrayList<>(),"longtext"),
    SELECT_MULTI_DEPT("SelectMultiDept","多部门",new ArrayList<>(),"longtext"),
    SELECT_MULTI_USER("SelectMultiUser","多用户",new ArrayList<>(),"longtext"),


    ;





    private String type;

    private String name;
    private Object defaultValue;

    private String sqlDDL;


    public static FormTypeEnum getByType(String type){
        return Arrays.stream(FormTypeEnum.values()).filter(w->StrUtil.equals(w.getType(),type)).findFirst().get();
    }
}
