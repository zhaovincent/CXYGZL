package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.CommonUtil;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;
import org.springframework.core.env.Environment;

import java.util.List;

public class CoreHttpUtil {
    private static TLogHutoolhttpInterceptor tLogHutoolhttpInterceptor = new TLogHutoolhttpInterceptor();

    public static String getBaseUrl() {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("core.url");
        return bizUrl;
    }


    public static String post(Object object, String url) {

        String bizUrl =getBaseUrl();


        String post = HttpRequest.post(StrUtil.format("{}{}", bizUrl, url)).body(JSON.toJSONString(object))
                .addInterceptor(tLogHutoolhttpInterceptor).execute().body();


        return post;
    }

    public static String get(String url) {

        String bizUrl = getBaseUrl();

        return HttpRequest.get(StrUtil.format("{}{}", bizUrl, url)).addInterceptor(tLogHutoolhttpInterceptor).execute().body();


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
        VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
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
    public static R<IndexPageStatistics> querySimpleData(String userId) {

        String s =get("/process-instance/querySimpleData?userId=" + userId );
        return JSON.parseObject(s, new TypeReference<R<IndexPageStatistics>>() {
        });

    }

    /**
     * 创建流程
     *
     * @param jsonObject
     * @return
     */
    public static com.cxygzl.common.dto.R<String> createFlow(Node jsonObject, String userId) {

        String post = post(jsonObject, "/flow/create?userId=" + userId);
        com.cxygzl.common.dto.R<String> r = JSON.parseObject(post, com.cxygzl.common.dto.R.class);
        return r;

    }

    /**
     * 启动流程
     *
     * @param jsonObject
     * @return
     */
    public static String startProcess(ProcessInstanceParamDto jsonObject) {

        return post(jsonObject, "/flow/start");

    }


    /**
     * 查询指派任务
     *
     * @param jsonObject
     * @return
     */
    public static com.cxygzl.common.dto.R<PageResultDto<TaskDto>> queryAssignTask(TaskQueryParamDto jsonObject) {

        String post = post(jsonObject, "/flow/queryAssignTask");

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
        });
        return r;

    }

    /**
     * 查询已办任务
     *
     * @param jsonObject
     * @return
     */
    public static  com.cxygzl.common.dto.R<PageResultDto<TaskDto>> queryCompletedTask(TaskQueryParamDto jsonObject) {

        String post = post(jsonObject, "/flow/queryCompletedTask");
        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
        });
        return r;

    }

    /**
     * 完成任务
     *
     * @param jsonObject
     * @return
     */
    public static R completeTask(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/task/complete");
        return CommonUtil.toObj(post,R.class);

    }

    /**
     * 转交
     *
     * @param jsonObject
     * @return
     */
    public static String setAssignee(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/setAssignee");

    }

    /**
     * 终止流程
     *
     * @param jsonObject
     * @return
     */
    public static  com.cxygzl.common.dto.R stopProcessInstance(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/flow/stopProcessInstance");
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        return r;

    }

    /**
     * 解决委派任务
     *
     * @param jsonObject
     * @return
     */
    public static String resolveTask(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/resolveTask");

    }

    /**
     * 退回
     *
     * @param jsonObject
     * @return
     */
    public static String back(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/back");

    }

    /**
     * 委派任务
     *
     * @param jsonObject
     * @return
     */
    public static R delegateTask(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/task/delegateTask");
        com.cxygzl.common.dto.R r=JSON.parseObject(post,R.class);

        return r;

    }

    /**
     * 显示图片
     *
     * @param procInsId
     * @return
     */
    public static String showImg(String procInsId) {

        return get("/flow/showImg?procInsId=" + procInsId);

    }

    /**
     * 查询任务
     *
     * @param taskId
     * @param userId
     * @return
     */
    public static  com.cxygzl.common.dto.R<TaskResultDto> queryTask(String taskId, String userId) {

        String s = get(StrUtil.format("/task/queryTask?taskId={}&userId={}", taskId, userId));
        com.cxygzl.common.dto.R<TaskResultDto> r = JSON.parseObject(s, new TypeReference<R<TaskResultDto>>() {
        });
        return r;

    }


}
