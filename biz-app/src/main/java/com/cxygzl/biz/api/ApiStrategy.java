package com.cxygzl.biz.api;

import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.LoginUrlDto;
import com.cxygzl.common.dto.third.*;

import java.util.List;
import java.util.Map;

/**
 * API接口
 * 用来扩展数据：对接已存在的组织架构、角色等数据
 */
public interface ApiStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        ApiStrategyFactory.register(key, this);
    }


    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     *
     * @param roleIdList 角色id集合
     * @return
     */
    List<String> loadUserIdListByRoleIdList(List<String> roleIdList);

    /**
     * 根据用户id查询角色id集合
     * @param userId
     * @return
     */
    List<String> loadRoleIdListByUserId(String userId);


    /**
     * 获取所有的角色
     *
     * @return
     */
    List<RoleDto> loadAllRole();

    /**
     * 根据部门id集合获取该部门下的用户id集合
     * 注意：直属部门，不包括子级及以下部门
     *
     * @param deptIdList 部门id集合
     * @return
     */
    List<String> loadUserIdListByDeptIdList(List<String> deptIdList);

    /**
     * 根据父级id获取所有的部门
     * 若父级id为空 则获取所有的部门
     *
     * @param parentDeptId 父级id
     * @return
     */
    List<DeptDto> loadAllDept(String parentDeptId);

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    List<UserDto> loadUserByDept(String deptId);

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    UserDto getUser(String userId);

    /**
     * 获取部门数据
     * @param deptId
     * @return
     */
    DeptDto getDept(String deptId);

    /**
     * 根据手机号获取用户
     * @param phone
     * @return
     */
    UserDto getUserByPhone(String phone);

    /**
     * 根据名字搜索用户
     *
     * @param name
     * @return
     */
    List<UserDto> searchUser(String name);

    /**
     * 根据token获取用户id
     * 处理登录接口请求的
     *
     * @param token
     * @return
     */
    String getUserIdByToken(String token);

    /**
     * 查询用户属性
     *
     * @return
     */
    List<UserFieldDto> queryUserFieldList();

    /**
     * 查询用户属性值
     *
     * @return
     */
    Map<String, String> queryUserFieldData(String userId);

    /**
     * 创建流程
     *
     * @param processDto
     */
    void createProcess(CreateProcessDto processDto);

    /**
     * 发起流程
     *
     * @param processDto
     */
    void startProcess(StartProcessDto processDto);

    /**
     * 完成流程实例
     *
     * @param processInstanceParamDto
     */
    void completeProcessInstance(com.cxygzl.common.dto.third.ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 终止流程
     *
     * @param processInstanceParamDto
     */
    void stopProcessInstance(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 添加待办任务
     *
     * @param taskParamDtoList
     */
    void addWaitTask(List<com.cxygzl.common.dto.third.TaskParamDto> taskParamDtoList);


    /**
     * 处理任务
     *
     * @param taskParamDtoList
     * @param taskType 任务类型 {@link ProcessInstanceConstant.TaskType}
     */
    void handleTask(List<TaskParamDto> taskParamDtoList, String taskType);

    /**
     * 重新拉取数据
     * 现在用来钉钉同步数据使用 其他业务视情况决定是否启用
     */
    void loadRemoteData();

    /**
     * 获取登录地址
     *
     * @return
     */
    LoginUrlDto getLoginUrl();

    /**
     * 获取登录参数
     * 钉钉扫码登录使用
     * 其他业务视情况决定是否启用
     * @return
     */
    Object getLoginParam();

    /**
     * 发送消息
     *  业务行对接消息
     * @param messageDto
     */
    void sendMsg(MessageDto messageDto);
}
