package com.cxygzl.biz.api.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.common.dto.LoginUrlDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.third.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocalApi implements ApiStrategy, InitializingBean {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IUserFieldService userFieldService;
    @Resource
    private IUserFieldDataService userFieldDataService;

    @Resource
    private IMessageService messageService;

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
        List<UserRole> userRoleList = userRoleService.lambdaQuery().in(UserRole::getRoleId, roleIdList).list();
        Set<String> userIdSet = userRoleList.stream().map(w -> String.valueOf(w.getUserId())).collect(Collectors.toSet());
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
        return BeanUtil.copyToList(roleList, RoleDto.class);
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
        List<DeptDto> deptDtoList = new ArrayList<>();
        for (Dept dept : deptList) {
            DeptDto deptDto = BeanUtil.copyProperties(dept, DeptDto.class);
            deptDto.setLeaderUserIdList(CollUtil.newArrayList(String.valueOf(dept.getLeaderUserId())));
            deptDtoList.add(deptDto);
        }

        return deptDtoList;
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
        return BeanUtil.copyToList(userList, UserDto.class);
    }

    /**
     * 根据手机号获取用户
     *
     * @param phone
     * @return
     */
    @Override
    public UserDto getUserByPhone(String phone) {
        User user = userService.lambdaQuery().eq(User::getPhone, phone).one();

        return BeanUtil.copyProperties(user, UserDto.class);

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
        return BeanUtil.copyProperties(user, UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String name) {
        List<User> userList = userService.lambdaQuery().and(k ->
                k.like(User::getPinyin, name)
                        .or(w -> w.like(User::getPy, name))
                        .or(w -> w.like(User::getName, name))
        ).list();

        return BeanUtil.copyToList(userList, UserDto.class);
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
        return loginIdByToken == null ? null : loginIdByToken.toString();
    }

    /**
     * 查询用户属性
     *
     * @return
     */
    @Override
    public List<UserFieldDto> queryUserFieldList() {
        List<UserField> list = userFieldService.lambdaQuery().list();
        return BeanUtil.copyToList(list, UserFieldDto.class);
    }

    /**
     * 查询用户属性值
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, String> queryUserFieldData(String userId) {
        List<UserFieldData> userFieldDataList = userFieldDataService.lambdaQuery().eq(UserFieldData::getUserId, userId).list();
        Map<String, String> collect = userFieldDataList.stream().collect(Collectors.toMap(w -> w.getKey(), w -> w.getData()));
        return collect;
    }

    /**
     * 创建流程
     *
     * @param processDto
     */
    @Override
    public void createProcess(ProcessDto processDto) {

    }

    /**
     * 发起流程
     *
     * @param processDto
     */
    @Override
    public void startProcess(ProcessDto processDto) {

    }

    /**
     * 完成流程实例
     *
     * @param processInstanceParamDto
     */
    @Override
    public void completeProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

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
        return LoginUrlDto.builder().innerUrl(true).url("/aplogin").build();
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    @Override
    public Object getLoginParam() {
        return new HashMap<>();
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
        afterPropertiesSet("local");
    }
}
