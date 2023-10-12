package com.cxygzl.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.UserFieldDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.core.config.MqQueue;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class BizHttpUtil {



    public static String post(Object object, String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("biz.url");

        return HttpUtil.post(object, url, bizUrl);

    }


    public static <T> R<List<T>> postArray(Object object, String url) {
        String post = post(object, url);

        R<List<T>> r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<T>>>() {
        });
        return r;
    }

    public static String get(String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("biz.url");

        return HttpUtil.get(url,bizUrl);
    }


    /**
     * 节点开始事件
     *
     * @param nodeRecordParamDto
     */
    public static void startNodeEvent(ProcessInstanceNodeRecordParamDto nodeRecordParamDto) {

        MqQueue.put(nodeRecordParamDto, "/remote/startNodeEvent");


    }

    /**
     * 保存父子节点执行关系
     *
     * @param nodeRecordParamDto
     */
    public static void saveParentChildExecution(ProcessInstanceNodeRecordParamDto nodeRecordParamDto) {
        post(nodeRecordParamDto, "/remote/saveParentChildExecution");
    }

    /**
     * 节点结束事件
     *
     * @param nodeRecordParamDto
     */
    public static void endNodeEvent(ProcessInstanceNodeRecordParamDto nodeRecordParamDto) {
        MqQueue.put(nodeRecordParamDto, "/remote/endNodeEvent");
    }

    /**
     * 节点取消事件
     *
     * @param nodeRecordParamDto
     */
    public static void cancelNodeEvent(ProcessInstanceNodeRecordParamDto nodeRecordParamDto) {
        MqQueue.put(nodeRecordParamDto, "/remote/cancelNodeEvent");
    }

    /**
     * 查询部门列表
     *
     * @param deptIdList
     */
    public static List<DeptDto> queryDeptList(List<String> deptIdList) {
        String post = post(deptIdList, "/remote/queryDeptList");
        R<List<DeptDto>> listR = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<DeptDto>>>() {
        });
        return listR.getData();
    }

    /**
     * 保存消息
     *
     * @param messageDto
     */
    public static void saveMessage(MessageDto messageDto) {
        MqQueue.put(messageDto, "/remote/saveMessage");
    }

    /**
     * 流程结束事件
     *
     * @param processInstanceParamDto
     */
    public static void endProcessEvent(ProcessInstanceParamDto processInstanceParamDto) {
        MqQueue.put(processInstanceParamDto, "/remote/endProcess");
    }

    /**
     * 流程开始事件
     *
     * @param processInstanceParamDto
     */
    public static void startProcessEvent(ProcessInstanceRecordParamDto processInstanceParamDto) {
        MqQueue.put(processInstanceParamDto, "/remote/startProcessEvent");
    }

    /**
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     */
    public static R<List<String>> queryUserIdListByRoleIdList(List<String> roleIdList) {
        return postArray(roleIdList, "/remote/queryUserIdListByRoleIdList");
    }

    /**
     * 根据用户id查询角色id集合
     *
     * @param userId
     */
    public static List<String> queryRoleIdListByUserId(String userId) {
        String s = get("/remote/queryRoleIdListByUserId?userId=" + userId);
        return JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<List<String>>>() {
        }).getData();
    }

    /**
     * 根据部门id集合查询所有的用户id集合
     *
     * @param deptIdList
     */
    public static R<List<String>> queryUserIdListByDepIdList(List<String> deptIdList) {
        String s = post(deptIdList, "/remote/queryUserIdListByDepIdList");
        R<List<String>> r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<List<String>>>() {
        });
        return r;
    }

    /**
     * 根据用户id查询上级部门id集合
     *
     * @param userId 用户id
     */
    public static R<List<com.cxygzl.common.dto.third.DeptDto>> queryParentDepListByUserId(String userId) {
        String s = get("/remote/queryParentDepListByUserId?userId=" + userId);
        R<List<com.cxygzl.common.dto.third.DeptDto>> r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<List<DeptDto>>>() {
        });
        return r;
    }

    /**
     * 查询流程管理员
     *
     * @param flowId 流程id
     */
    public static R<String> queryProcessAdmin(String flowId) {
        String s = get("/remote/queryProcessAdmin?flowId=" + flowId);
        R<String> longR = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<String>>() {
        });
        return longR;
    }

    /**
     * 查询流程设置
     *
     * @param flowId 流程id
     */
    public static R<FlowSettingDto> queryProcessSetting(String flowId) {
        String s = get("/remote/queryProcessSetting?flowId=" + flowId);
        R<FlowSettingDto> longR = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<FlowSettingDto>>() {
        });
        return longR;
    }

    /**
     * 查询节点数据
     *
     * @param flowId
     * @param nodeId
     * @return
     */
    public static R<String> queryNodeOriData(String flowId, String nodeId) {
        String s = get(StrUtil.format("/remote/getNodeData?flowId" +
                "={}&nodeId={}", flowId, nodeId));
        R<String> r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<String>>() {
        });

        return r;
    }

    /**
     * 查询所有的用户信息
     *
     * @param userId
     * @return
     */
    public static R<Map<String, Object>> queryUserInfo(String userId) {
        String s = get("/remote/queryUserAllInfo?userId=" + userId);
        R<Map<String, Object>> mapR = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<Map<String, Object>>>() {
        });
        return mapR;
    }

    /**
     * 查询配置的用户属性
     *
     * @return
     */
    public static R<List<UserFieldDto>> queryUseField() {
        String s = get("/remote/queryUseField");
        R<List<UserFieldDto>> mapR = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<List<UserFieldDto>>>() {
        });
        return mapR;
    }

    /**
     * 节点开始指派用户了
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    public static void startAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        MqQueue.put(processInstanceAssignUserRecordParamDto, "/remote/startAssignUser");
    }

    /**
     * 任务完成事件
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    public static void taskCompletedEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        MqQueue.put(processInstanceAssignUserRecordParamDto, "/remote/taskCompletedEvent");
    }

    /**
     * 任务结束事件
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    public static void taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        MqQueue.put(processInstanceAssignUserRecordParamDto, "/remote/taskEndEvent");
    }


    /**
     * 保存抄送数据
     *
     * @param processCopyDto
     * @return
     */
    public static void saveCC(ProcessInstanceCopyDto processCopyDto) {
        post(processCopyDto, "/remote/savecc");
    }

    /**
     * 保存节点原始数据
     *
     * @param processNodeDataDto
     * @return
     */
    public static void saveNodeOriData(ProcessNodeDataDto processNodeDataDto) {
        post(processNodeDataDto, "/remote/saveNodeData");
    }
}
