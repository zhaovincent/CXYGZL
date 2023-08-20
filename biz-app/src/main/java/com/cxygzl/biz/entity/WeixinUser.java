package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 微信用户
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`weixin_user`")
public class WeixinUser extends BaseEntity {

    /**
     * 用户id
     */
    @TableField("`user_id`")
    private String userId;

    /**
     * union_id
     */
    @TableField("`union_id`")
    private String unionId;
    /**
     * open_id
     */
    @TableField("`open_id`")
    private String openId;
    /**
     * 手机号
     */
    @TableField("`phone`")
    private String phone;
}
