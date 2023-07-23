package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`role_menu`")
public class RoleMenu  extends BaseEntity {

    /**
     * 角色ID
     */
    @TableField("`role_id`")
    private Long roleId;

    /**
     * 菜单ID
     */
    @TableField("`menu_id`")
    private Long menuId;

}
