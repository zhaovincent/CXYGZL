package com.cxygzl.biz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author cyxgzl
 * @since 2023-05-05
 */
@Getter
@Setter
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户名
     */
    private String name;

    /**
     * 拼音  全拼
     */
    private String pinyin;

    /**
     * 拼音, 首字母缩写
     */
    private String py;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别1男2女
     */
    private Integer gender;

    /**
     * 部门id
     */
    private Long deptId;


    /**
     * 入职日期
     */
    @JsonFormat( timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date entryDate;



    private Integer status;
}
