package com.cxygzl.biz.service;

import com.cxygzl.common.dto.TaskParamDto;

/**
 * 任务处理
 */
public interface ITaskService {
    /**
     * 查询任务
     * @param taskId
     * @return
     */
    Object queryTask(String taskId);

    /**
     * 完成任务
     * @param taskParamDto
     * @return
     */
    Object completeTask(TaskParamDto taskParamDto);

    /**
     * 前加签
     * @param taskParamDto
     * @return
     */
    Object delegateTask(TaskParamDto taskParamDto);

    /**
     *  加签完成任务
     * @param taskParamDto
     * @return
     */
    Object resolveTask(TaskParamDto taskParamDto);

    /**
     * 设置执行人
     * @param taskParamDto
     * @return
     */
    Object setAssignee(TaskParamDto taskParamDto);

    /**
     * 结束流程
     * @param taskParamDto
     * @return
     */
    Object stopProcessInstance(TaskParamDto taskParamDto);

    /**
     * 退回
     * @param taskParamDto
     * @return
     */
    Object back( TaskParamDto taskParamDto);

}
