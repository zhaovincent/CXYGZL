package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessInstanceNodeRecord;
import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.common.dto.R;

/**
 * <p>
 * 流程节点记录 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessInstanceNodeRecordService extends IService<ProcessInstanceNodeRecord> {
    /**
     * 节点开始
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    R start(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto);

    /**
     * 节点结束
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    R endNodeEvent(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto);

    /**
     * 节点取消
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    R cancelNodeEvent(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto);
}
