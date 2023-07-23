package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.common.dto.R;

/**
 * <p>
 * 流程节点记录-执行人 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessNodeRecordAssignUserService extends IService<ProcessNodeRecordAssignUser> {
    /**
     * 设置执行人
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

    /**
     * 任务完成通知
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    R completeTaskEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);


}
