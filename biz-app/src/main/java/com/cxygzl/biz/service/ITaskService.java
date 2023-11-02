package com.cxygzl.biz.service;

import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;

/**
 * 任务处理
 */
public interface ITaskService {

    /**
     * 提交评论
     *
     * @param taskParamDto
     * @return
     */
    R submitComment(TaskParamDto taskParamDto);

    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    R completeTask(TaskParamDto taskParamDto);

    /**
     * 前加签
     *
     * @param taskParamDto
     * @return
     */
    R delegateTask(TaskParamDto taskParamDto);

    /**
     *  加签完成任务
     * @param taskParamDto
     * @return
     */
    R resolveTask(TaskParamDto taskParamDto);

    /**
     * 设置执行人
     *
     * @param taskParamDto
     * @return
     */
    R setAssignee(TaskParamDto taskParamDto);

    /**
     * 添加执行人
     * @param taskParamDto
     * @return
     */
    R addAssignee(TaskParamDto taskParamDto);

    /**
     * 减少执行人
     * @param taskParamDto
     * @return
     */
    R delAssignee(TaskParamDto taskParamDto);




    /**
     * 退回
     * @param taskParamDto
     * @return
     */
    R back(TaskParamDto taskParamDto);

    /**
     * 撤回
     * @param taskParamDto
     * @return
     */
    R revoke(TaskParamDto taskParamDto);

}
