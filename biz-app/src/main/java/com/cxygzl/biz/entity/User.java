package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`user`")
public class User  extends BaseEntity {



    /**
     * 用户名
     */
    @TableField("`name`")
    private String name;

    /**
     * 拼音  全拼
     */
    @TableField("`pinyin`")
    private String pinyin;

    /**
     * 拼音, 首字母缩写
     */
    @TableField("`py`")
    private String py;

    /**
     * 昵称
     */
    @TableField("`nick_name`")
    private String nickName;

    /**
     * 头像url
     */
    @TableField("`avatar_url`")
    private String avatarUrl;

    /**
     * 性别1男2女
     */
    @TableField("`gender`")
    private Integer gender;

    /**
     * 部门id
     */
    @TableField("`dept_id`")
    private Long deptId;


    /**
     * 入职日期
     */
    @TableField("`entry_date`")
    private Date entryDate;



    /**
     * 登录密码
     */
    @TableField(value = "`password`",select = false)
    private String password;

    /**
     * 手机号
     */
    @TableField("`phone`")
    private String phone;
    @TableField("`status`")
    private Integer status;
}
