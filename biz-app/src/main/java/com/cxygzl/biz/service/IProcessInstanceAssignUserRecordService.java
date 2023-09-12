package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessInstanceAssignUserRecord;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.common.dto.R;

/**
 * <p>
 * 流程节点记录-执行人 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessInstanceAssignUserRecordService extends IService<ProcessInstanceAssignUserRecord> {
    /**
     * 设置执行人
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    R addAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto);

    /**
     * 任务完成通知
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    R completeTaskEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto);


}
