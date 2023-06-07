package com.cxygzl.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.*;
import org.springframework.core.env.Environment;

import java.util.List;

public class CoreHttpUtil {

    public static String post(Object object, String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("biz.url");


        return HttpUtil.post(StrUtil.format("{}{}", bizUrl, url), JSON.toJSONString(object));
    }

    public static String get(String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("biz.url");


        return HttpUtil.get(StrUtil.format("{}{}", bizUrl, url));
    }

    /**
     * 检查列表是否是给定的部门的子级
     * 全部都是
     *
     * @param depId
     * @param deptIdList
     * @return
     */
    public static String checkDepIsAllChild(Long depId, List<Long> deptIdList) {
        CheckChildDto parentDto = new CheckChildDto();
        parentDto.setChildId(depId);
        parentDto.setDeptIdList(deptIdList);

        return post(parentDto, "/remote/checkIsAllChild");

    }

    /**
     * 检查列表是否是给定的部门的父级
     * 全部都是
     *
     * @param depId
     * @param deptIdList
     * @return
     */
    public static String checkDepIsAllParent(Long depId, List<Long> deptIdList) {
        CheckParentDto parentDto = new CheckParentDto();
        parentDto.setParentId(depId);
        parentDto.setDeptIdList(deptIdList);

        return post(parentDto, "/remote/checkIsAllParent");

    }

    /**
     * 查询用户所有的信息数据
     *
     * @param userId
     * @return
     */
    public static String queryUserAllInfo(Long userId) {
        return get("/remote/queryUserAllInfo?userId=" + userId);

    }

    /**
     * 节点开始事件
     *
     * @param nodeRecordParamDto
     */
    public static void startNodeEvent(ProcessNodeRecordParamDto nodeRecordParamDto) {
        post(nodeRecordParamDto, "/remote/startNodeEvent");
    }

    /**
     * 节点结束事件
     *
     * @param nodeRecordParamDto
     */
    public static void endNodeEvent(ProcessNodeRecordParamDto nodeRecordParamDto) {
        post(nodeRecordParamDto, "/remote/endNodeEvent");
    }

    /**
     * 流程结束事件
     *
     * @param processInstanceParamDto
     */
    public static void endProcessEvent(ProcessInstanceParamDto processInstanceParamDto) {
        post(processInstanceParamDto, "/remote/endProcess");
    }

    /**
     * 流程结束事件
     *
     * @param processInstanceParamDto
     */
    public static void createProcessEvent(ProcessInstanceRecordParamDto processInstanceParamDto) {
        post(processInstanceParamDto, "/remote/createProcessEvent");
    }

    /**
     * 根据部门id集合查询所有的用户id集合
     *
     * @param deptIdList
     */
    public static String queryUserIdListByDepIdList(List<String> deptIdList) {
        return post(deptIdList, "/remote/queryUserIdListByDepIdList");
    }

    /**
     * 根据用户id查询上级部门id集合
     *
     * @param userId 用户id
     */
    public static String queryParentDepListByUserId(long userId) {
        return get( "/remote/queryParentDepListByUserId?userId="+userId);
    }

    /**
     * 查询流程管理员
     *
     * @param processId 流程id
     */
    public static String queryProcessAdmin(String processId) {
        return get( "/remote/queryProcessAdmin?processId="+processId);
    }

    /**
     * 查询节点数据
     * @param processId
     * @param nodeId
     * @return
     */
    public static String queryNodeOriData(String processId,String nodeId) {
        return get( StrUtil.format("/processNodeData/getNodeData?processId" +
                "={}&nodeId={}",processId,nodeId));
    }

    /**
     * 节点开始指派用户了
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    public static String startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return post(processNodeRecordAssignUserParamDto,"/remote/startAssignUser");
    }

    /**
     * 任务结束事件
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    public static String taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return post(processNodeRecordAssignUserParamDto,"/remote/taskEndEvent");
    }

    /**
     * 保存抄送数据
     * @param processCopyDto
     * @return
     */
    public static String saveCC(ProcessCopyDto processCopyDto) {
        return post(processCopyDto,"/remote/savecc");
    }

    /**
     * 保存节点原始数据
     * @param processNodeDataDto
     * @return
     */
    public static String saveNodeOriData(ProcessNodeDataDto processNodeDataDto) {
        return post(processNodeDataDto,"/processNodeData/saveNodeData");
    }
}
