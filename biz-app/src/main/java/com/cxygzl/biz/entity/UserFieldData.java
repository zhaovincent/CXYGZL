package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户字段-数据
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`user_field_data`")
public class UserFieldData  extends BaseEntity {


    /**
     * 用户id
     */
    @TableField("`user_id`")
    private Long userId;




    /**
     * 数据
     */
    @TableField("`data`")
    private String data;

    /**
     * 字段
     */
    @TableField("`key`")
    private String key;
}
