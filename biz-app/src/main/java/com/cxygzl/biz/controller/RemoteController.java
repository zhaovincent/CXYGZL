package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IRemoteService;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.UserFieldDto;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 远程请求控制器
 */
@RestController
@RequestMapping("remote")
public class RemoteController {
    @Resource
    private IRemoteService remoteService;


    /**
     * 根据部门id获取部门列表
     * @param deptIdList
     * @return
     */
    @PostMapping("queryDeptList")
    public R<List<DeptDto>> queryDeptList(@RequestBody List<String> deptIdList){
        return remoteService.queryDeptList(deptIdList);
    }
    /**
     * 保存待办任务
     *
     * @param messageDto
     * @return
     */
    @PostMapping("saveMessage")
    public R saveMessage(@RequestBody MessageDto messageDto) {
        return remoteService.saveMessage(messageDto);
    }

    /**
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     * @return
     */
    @PostMapping("queryUserIdListByRoleIdList")
    public R<List<String>> queryUserIdListByRoleIdList(@RequestBody List<String> roleIdList) {
        return remoteService.queryUserIdListByRoleIdList(roleIdList);
    }

    /**
     * 保存抄送
     *
     * @param copyDto
     * @return
     */
    @PostMapping("savecc")
    public R saveCC(@RequestBody ProcessCopyDto copyDto) {
        return remoteService.saveCC(copyDto);
    }

    /**
     * 检查是否是所有的父级
     *
     * @param checkParentDto
     * @return
     */
    @PostMapping("checkIsAllParent")
    public R<Boolean> checkIsAllParent(@RequestBody CheckParentDto checkParentDto) {
        return remoteService.checkIsAllParent(checkParentDto);
    }


    /**
     * 根据部门id集合查询用户id集合
     *
     * @param depIdList
     * @return
     */
    @PostMapping("queryUserIdListByDepIdList")
    public R<List<String>> queryUserIdListByDepIdList(@RequestBody List<String> depIdList) {
        return remoteService.queryUserIdListByDepIdList(depIdList);
    }

    /**
     * 检查是否是所有的子级
     *
     * @param checkChildDto
     * @return
     */
    @PostMapping("checkIsAllChild")
    public R<Boolean> checkIsAllChild(@RequestBody CheckChildDto checkChildDto) {
        return remoteService.checkIsAllChild(checkChildDto);
    }

    /**
     * 获取用户的信息-包括扩展字段
     *
     * @param userId
     * @return
     */
    @GetMapping("queryUserAllInfo")
    public R<Map<String, Object>> queryUserAllInfo(String userId) {
        return remoteService.queryUserAllInfo(userId);
    }

    /**
     * 查询用户属性配置
     *
     * @return
     */
    @GetMapping("queryUseField")
    public R<List<UserFieldDto>> queryUseField() {
        return remoteService.queryUseField();
    }

    /**
     * 根据用户id查询上级部门列表
     *
     * @param userId
     * @return
     */
    @SneakyThrows
    @GetMapping("queryParentDepListByUserId")
    public R<List<DeptDto>> queryParentDepListByUserId(String userId) {

        return remoteService.queryParentDepListByUserId(userId);
    }

    /**
     * 开始节点事件
     *
     * @param recordParamDto
     * @return
     */
    @PostMapping("startNodeEvent")
    public R startNodeEvent(@RequestBody ProcessNodeRecordParamDto recordParamDto) {
        return remoteService.startNodeEvent(recordParamDto);
    }

    /**
     * 记录执行节点父子关系
     *
     * @param recordParamDto
     * @return
     */
    @PostMapping("saveParentChildExecution")
    public R saveParentChildExecution(@RequestBody ProcessNodeRecordParamDto recordParamDto) {
        return remoteService.saveParentChildExecution(recordParamDto);
    }


    /**
     * 流程创建了
     *
     * @param processInstanceRecordParamDto
     * @return
     */
    @PostMapping("startProcessEvent")
    public R createProcessEvent(@RequestBody ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        return remoteService.startProcessEvent(processInstanceRecordParamDto);
    }

    /**
     * 结束节点事件
     *
     * @param recordParamDto
     * @return
     */
    @PostMapping("endNodeEvent")
    public R endNodeEvent(@RequestBody ProcessNodeRecordParamDto recordParamDto) {
        return remoteService.endNodeEvent(recordParamDto);
    }

    /**
     * 取消节点事件
     *
     * @param recordParamDto
     * @return
     */
    @PostMapping("cancelNodeEvent")
    public R cancelNodeEvent(@RequestBody ProcessNodeRecordParamDto recordParamDto) {
        return remoteService.cancelNodeEvent(recordParamDto);
    }

    /**
     * 开始设置执行人
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @PostMapping("startAssignUser")
    public R startAssignUser(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return remoteService.startAssignUser(processNodeRecordAssignUserParamDto);
    }

    /**
     * 任务结束事件
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @PostMapping("taskEndEvent")
    public R taskEndEvent(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return remoteService.taskEndEvent(processNodeRecordAssignUserParamDto);
    }

    /**
     * 任务取消事件
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @PostMapping("taskCancelEvent")
    public R taskCancelEvent(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return remoteService.taskCancelEvent(processNodeRecordAssignUserParamDto);
    }

    /**
     * 实例结束
     *
     * @param processInstanceParamDto
     * @return
     */
    @PostMapping("endProcess")
    public R endProcess(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
        return remoteService.endProcess(processInstanceParamDto);
    }

    /**
     * 查询流程管理员
     *
     * @param flowId
     * @return
     */
    @GetMapping("queryProcessAdmin")
    public R<String> queryProcessAdmin(String flowId) {
        return remoteService.queryProcessAdmin(flowId);
    }

    /**
     * 查询流程设置
     *
     * @param flowId
     * @return
     */
    @GetMapping("queryProcessSetting")
    public R<FlowSettingDto> queryProcessSetting(String flowId) {
        return remoteService.queryProcessSetting(flowId);
    }


    /**
     * 保存节点数据
     * @param processNodeDataDto
     * @return
     */
    @PostMapping("saveNodeData")
    public R saveNodeData(@RequestBody ProcessNodeDataDto processNodeDataDto){
        return remoteService.saveNodeData(processNodeDataDto);
    }

    /**
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    @GetMapping("getNodeData")
    public R<String> getNodeData(String flowId,String nodeId){
        return remoteService.getNodeData(flowId, nodeId);
    }
}
