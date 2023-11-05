package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessInstanceOperRecord;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-11-03 17:45
 */
public interface IProcessInstanceOperRecordService extends IService<ProcessInstanceOperRecord> {

    /**
     * 保存记录
     *
     * @param userId
     * @param taskParamDto
     * @param operType
     * @param desc
     * @return
     */
    R saveRecord(String userId, TaskParamDto taskParamDto,String operType,String desc);

    /**
     * 撤销流程
     * @param userId
     * @param processInstanceId
     * @return
     */
    R saveCancelProcessRecord(String userId,String processInstanceId);

    /**
     * 发起流程
     *
     * @param userId
     * @param processInstanceId
     * @param flowId
     * @return
     */
    R saveStartProcessRecord(String userId,String processInstanceId,String flowId);

}
