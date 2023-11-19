package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.HttpUtil;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public class CoreHttpUtil {

    public static String getBaseUrl() {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("core.url");
        return bizUrl;
    }


    public static String post(Object object, String url) {

        String baseUrl = getBaseUrl();

        return HttpUtil.post(object, url, baseUrl);


    }

    public static String get(String url) {

        String baseUrl = getBaseUrl();

        return HttpUtil.get(url, baseUrl);


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
     * 查询任务评论
     * 全部都是
     *
     * @param taskId
     * @return
     */
    public static R<List<SimpleApproveDescDto>> queryTaskComments(String taskId) {
        VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
        variableQueryParamDto.setTaskId(taskId);

        String post = post(variableQueryParamDto, "/task/queryTaskComments");
        R<List<SimpleApproveDescDto>> listR = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<SimpleApproveDescDto>>>() {
        });
        return listR;

    }

    /**
     * 查询流程评论
     * 全部都是
     *
     * @param processInstanceId
     * @return
     */
    public static R<List<SimpleApproveDescDto>> queryProcessInstanceComments(String processInstanceId) {


        String post = get("/task/queryProcessInstanceComments?processInstanceId="+processInstanceId);
        R<List<SimpleApproveDescDto>> listR = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<SimpleApproveDescDto>>>() {
        });
        return listR;

    }

    /**
     * queryTaskAssignee
     *
     * @param nodeId
     * @param processInstanceId
     * @return
     */
    public static R<List<TaskDto>> queryTaskAssignee(String nodeId,String processInstanceId) {
        TaskParamDto variableQueryParamDto = new TaskParamDto();
        variableQueryParamDto.setNodeId(nodeId);
        variableQueryParamDto.setProcessInstanceId(processInstanceId);

        String post = post(variableQueryParamDto, "/flow/queryTaskAssignee");
        R<List<TaskDto>> listR = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<List<TaskDto>>>() {
        });
        return listR;

    }

    /**
     * 查询流程变量
     * 全部都是
     *
     * @return
     */
    public static R<IndexPageStatistics> querySimpleData(String userId) {

        String s = get("/process-instance/querySimpleData?userId=" + userId);
        return JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<IndexPageStatistics>>() {
        });

    }
    /**
     * 查询流程表单数据
     *
     * @return
     */
    public static R<Map<String, Object>> queryVariables(VariableQueryParamDto variableQueryParamDto) {

        String s = post(variableQueryParamDto,"/process-instance/queryVariables"  );
        return JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<Map<String, Object>>>() {
        });

    }

    /**
     * 创建流程
     *
     * @param node
     * @param processName
     * @return
     */
    public static com.cxygzl.common.dto.R<String> createFlow(Node node, String userId,String processName) {
        CreateFlowDto createFlowDto=new CreateFlowDto();
        createFlowDto.setUserId(userId);
        createFlowDto.setNode(node);
        createFlowDto.setProcessName(processName);

        String post = post(createFlowDto, "/flow/create");
        com.cxygzl.common.dto.R<String> r = JsonUtil.parseObject(post, com.cxygzl.common.dto.R.class);
        return r;

    }

    /**
     * 启动流程
     *
     * @param jsonObject
     * @return
     */
    public static R<String> startProcess(ProcessInstanceParamDto jsonObject) {

        String post = post(jsonObject, "/flow/start");
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R<String>>() {
        });

    }

    /**
     * 通知消息事件继续执行
     *
     * @param jsonObject
     * @return
     */
    public static R notifyMsgEvent(NotifyMessageDto jsonObject) {

        String post = post(jsonObject, "/flow/notifyMsg");
        return JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
        });
    }


    /**
     * 查询指派任务
     *
     * @param jsonObject
     * @return
     */
    public static com.cxygzl.common.dto.R<PageResultDto<TaskDto>> queryTodoTask(TaskQueryParamDto jsonObject) {

        String post = post(jsonObject, "/flow/queryTodoTask");

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
        });
        return r;

    }

    /**
     * 查询已办任务
     *
     * @param jsonObject
     * @return
     */
    public static com.cxygzl.common.dto.R<PageResultDto<TaskDto>> queryCompletedTask(TaskQueryParamDto jsonObject) {

        String post = post(jsonObject, "/flow/queryCompletedTask");
        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
        });
        return r;

    }

    /**
     * 查询已办任务的流程实例
     *
     * @param jsonObject
     * @return
     */
    public static com.cxygzl.common.dto.R<PageResultDto<ProcessInstanceDto>> queryCompletedProcessInstance(ProcessQueryParamDto jsonObject) {

        String post = post(jsonObject, "/flow/queryCompletedProcessInstance");
        com.cxygzl.common.dto.R<PageResultDto<ProcessInstanceDto>> r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<com.cxygzl.common.dto.R<PageResultDto<ProcessInstanceDto>>>() {
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
        return JsonUtil.parseObject(post, R.class);

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
     * 转交--管理员
     *
     * @param jsonObject
     * @return
     */
    public static R setAssigneeByAdmin(AdminHandOverDto jsonObject) {

        String post = post(jsonObject, "/task/setAssigneeByAdmin");
        return JsonUtil.parseObject(post,R.class);

    }

    /**
     * 加签
     *
     * @param jsonObject
     * @return
     */
    public static String addAssignee(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/addAssignee");

    }

    /**
     * 减签
     *
     * @param jsonObject
     * @return
     */
    public static String delAssignee(TaskParamDto jsonObject) {

        return post(jsonObject, "/task/delAssignee");

    }

    /**
     * 终止流程
     *
     * @param jsonObject
     * @return
     */
    public static com.cxygzl.common.dto.R stopProcessInstance(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/flow/stopProcessInstance");
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
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
     * 撤回
     *
     * @param jsonObject
     * @return
     */
    public static R revoke(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/task/revoke");
        return JsonUtil.parseObject(post,R.class);

    }
    /**
     * 提交评论
     *
     * @param jsonObject
     * @return
     */
    public static R submitComment(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/task/submitComment");
        return JsonUtil.parseObject(post,R.class);

    }

    /**
     * 委派任务
     *
     * @param jsonObject
     * @return
     */
    public static R delegateTask(TaskParamDto jsonObject) {

        String post = post(jsonObject, "/task/delegateTask");
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, R.class);

        return r;

    }


    /**
     * 查询任务
     *
     * @param taskId
     * @param userId
     * @return
     */
    public static com.cxygzl.common.dto.R<TaskResultDto> queryTask(String taskId, String userId) {

        String s = get(StrUtil.format("/task/queryTask?taskId={}&userId={}", taskId, userId));
        com.cxygzl.common.dto.R<TaskResultDto> r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R<TaskResultDto>>() {
        });
        return r;

    }


    /**
     * 删除流程
     * @param processInstanceParamDto
     * @return
     */
    public static com.cxygzl.common.dto.R deleteProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

        String s = post(processInstanceParamDto,StrUtil.format("/process-instance/delete"));
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(s, new JsonUtil.TypeReference<R>() {
        });
        return r;

    }


}
