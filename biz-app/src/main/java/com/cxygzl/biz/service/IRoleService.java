package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.vo.RoleQueryVO;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.NodeUser;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-06-08
 */
public interface IRoleService extends IService<Role> {

    /**
     * 根据用户id获取角色key集合
     * @param userId
     * @return
     */
    R<Set<String>> queryRoleKeyByUserId(String userId);

    /**
     * 查询列表
     * @param pageDto
     * @return
     */
    Object queryList(RoleQueryVO pageDto);

    /**
     * 查询所有角色
     * @return
     */
    R queryAll();

    /**
     * 查询角色下的用户列表
     * @param id
     * @return
     */
    Object queryUserList(long id);

    /**
     * 保存角色用户
     * @param nodeUserDtoList
     * @param id
     * @return
     */
    Object saveUserList(List<NodeUser> nodeUserDtoList, long id);

    /**
     * 创建角色
     * @param role
     * @return
     */
    Object create(Role role);

    /**
     * 修改角色
     * @param role
     * @return
     */
    Object edit(Role role);

    /**
     * 删除角色
     * @param role
     * @return
     */
    Object delete(Role role);

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合(包括按钮权限ID)
     */
    R<List<Long>> getRoleMenuIds(long roleId);

    /**
     * 修改角色的资源权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    R updateRoleMenus(long roleId, List<Long> menuIds);
}
