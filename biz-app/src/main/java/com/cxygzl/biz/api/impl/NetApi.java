package com.cxygzl.biz.api.impl;

import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.service.IRoleService;
import com.cxygzl.biz.service.IUserRoleService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.RoleDto;
import com.cxygzl.common.dto.third.UserDto;
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
    public List<String> loadUserIdListByRoleIdList(List<String> roleIdList) {

        String post = CoreHttpUtil.post(roleIdList, "/test/net/loadUserIdListByRoleIdList");
        return JSON.parseArray(post,String.class);


    }

    /**
     * 获取所有的角色
     *
     * @return
     */
    @Override
    public List<RoleDto> loadAllRole() {
        String post = CoreHttpUtil.get( "/test/net/loadUserIdListByRoleIdList");
        return JSON.parseArray(post,RoleDto.class);
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
        String post = CoreHttpUtil.post(deptIdList, "/test/net/loadUserIdListByDeptIdList");
        return JSON.parseArray(post,String.class);
    }

    /**
     * 获取所有的部门
     *
     * @return
     */
    @Override
    public List<DeptDto> loadAllDept(String parentDeptId) {
        String post = CoreHttpUtil.get( "/test/net/loadAllDept?parentDeptId="+(parentDeptId==null?"":parentDeptId));
        return JSON.parseArray(post,DeptDto.class);
    }

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    @Override
    public List<UserDto> loadUserByDept(String deptId) {
        String post = CoreHttpUtil.get( "/test/net/loadUserByDept?deptId="+deptId);
        return JSON.parseArray(post,UserDto.class);
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserDto getUser(String userId) {
        String post = CoreHttpUtil.get( "/test/net/getUser?userId="+userId);
        return JSON.parseObject(post,UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String name) {
        String post = CoreHttpUtil.get( "/test/net/searchUser?name="+name);
        return JSON.parseArray(post,UserDto.class);
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
        String post = CoreHttpUtil.get( "/test/net/getUserIdByToken?token="+token);
        return post;
    }

    /**
     * 发送消息
     *
     * @param messageDto
     */
    @Override
    public void sendMsg(MessageDto messageDto) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("net");
    }
}
