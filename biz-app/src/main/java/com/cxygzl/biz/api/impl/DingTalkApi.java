package com.cxygzl.biz.api.impl;

import com.cxygzl.biz.api.ApiStrategy;
import com.cxygzl.biz.utils.DingTalkHttpUtil;
import com.cxygzl.common.dto.LoginUrlDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.*;
import com.cxygzl.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DingTalkApi implements ApiStrategy, InitializingBean {


    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     *
     * @param roleIdList 角色id集合
     * @return
     */
    @Override
    public List<String> loadUserIdListByRoleIdList(List<String> roleIdList) {

        String post = DingTalkHttpUtil.post(roleIdList, "/remote/loadUserIdListByRoleIdList");
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<String>>>() {
        }).getData();


    }

    /**
     * 根据用户id查询角色id集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> loadRoleIdListByUserId(String userId) {
        String post = DingTalkHttpUtil.get( "/remote/loadRoleIdListByUserId?userId="+userId);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<String>>>() {
        }).getData();
    }

    /**
     * 获取所有的角色
     *
     * @return
     */
    @Override
    public List<RoleDto> loadAllRole() {
        String post = DingTalkHttpUtil.get("/remote/loadAllRole");
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<RoleDto>>>() {
        }).getData();
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
        String post = DingTalkHttpUtil.post(deptIdList, "/remote/loadUserIdListByDeptIdList");
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<String>>>() {
        }).getData();
    }

    /**
     * 获取所有的部门
     *
     * @return
     */
    @Override
    public List<DeptDto> loadAllDept(String parentDeptId) {
        String post = DingTalkHttpUtil.get("/remote/loadAllDept?deptId=" + (parentDeptId == null ? "" : parentDeptId));
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<DeptDto>>>() {
        }).getData();
    }

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    @Override
    public List<UserDto> loadUserByDept(String deptId) {
        String post = DingTalkHttpUtil.get("/remote/loadUserByDept?deptId=" + deptId);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<UserDto>>>() {
        }).getData();
    }

    /**
     * 获取部门数据
     *
     * @param deptId
     * @return
     */
    @Override
    public DeptDto getDept(String deptId) {
        String post = DingTalkHttpUtil.get("/remote/getDept?deptId=" + deptId);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<DeptDto>>() {
        }).getData();
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserDto getUser(String userId) {
        String post = DingTalkHttpUtil.get("/remote/getUser?userId=" + userId);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<UserDto>>() {
        }).getData();
    }

    /**
     * 根据手机号获取用户
     *
     * @param phone
     * @return
     */
    @Override
    public UserDto getUserByPhone(String phone) {
        String post = DingTalkHttpUtil.get("/remote/getUserByPhone?phone=" + phone);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<UserDto>>() {
        }).getData();
    }

    @Override
    public List<UserDto> searchUser(String name) {
        String post = DingTalkHttpUtil.get("/remote/searchUser?name=" + name);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<UserDto>>>() {
        }).getData();
    }

    /**
     * 查询用户属性
     *
     * @return
     */
    @Override
    public List<UserFieldDto> queryUserFieldList() {
        String post = DingTalkHttpUtil.get("/remote/queryUserFieldList");
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<UserFieldDto>>>() {
        }).getData();
    }

    /**
     * 查询用户属性值
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, String> queryUserFieldData(String userId) {
        String post = DingTalkHttpUtil.get("/remote/queryUserFieldData?userId=" + userId);
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<Map<String, String>>>() {
        }).getData();
    }

    /**
     * 创建流程
     *
     * @param processDto
     */
    @Override
    public void createProcess(CreateProcessDto processDto) {
        DingTalkHttpUtil.post(processDto, "/remote/createProcess");
    }

    /**
     * 发起流程
     *
     * @param processDto
     */
    @Override
    public void startProcess(StartProcessDto processDto) {
        DingTalkHttpUtil.post(processDto, "/remote/startProcess");

    }

    /**
     * 完成流程实例
     *
     * @param processInstanceParamDto
     */
    @Override
    public void completeProcessInstance(com.cxygzl.common.dto.third.ProcessInstanceParamDto processInstanceParamDto) {
        DingTalkHttpUtil.post(processInstanceParamDto, "/processInstance/complete");
    }

    /**
     * 终止流程
     *
     * @param processInstanceParamDto
     */
    @Override
    public void stopProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {
        DingTalkHttpUtil.post(processInstanceParamDto, "/processInstance/stop");
    }

    /**
     * 添加待办任务
     *
     * @param taskParamDtoList
     */
    @Override
    public void addWaitTask(List<com.cxygzl.common.dto.third.TaskParamDto> taskParamDtoList) {
        DingTalkHttpUtil.post(taskParamDtoList, "/processInstance/addWaitTask");
    }

    /**
     * 处理任务
     *
     * @param taskParamDtoList
     * @param taskType
     */
    @Override
    public void handleTask(List<TaskParamDto> taskParamDtoList, String taskType) {
        DingTalkHttpUtil.post(taskParamDtoList, "/processInstance/handleTask/"+taskType);

    }

    /**
     * 重新拉取数据
     */
    @Override
    public void loadRemoteData() {
        DingTalkHttpUtil.post(new HashMap<>(), "/remote/loadRemoteData");
    }

    /**
     * 获取登录地址
     *
     * @return
     */
    @Override
    public LoginUrlDto getLoginUrl() {
        String s = DingTalkHttpUtil.get("/remote/getLoginUrl");
        R<LoginUrlDto> r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<LoginUrlDto>>() {
        });
        return r.getData();

    }

    /**
     * 获取登录参数
     *
     * @return
     */
    @Override
    public Object getLoginParam() {
        String s = DingTalkHttpUtil.get("/remote/getLoginParam");
        R r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R>() {
        });
        return r.getData();
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

        String s = DingTalkHttpUtil.get("/user/getUserIdByCode?authCode=" + token);
        R<String> r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<String>>() {
        });

        return r.getData();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("dingtalk");
    }
}
