package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskQueryParamDto;
import com.cxygzl.common.dto.VariableQueryParamDto;
import org.springframework.core.env.Environment;

import java.util.List;

public class CoreHttpUtil {

    public static String post(Object object, String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("core.url");


        return HttpUtil.post(StrUtil.format("{}{}", bizUrl, url), JSON.toJSONString(object));
    }

    public static String get(String url) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("core.url");


        return HttpUtil.get(StrUtil.format("{}{}", bizUrl, url));
    }

    /**
     * 查询任务变量
     * 全部都是
     *
     * @param taskId
     * @param keyList
     * @return
     */
    public static String queryTaskVariables(String taskId, List<String> keyList) {
        VariableQueryParamDto variableQueryParamDto=new VariableQueryParamDto();
        variableQueryParamDto.setKeyList(keyList);
        variableQueryParamDto.setTaskId(taskId);

        return post(variableQueryParamDto, "/task/queryTaskVariables");

    }

    /**
     * 查询流程变量
     * 全部都是
     *
     * @return
     */
    public static String queryProcessInstanceVariables(String executionId) {
        VariableQueryParamDto variableQueryParamDto=new VariableQueryParamDto();
        variableQueryParamDto.setExecutionId(executionId);
        return post(variableQueryParamDto, "/process-instance/queryVariables");

    }

    /**
     * 创建流程
     * @param jsonObject
     * @return
     */
    public static String createProcess(JSONObject jsonObject,long userId) {

        return post(jsonObject, "/flow/create?userId="+userId);

    }

    /**
     * 启动流程
     * @param jsonObject
     * @return
     */
    public static String startProcess(ProcessInstanceParamDto jsonObject) {

        return post(jsonObject, "/flow/start");

    }


    /**
     * 查询指派任务
     * @param jsonObject
     * @return
     */
    public static String queryAssignTask(TaskQueryParamDto jsonObject) {

        return post(jsonObject, "/flow/queryAssignTask");

    }
    /**
     * 查询已办任务
     * @param jsonObject
     * @return
     */
    public static String queryCompletedTask(TaskQueryParamDto jsonObject) {

        return post(jsonObject, "/flow/queryCompletedTask");

    }

    /**
     * 完成任务
     * @param jsonObject
     * @return
     */
    public static String completeTask(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/complete");

    }

    /**
     * 转交
     * @param jsonObject
     * @return
     */
    public static String setAssignee(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/setAssignee");

    }

    /**
     * 终止流程
     * @param jsonObject
     * @return
     */
    public static String stopProcessInstance(TaskParamDto jsonObject) {

        return post(jsonObject, "/flow/stopProcessInstance");

    }

    /**
     * 解决委派任务
     * @param jsonObject
     * @return
     */
    public static String resolveTask(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/resolveTask");

    }


    /**
     * 委派任务
     * @param jsonObject
     * @return
     */
    public static String delegateTask(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/delegateTask");

    }

    /**
     * 显示图片
     * @param procInsId
     * @return
     */
    public static String showImg(String procInsId) {

        return get( "/flow/showImg?procInsId="+procInsId);

    }

    /**
     * 查询任务
     * @param taskId
     * @param userId
     * @return
     */
    public static String queryTask(String taskId,long userId) {

        return get(StrUtil.format("/task/queryTask?taskId={}&userId={}", taskId, userId));

    }



}
