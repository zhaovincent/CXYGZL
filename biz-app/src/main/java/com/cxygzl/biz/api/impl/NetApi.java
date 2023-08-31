package com.cxygzl.biz.api.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.service.IRoleService;
import com.cxygzl.biz.service.IUserRoleService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.dto.LoginUrlDto;
import com.cxygzl.common.dto.third.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
     * 根据用户id查询角色id集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> loadRoleIdListByUserId(String userId) {
        return null;
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
        return JSON.parseArray(post, UserDto.class);
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

    /**
     * 获取部门数据
     *
     * @param deptId
     * @return
     */
    @Override
    public DeptDto getDept(String deptId) {
        return null;
    }

    /**
     * 根据手机号获取用户
     *
     * @param phone
     * @return
     */
    @Override
    public UserDto getUserByPhone(String phone) {
        return null;
    }

    @Override
    public List<UserDto> searchUser(String name) {
        String post = CoreHttpUtil.get( "/test/net/searchUser?name="+name);
        return JSON.parseArray(post,UserDto.class);
    }

    /**
     * 查询用户属性
     *
     * @return
     */
    @Override
    public List<UserFieldDto> queryUserFieldList() {
        String post = CoreHttpUtil.get( "/test/net/queryUserFieldList");
        return JSON.parseArray(post,UserFieldDto.class);
    }

    /**
     * 查询用户属性值
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, String> queryUserFieldData(String userId) {
        String post = CoreHttpUtil.get( "/test/net/queryUserFieldData?userId="+userId);
        return JSON.parseObject(post, new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * 创建流程
     *
     * @param processDto
     */
    @Override
    public void createProcess(CreateProcessDto processDto) {

    }

    /**
     * 发起流程
     *
     * @param processDto
     */
    @Override
    public void startProcess(StartProcessDto processDto) {

    }

    /**
     * 完成流程实例
     *
     * @param processInstanceParamDto
     */
    @Override
    public void completeProcessInstance(com.cxygzl.common.dto.third.ProcessInstanceParamDto processInstanceParamDto) {

    }

    /**
     * 终止流程
     *
     * @param processInstanceParamDto
     */
    @Override
    public void stopProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

    }

    /**
     * 添加待办任务
     *
     * @param taskParamDtoList
     */
    @Override
    public void addWaitTask(List<TaskParamDto> taskParamDtoList) {

    }


    /**
     * 处理任务
     *
     * @param taskParamDtoList
     * @param taskType
     */
    @Override
    public void handleTask(List<TaskParamDto> taskParamDtoList, String taskType) {

    }

    /**
     * 重新拉取数据
     */
    @Override
    public void loadRemoteData() {

    }

    /**
     * 获取登录地址
     *
     * @return
     */
    @Override
    public LoginUrlDto getLoginUrl() {
        return null;
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    @Override
    public Object getLoginParam() {
        return null;
    }

    /**
     * 发送消息
     *
     * @param messageDto
     */
    @Override
    public void sendMsg(MessageDto messageDto) {

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

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("net");
    }
}
