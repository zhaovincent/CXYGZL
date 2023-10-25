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

    INPUT("Input","单行文本",""),
    TEXTAREA("Textarea","多行文本",""),
    DESCRIPTION("Description","说明",""),
    AREA("Area","地区",new HashMap<>()),
    NUMBER("Number","数字",null),
    DATE("Date","日期",null),
    DATE_TIME("DateTime","日期时间",null),
    LAYOUT("Layout","明细",new ArrayList<>()),
    TIME("Time","时间",null),
    MONEY("Money","金额",null),
    SCORE("Score","评分",null),
    UPLOAD_FILE("UploadFile","文件",new ArrayList<>()),
    UPLOAD_IMAGE("UploadImage","图片",new ArrayList<>()),
    SINGLE_SELECT("SingleSelect","单选",new ArrayList<>()),
    MULTI_SELECT("MultiSelect","多选",new ArrayList<>()),
    SELECT_DEPT("SelectDept","单部门",new ArrayList<>()),
    SELECT_USER("SelectUser","单用户",new ArrayList<>()),
    SELECT_MULTI_DEPT("SelectMultiDept","多部门",new ArrayList<>()),
    SELECT_MULTI_USER("SelectMultiUser","多用户",new ArrayList<>()),
    SIGNATURE("Signature","手写签名",null),
    FORMULA("Formula","计算公式",null),
    RELATED_PROCESS("RelatedProcess","关联流程",null),


    ;





    private String type;

    private String name;
    private Object defaultValue;


    public static FormTypeEnum getByType(String type){
        return Arrays.stream(FormTypeEnum.values()).filter(w->StrUtil.equals(w.getType(),type)).findFirst().get();
    }
}
