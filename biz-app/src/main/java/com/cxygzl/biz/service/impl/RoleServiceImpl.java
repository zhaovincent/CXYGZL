package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.mapper.RoleMapper;
import com.cxygzl.biz.service.IRoleMenuService;
import com.cxygzl.biz.service.IRoleService;
import com.cxygzl.biz.service.IUserRoleService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.vo.RoleQueryVO;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.NodeUser;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-06-08
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IUserService userService;
    @Resource
    private IRoleMenuService roleMenuService;

    /**
     * 根据用户id获取角色key集合
     *
     * @param userId
     * @return
     */
    @Override
    public R<Set<String>> queryRoleKeyByUserId(long userId) {
        List<UserRole> userRoleList = userRoleService.queryListByUserId(userId).getData();
        if(CollUtil.isEmpty(userRoleList)){
            return R.success(CollUtil.newHashSet());
        }
        Set<Long> roleIdSet = userRoleList.stream().map(w -> w.getRoleId()).collect(Collectors.toSet());
        List<Role> roles = this.listByIds(roleIdSet);
        if(CollUtil.isEmpty(roles)){
            return R.success(CollUtil.newHashSet());
        }
        Set<String> keySet = roles.stream().map(w -> w.getKey()).collect(Collectors.toSet());
        return R.success(keySet);

    }

    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public Object queryList(RoleQueryVO pageDto) {

        Page<Role> page = this.lambdaQuery().orderByAsc(Role::getSort)
                .like(StrUtil.isNotBlank(pageDto.getKeywords()),Role::getName,pageDto.getKeywords())
                .page(new Page<>(pageDto.getPageNum(),
                pageDto.getPageSize()));

        return R.success(page);
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public R queryAll() {
        return R.success(this.lambdaQuery().list());
    }

    /**
     * 查询角色下的用户列表
     *
     * @param id
     * @return
     */
    @Override
    public Object queryUserList(long id) {
        List<UserRole> userRoleList = userRoleService.lambdaQuery().eq(UserRole::getRoleId, id).list();

        List<NodeUser> nodeUserDtoList=new ArrayList<>();

        for (UserRole userRole : userRoleList) {
            User user = userService.lambdaQuery().eq(User::getId, userRole.getUserId()).one();
            NodeUser nodeUserDto = new NodeUser();
            nodeUserDto.setId(String.valueOf(user.getId()));
            nodeUserDto.setName(user.getName());
            nodeUserDto.setType(NodeUserTypeEnum.USER.getKey());
            nodeUserDto.setSelected(true);
            nodeUserDto.setAvatar(user.getAvatarUrl());


            nodeUserDtoList.add(nodeUserDto);
        }

        return R.success(nodeUserDtoList);
    }

    /**
     * 保存角色用户
     *
     * @param nodeUserDtoList
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Object saveUserList(List<NodeUser> nodeUserDtoList, long id) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getRoleId,id);
        userRoleService.remove(queryWrapper);

        for (NodeUser nodeUserDto : nodeUserDtoList) {
            UserRole userRole = new UserRole();

            userRole.setUserId(Long.valueOf(nodeUserDto.getId()));
            userRole.setRoleId(id);
            userRoleService.save(userRole);

        }

        return R.success();
    }

    /**
     * 创建角色
     *
     * @param role
     * @return
     */
    @Override
    public Object create(Role role) {
        String key = role.getKey();
        Long count = this.lambdaQuery().eq(Role::getKey, key).count();
        if(count>0){
            return R.fail("角色唯一键已存在");
        }
        String name = role.getName();
          count = this.lambdaQuery().eq(Role::getName, name).count();
        if(count>0){
           return  R.fail("角色名字已存在");
        }
        role.setUserId(StpUtil.getLoginIdAsLong());
        this.save(role);
        return R.success();
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @Override
    public Object edit(Role role) {
        String key = role.getKey();
        Long count = this.lambdaQuery().ne(Role::getId,role.getId()).eq(Role::getKey, key).count();
        if(count>0){
            return R.fail("角色唯一键已存在");
        }
        String name = role.getName();
        count = this.lambdaQuery().ne(Role::getId,role.getId()).eq(Role::getName, name).count();
        if(count>0){
            return  R.fail("角色名字已存在");
        }
        role.setUserId(StpUtil.getLoginIdAsLong());
        this.updateById(role);
        return R.success();
    }

    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    @Transactional
    @Override
    public Object delete(Role role) {
        this.removeById(role.getId());
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getRoleId,role.getId());
        userRoleService.remove(queryWrapper);
        return R.success();
    }

    /**
     * 获取角色的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合(包括按钮权限ID)
     */
    @Override
    public R<List<Long>> getRoleMenuIds(long roleId) {

        MPJLambdaWrapper<RoleMenu> lambdaWrapper=new MPJLambdaWrapper<>();
        lambdaWrapper.select(RoleMenu::getMenuId)
                .innerJoin(Menu.class,Menu::getId,RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId,roleId);
        List<Long> longList = roleMenuService.selectJoinList(Long.class, lambdaWrapper);

        return R.success(longList);
    }

    /**
     * 修改角色的资源权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    @Override
    public R updateRoleMenus(long roleId, List<Long> menuIds) {
        // 删除角色菜单
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        // 新增角色菜单
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<RoleMenu> roleMenus = menuIds.stream()
                    .map(menuId ->{
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setMenuId(menuId);
                        roleMenu.setRoleId(roleId);
                        return roleMenu;
                    })
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenus);
        }
        return R.success();
    }
}
