
package com.cxygzl.biz.service;

import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;

import java.util.List;
import java.util.Map;

/**
 * 远程调用的接口
 */
public interface IRemoteService {
    /**
     * 保存待办任务
     * @param messageDto
     * @return
     */
    R saveMessage(MessageDto messageDto);
    /**
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     * @return
     */
    R<List<String>> queryUserIdListByRoleIdList(List<String> roleIdList);

    /**
     * 保存抄送
     * @param copyDto
     * @return
     */
    R saveCC(ProcessCopyDto copyDto);

    /**
     * 检查是否是所有的父级
     * @param checkParentDto
     * @return
     */
    R<Boolean> checkIsAllParent(CheckParentDto checkParentDto);

    /**
     * 根据部门id集合查询用户id集合
     *
     * @param depIdList
     * @return
     */
    R<List<String>> queryUserIdListByDepIdList(List<String> depIdList);

    /**
     * 检查是否是所有的子级
     * @param checkChildDto
     * @return
     */
    R<Boolean> checkIsAllChild(CheckChildDto checkChildDto);

    /**
     * 获取用户的信息-包括扩展字段
     * @param userId
     * @return
     */
    R<Map<String,Object>> queryUserAllInfo(String userId);

    /**
     * 根据当前用户查询包括自己部门在内的上级部门对象
     * @param userId
     * @return
     */
    R<List<DeptDto>> queryParentDepListByUserId(String userId);

    /**
     * 开始节点事件
     * @param recordParamDto
     * @return
     */
    R startNodeEvent(ProcessNodeRecordParamDto recordParamDto);

    /**
     * 流程创建了
     * @param processInstanceRecordParamDto
     * @return
     */
    R createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto);



    /**
     * 完成节点事件
     * @param recordParamDto
     * @return
     */
    R endNodeEvent(ProcessNodeRecordParamDto recordParamDto);

    /**
     * 开始设置执行人
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    R startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

    /**
     * 任务结束事件
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

    /**
     * 实例结束
     *
     * @param processInstanceParamDto
     * @return
     */
    R endProcess(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 查询流程管理员
     *
     * @param flowId
     * @return
     */
    R<String> queryProcessAdmin(String flowId);
}
