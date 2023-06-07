package com.cxygzl.biz.vo;

import lombok.Data;

@Data
public class UserFieldParamVO {

    /**
     * 用户名
     */
    private String name;


    /**
     * 字段类型
     */
    private String type;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 配置json字符串
     */
    private String configuration;

    private String key;
}
