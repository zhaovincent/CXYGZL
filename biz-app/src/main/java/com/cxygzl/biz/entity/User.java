package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    private Long depId;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField( fill = FieldFill.INSERT)
    private Boolean delFlag;

    /**
     * 入职日期
     */
    @JsonFormat( timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date entryDate;

    /**
     * 离职日期
     */
    @JsonFormat( timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date leaveDate;

    /**
     * 创建时间
     */
    @TableField( fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
