package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户-角色
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`user_role`")
public class UserRole  extends BaseEntity {

    /**
     * 用户id
     */
    @TableField("`user_id`")
    private Long userId;

    /**
     * 角色id
     */
    @TableField("`role_id`")
    private Long roleId;
}
