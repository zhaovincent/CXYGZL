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
     * 结束流程
     * @param taskParamDto
     * @return
     */
    Object stopProcessInstance(TaskParamDto taskParamDto);


}
