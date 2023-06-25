package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author Vincent
 * @since 2023-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("role_menu")
@Builder
public class RoleMenu extends BaseEntity{

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
