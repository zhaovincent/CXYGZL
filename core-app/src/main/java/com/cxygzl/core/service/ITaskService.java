package com.cxygzl.core.service;

import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.VariableQueryParamDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-16 16:59
 */
public interface ITaskService {
    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    R complete(TaskParamDto taskParamDto);

    /**
     * 委派任务
     * @param taskParamDto
     * @return
     */
    R delegateTask(@RequestBody TaskParamDto taskParamDto);

    /**
     * 加签任务完成
     * @param taskParamDto
     * @return
     */
    R resolveTask(@RequestBody TaskParamDto taskParamDto);

    /**
     * 转交
     * @param taskParamDto
     * @return
     */
    R setAssignee(@RequestBody TaskParamDto taskParamDto);

    /**
     * 删除执行人
     * @param taskParamDto
     * @return
     */
    R delAssignee(@RequestBody TaskParamDto taskParamDto);

    /**
     * 添加执行人
     * @param taskParamDto
     * @return
     */
    R addAssignee(@RequestBody TaskParamDto taskParamDto);

    /**
     * 驳回
     * @param taskParamDto
     * @return
     */
    R back(@RequestBody TaskParamDto taskParamDto);

    /**
     * 撤回
     * @param taskParamDto
     * @return
     */
    R revoke(@RequestBody TaskParamDto taskParamDto);
    /**
     * 查询任务
     *
     * @param taskId
     * @param userId
     * @return
     */
    R queryTask(String taskId, String userId);

    /**
     * 查询任务评论
     *
     * @param paramDto
     * @return
     */
    R queryTaskComments(VariableQueryParamDto paramDto);

    /**
     * 查询任务变量
     * @param paramDto
     * @return
     */
    R queryTaskVariables(@RequestBody VariableQueryParamDto paramDto);
}