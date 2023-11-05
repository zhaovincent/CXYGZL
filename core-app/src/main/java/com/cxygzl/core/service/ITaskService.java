package com.cxygzl.core.service;

import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.VariableQueryParamDto;

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
     *
     * @param taskParamDto
     * @return
     */
    R delegateTask(TaskParamDto taskParamDto);

    /**
     * 加签任务完成
     *
     * @param taskParamDto
     * @return
     */
    R resolveTask(TaskParamDto taskParamDto);

    /**
     * 转交
     *
     * @param taskParamDto
     * @return
     */
    R setAssignee(TaskParamDto taskParamDto);

    /**
     * 删除执行人
     *
     * @param taskParamDto
     * @return
     */
    R delAssignee(TaskParamDto taskParamDto);

    /**
     * 添加执行人
     *
     * @param taskParamDto
     * @return
     */
    R addAssignee(TaskParamDto taskParamDto);

    /**
     * 驳回
     *
     * @param taskParamDto
     * @return
     */
    R back(TaskParamDto taskParamDto);

    /**
     * 撤回
     *
     * @param taskParamDto
     * @return
     */
    R revoke(TaskParamDto taskParamDto);

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
     * 查询流程实例评论
     *
     * @param processInstanceId
     * @return
     */
    R queryProcessInstanceComments(String processInstanceId);

    /**
     * 查询任务变量
     *
     * @param paramDto
     * @return
     */
    R queryTaskVariables(VariableQueryParamDto paramDto);

    /**
     * 提交评论
     *
     * @param taskParamDto
     * @return
     */
    R submitComment(TaskParamDto taskParamDto);
}
