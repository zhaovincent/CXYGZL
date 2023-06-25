package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.service.IRoleService;
import com.cxygzl.biz.vo.RoleQueryVO;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.NodeUser;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色
 */
@RestController
@RequestMapping(value = {"role","api/role"})
public class RoleController {


    @Resource
    private IRoleService roleService;

    /**
     * 创建角色
     * @param role
     * @return
     */
    @PostMapping("create")
    public Object create(@RequestBody Role role){
        return roleService.create(role);
    }
    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("edit")
    public Object edit(@RequestBody Role role){
        return roleService.edit(role);
    }


    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    @SneakyThrows
    @DeleteMapping("delete")
    public Object delete(@RequestBody Role role) {

        return roleService.delete(role);

    }
    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合(包括按钮权限ID)
     */
    @GetMapping("getRoleMenuIds")
    R<List<Long>> getRoleMenuIds(long roleId){
        return roleService.getRoleMenuIds(roleId);
    }

    /**
     * 修改角色权限
     * @param roleId
     * @param menuIds
     * @return
     */
    @PutMapping("/{roleId}/menus")
    public R updateRoleMenus(
            @PathVariable Long roleId,
            @RequestBody List<Long> menuIds
    ) {
      return roleService.updateRoleMenus(roleId,menuIds);

    }
    /**
     * 获取角色详细信息
     * @param user
     * @return
     */
    @GetMapping("getDetail")
    public Object getDetail(long id){
        return roleService.getById(id);
    }


    /**
     * 查询列表
     * @param pageDto
     * @return
     */
    @PostMapping("queryList")
    public Object queryList(@RequestBody RoleQueryVO pageDto){
        return roleService.queryList(pageDto);
    }

    /**
     * 查询所有角色
     * @param pageDto
     * @return
     */
    @GetMapping("queryAll")
    public R queryAll(){
        return roleService.queryAll();
    }

    /**
     * 保存角色用户
     * @param pageDto
     * @return
     */
    @PostMapping("saveUserList")
    public Object saveUserList(@RequestBody List<NodeUser> nodeUserDtoList, long id){
        return roleService.saveUserList(nodeUserDtoList,id);
    }

    /**
     * 查询角色下的用户列表
     * @param id
     * @return
     */
    @GetMapping("queryUserList")
    public Object queryUserList(long id){
        return roleService.queryUserList(id);
    }
}
