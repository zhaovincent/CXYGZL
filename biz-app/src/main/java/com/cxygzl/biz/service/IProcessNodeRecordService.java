package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessNodeRecord;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.R;

/**
 * <p>
 * 流程节点记录 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessNodeRecordService extends IService<ProcessNodeRecord> {
    /**
     * 节点开始
     * @param processNodeRecordParamDto
     * @return
     */
    R start(ProcessNodeRecordParamDto processNodeRecordParamDto);

    /**
     * 节点结束
     * @param processNodeRecordParamDto
     * @return
     */
    R endNodeEvent(ProcessNodeRecordParamDto processNodeRecordParamDto);

    /**
     * 驳回
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    R rejectNodeEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);
}
