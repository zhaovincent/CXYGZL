
package com.cxygzl.biz.service;

import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.UserFieldDto;

import java.util.List;
import java.util.Map;

/**
 * 远程调用的接口
 */
public interface IRemoteService {
    /**
     * 根据用户id查询角色id集合
     * @param userId
     * @return
     */
    R<List<String>> queryRoleIdListByUserId(String userId);

    /**
     * 根据部门id获取部门列表
     * @param deptIdList
     * @return
     */
    R<List<DeptDto>> queryDeptList(List<String> deptIdList);

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
     * 查询用户属性配置
     * @return
     */
    R<List<UserFieldDto>> queryUseField();


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
     * 记录父子执行id
     * @param recordParamDto
     * @return
     */
    R saveParentChildExecution(ProcessNodeRecordParamDto recordParamDto);

    /**
     * 流程创建了
     * @param processInstanceRecordParamDto
     * @return
     */
    R startProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto);



    /**
     * 完成节点事件
     * @param recordParamDto
     * @return
     */
    R endNodeEvent(ProcessNodeRecordParamDto recordParamDto);

    /**
     * 节点取消
     * @param recordParamDto
     * @return
     */
    R cancelNodeEvent(ProcessNodeRecordParamDto recordParamDto);

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
     * 任务取消了--驳回
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    R taskCancelEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

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

    /**
     * 查询流程设置
     * @param flowId
     * @return
     */
    R<FlowSettingDto> queryProcessSetting(String flowId);

    /**
     * 保存流程节点数据
     * @param processNodeDataDto
     * @return
     */
    R saveNodeData(ProcessNodeDataDto processNodeDataDto);

    /***
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    R<String> getNodeData(String flowId,String nodeId);
}
