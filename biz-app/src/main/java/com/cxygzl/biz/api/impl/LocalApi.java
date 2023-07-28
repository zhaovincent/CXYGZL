package com.cxygzl.biz.api.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.entity.UserRole;
import com.cxygzl.biz.service.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.RoleDto;
import com.cxygzl.common.dto.third.UserDto;
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

    @Resource
    private IMessageService messageService;
    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     *
     * @param roleIdList 角色id集合
     * @return
     */
    @Override
    public List<String> loadUserIdListByRoleIdList(List<String> roleIdList) {
        List<UserRole> userRoleList = userRoleService.lambdaQuery().in(UserRole::getRoleId, roleIdList).list();
        Set<String> userIdSet = userRoleList.stream().map(w ->String.valueOf( w.getUserId())).collect(Collectors.toSet());
        return CollUtil.newArrayList(userIdSet);
    }

    /**
     * 获取所有的角色
     *
     * @return
     */
    @Override
    public List<RoleDto> loadAllRole() {
        List<Role> roleList = roleService.lambdaQuery().list();
        return BeanUtil.copyToList(roleList,RoleDto.class);

    }

    /**
     * 根据部门id集合获取该部门下的用户id集合
     * 注意：直属部门，不包括子级及以下部门
     *
     * @param deptIdList 部门id集合
     * @return
     */
    @Override
    public List<String> loadUserIdListByDeptIdList(List<String> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)) {
           return new ArrayList<>();
        }
        List<User> userList = userService.lambdaQuery().in(User::getDeptId, deptIdList).list();
        return userList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
    }

    /**
     * 获取所有的部门
     *
     * @return
     */
    @Override
    public List<DeptDto> loadAllDept(String parentDeptId) {
        List<Dept> deptList = deptService.lambdaQuery()
                .eq(parentDeptId != null, Dept::getParentId, parentDeptId)
                .list();
        return BeanUtil.copyToList(deptList,DeptDto.class);

    }

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    @Override
    public List<UserDto> loadUserByDept(String deptId) {
        List<User> userList = userService.lambdaQuery()
                .eq(User::getDeptId, deptId)
                .list();
        return BeanUtil.copyToList(userList,UserDto.class);

    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserDto getUser(String userId) {
        User user = userService.getById(userId);
        return BeanUtil.copyProperties(user,UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String name) {
        List<User> userList = userService.lambdaQuery().and(k ->
                k.like(User::getPinyin, name)
                        .or(w -> w.like(User::getPy, name))
                        .or(w -> w.like(User::getName, name))
        ).list();

        return BeanUtil.copyToList(userList,UserDto.class);

    }

    /**
     * 根据token获取用户id
     * 处理登录接口请求的
     *
     * @param token
     * @return
     */
    @Override
    public String getUserIdByToken(String token) {
        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
        return loginIdByToken==null?null:loginIdByToken.toString();
    }

    /**
     * 发送消息
     *
     * @param messageDto
     */
    @Override
    public void sendMsg(MessageDto messageDto) {
        messageService.saveMessage(messageDto);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("local");
    }
}
