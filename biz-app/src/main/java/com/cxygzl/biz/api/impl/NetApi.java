package com.cxygzl.biz.api.impl;

import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.service.IRoleService;
import com.cxygzl.biz.service.IUserRoleService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class NetApi implements ApiStrategy, InitializingBean {

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

        String post = CoreHttpUtil.post(roleIdList, "/test/net/loadUserIdListByRoleIdList");
        return JSON.parseArray(post,Long.class);


    }

    /**
     * 获取所有的角色
     *
     * @return
     */
    @Override
    public List<Role> loadAllRole() {
        String post = CoreHttpUtil.get( "/test/net/loadUserIdListByRoleIdList");
        return JSON.parseArray(post,Role.class);
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
        String post = CoreHttpUtil.post(deptIdList, "/test/net/loadUserIdListByDeptIdList");
        return JSON.parseArray(post,Long.class);
    }

    /**
     * 获取所有的部门
     *
     * @return
     */
    @Override
    public List<Dept> loadAllDept(Long parentDeptId) {
        String post = CoreHttpUtil.get( "/test/net/loadAllDept?parentDeptId="+(parentDeptId==null?"":parentDeptId));
        return JSON.parseArray(post,Dept.class);
    }

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    @Override
    public List<User> loadUserByDept(long deptId) {
        String post = CoreHttpUtil.get( "/test/net/loadUserByDept?deptId="+deptId);
        return JSON.parseArray(post,User.class);
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public User getUser(long userId) {
        String post = CoreHttpUtil.get( "/test/net/getUser?userId="+userId);
        return JSON.parseObject(post,User.class);
    }

    @Override
    public List<User> searchUser(String name) {
        String post = CoreHttpUtil.get( "/test/net/searchUser?name="+name);
        return JSON.parseArray(post,User.class);
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
        String post = CoreHttpUtil.get( "/test/net/getUserIdByToken?token="+token);
        return Long.parseLong(post);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("net");
    }
}
