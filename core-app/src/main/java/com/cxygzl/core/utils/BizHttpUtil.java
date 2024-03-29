package com.cxygzl.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;
import org.springframework.core.env.Environment;

import java.util.List;

public class BizHttpUtil {

    private static TLogHutoolhttpInterceptor tLogHutoolhttpInterceptor = new TLogHutoolhttpInterceptor();


    public static String post(Object object, String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("biz.url");


        String post = HttpRequest.post(StrUtil.format("{}{}", bizUrl, url)).body(JSON.toJSONString(object))
                .addInterceptor(tLogHutoolhttpInterceptor).execute().body();


        return post;
    }


    public static <T> R<List<T>> postArray(Object object, String url) {
        String post = post(object, url);

        R<List<T>> r = JSON.parseObject(post, new TypeReference<R<List<T>>>() {
        });
        return r;
    }

    public static String get(String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("biz.url");

        return HttpRequest.get(StrUtil.format("{}{}", bizUrl, url)).addInterceptor(tLogHutoolhttpInterceptor).execute().body();


    }


    /**
     * 节点开始事件
     *
     * @param nodeRecordParamDto
     */
    public static void startNodeEvent(ProcessInstanceNodeRecordParamDto nodeRecordParamDto) {
        post(nodeRecordParamDto, "/remote/startNodeEvent");
    }

    /**
     * 节点结束事件
     *
     * @param nodeRecordParamDto
     */
    public static void endNodeEvent(ProcessInstanceNodeRecordParamDto nodeRecordParamDto) {
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
     * 保存消息
     *
     * @param messageDto
     */
    public static void saveMessage(MessageDto messageDto) {
        post(messageDto, "/remote/saveMessage");
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
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     */
    public static R<List<String>> queryUserIdListByRoleIdList(List<String> roleIdList) {
        return postArray(roleIdList, "/remote/queryUserIdListByRoleIdList");
    }

    /**
     * 根据部门id集合查询所有的用户id集合
     *
     * @param deptIdList
     */
    public static R<List<String>> queryUserIdListByDepIdList(List<String> deptIdList) {
        String s = post(deptIdList, "/remote/queryUserIdListByDepIdList");
        R<List<String>> r = JSON.parseObject(s, new TypeReference<R<List<String>>>() {
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
        R<List<com.cxygzl.common.dto.third.DeptDto>> r = JSON.parseObject(s, new TypeReference<R<List<DeptDto>>>() {
        });
        return r;
    }
    /**
     * 查询部门列表
     *
     * @param deptIdList
     */
    public static List<DeptDto> queryDeptList(List<String> deptIdList) {
        String post = post(deptIdList, "/remote/queryDeptList");
        R<List<DeptDto>> listR = JSON.parseObject(post, new TypeReference<R<List<DeptDto>>>() {
        });
        return listR.getData();
    }

    /**
     * 查询流程管理员
     *
     * @param flowId 流程id
     */
    public static R<String> queryProcessAdmin(String flowId) {
        String s = get("/remote/queryProcessAdmin?flowId=" + flowId);
        R<String> longR = JSON.parseObject(s, new TypeReference<R<String>>() {
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
        String s = get(StrUtil.format("/processNodeData/getNodeData?flowId" +
                "={}&nodeId={}", flowId, nodeId));
        R<String> r = JSON.parseObject(s, new TypeReference<R<String>>() {
        });

        return r;
    }

    /**
     * 节点开始指派用户了
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    public static void startAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        post(processInstanceAssignUserRecordParamDto, "/remote/startAssignUser");
    }

    /**
     * 任务结束事件
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    public static void taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        post(processInstanceAssignUserRecordParamDto, "/remote/taskEndEvent");
    }

    /**
     * 保存抄送数据
     *
     * @param processInstanceCopyDto
     * @return
     */
    public static void saveCC(ProcessInstanceCopyDto processInstanceCopyDto) {
        post(processInstanceCopyDto, "/remote/savecc");
    }

    /**
     * 保存节点原始数据
     *
     * @param processNodeDataDto
     * @return
     */
    public static void saveNodeOriData(ProcessNodeDataDto processNodeDataDto) {
        post(processNodeDataDto, "/processNodeData/saveNodeData");
    }
}
