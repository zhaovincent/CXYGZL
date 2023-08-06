package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.ProcessOperRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.common.dto.ProcessInstanceRecordParamDto;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;

/**
 * <p>
 * 流程操作记录 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-07-30
 */
public interface IProcessOperRecordService extends IService<ProcessOperRecord> {
    /**
     * 开始流程
     * @param processInstanceRecordParamDto
     */
    void startProcessInstance(ProcessInstanceRecordParamDto processInstanceRecordParamDto);
    /**
     * 完成任务
     * @param processInstanceRecordParamDto
     */
    void completeTask(ProcessNodeRecordAssignUserParamDto processInstanceRecordParamDto);

    /**
     * 任务指派人了
     * @param processInstanceRecordParamDto
     */

    void taskSetAssignee(ProcessNodeRecordAssignUserParamDto processInstanceRecordParamDto);

    /**
     * 节点移动驳回了
     * @param processNodeRecordAssignUserParamDto
     */
    void nodeCancel(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

}
