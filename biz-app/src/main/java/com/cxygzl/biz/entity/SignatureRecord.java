package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 签名url记录表
 * </p>
 *
 * @author Vincent
 * @since 2023-10-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`signature_record`")
public class SignatureRecord extends BaseEntity {



    /**
     * 签名图片url
     */
    @TableField("`url`")
    private String url;

    /**
     * 用户
     */
    @TableField("`user_id`")
    private String userId;
}
