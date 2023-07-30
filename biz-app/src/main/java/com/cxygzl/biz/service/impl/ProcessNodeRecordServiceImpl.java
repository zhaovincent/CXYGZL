package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessNodeRecord;
import com.cxygzl.biz.mapper.ProcessNodeRecordMapper;
import com.cxygzl.biz.service.IProcessNodeRecordService;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 流程节点记录 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Slf4j
@Service
public class ProcessNodeRecordServiceImpl extends ServiceImpl<ProcessNodeRecordMapper, ProcessNodeRecord> implements IProcessNodeRecordService {
    /**
     * 节点开始
     *
     * @param processNodeRecordParamDto
     * @return
     */
    @Override
    public R start(ProcessNodeRecordParamDto processNodeRecordParamDto) {

        ProcessNodeRecord processNodeRecord = BeanUtil.copyProperties(processNodeRecordParamDto, ProcessNodeRecord.class);
        processNodeRecord.setStartTime(new Date());
        processNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());

        this.save(processNodeRecord);
        return R.success();
    }

    /**
     * 节点结束
     *
     * @param processNodeRecordParamDto
     * @return
     */
    @Override
    public R endNodeEvent(ProcessNodeRecordParamDto processNodeRecordParamDto) {


        this.lambdaUpdate()
                .set(ProcessNodeRecord::getStatus,NodeStatusEnum.YJS.getCode())
                .set(ProcessNodeRecord::getEndTime,new Date())
                .eq(ProcessNodeRecord::getProcessInstanceId, processNodeRecordParamDto.getProcessInstanceId())
                .eq(ProcessNodeRecord::getNodeId, processNodeRecordParamDto.getNodeId())
                .eq(ProcessNodeRecord::getExecutionId, processNodeRecordParamDto.getExecutionId())
                .update(new ProcessNodeRecord());
        return R.success();
    }
}
