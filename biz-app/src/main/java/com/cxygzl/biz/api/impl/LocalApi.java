package com.cxygzl.biz.api.impl;

import cn.hutool.core.collection.CollUtil;
import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.entity.UserRole;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.service.IRoleService;
import com.cxygzl.biz.service.IUserRoleService;
import com.cxygzl.biz.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocalApi implements ApiStrategy, InitializingBean {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IRoleService roleService;
    @Resource
    private IDeptService deptService;
    @Resource
    private IUserService userService;
    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     *
     * @param roleIdList 角色id集合
     * @return
     */
    @Override
    public List<Long> loadUserIdListByRoleIdList(List<Long> roleIdList) {
        List<UserRole> userRoleList = userRoleService.lambdaQuery().in(UserRole::getRoleId, roleIdList).list();
        Set<Long> userIdSet = userRoleList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());
        return CollUtil.newArrayList(userIdSet);
    }

    /**
     * 获取所有的角色
     *
     * @return
     */
    @Override
    public List<Role> loadAllRole() {
        List<Role> roleList = roleService.lambdaQuery().list();
        return roleList;
    }

    /**
     * 根据部门id集合获取该部门下的用户id集合
     * 注意：直属部门，不包括子级及以下部门
     *
     * @param deptIdList 部门id集合
     * @return
     */
    @Override
    public List<Long> loadUserIdListByDeptIdList(List<Long> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)) {
           return new ArrayList<>();
        }
        List<User> userList = userService.lambdaQuery().in(User::getDeptId, deptIdList).list();
        return userList.stream().map(w -> w.getId()).collect(Collectors.toList());
    }

    /**
     * 获取所有的部门
     *
     * @return
     */
    @Override
    public List<Dept> loadAllDept(Long parentDeptId) {
        List<Dept> deptList = deptService.lambdaQuery()
                .eq(parentDeptId != null, Dept::getParentId, parentDeptId)
                .list();
        return deptList;
    }

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    @Override
    public List<User> loadUserByDept(long deptId) {
        List<User> userList = userService.lambdaQuery()
                .eq(User::getDeptId, deptId)
                .list();
        return userList;
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public User getUser(long userId) {
        return userService.getById(userId);
    }

    @Override
    public List<User> searchUser(String name) {
        List<User> userList = userService.lambdaQuery().and(k ->
                k.like(User::getPinyin, name)
                        .or(w -> w.like(User::getPy, name))
                        .or(w -> w.like(User::getName, name))
        ).list();

        return userList;
    }

    /**
     * 根据token获取用户id
     * 处理登录接口请求的
     *
     * @param token
     * @return
     */
    @Override
    public Long getUserIdByToken(String token) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("local");
    }
}
