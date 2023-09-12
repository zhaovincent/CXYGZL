package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceNodeRecord;
import com.cxygzl.biz.mapper.ProcessInstanceNodeRecordMapper;
import com.cxygzl.biz.service.IProcessInstanceNodeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
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
public class ProcessInstanceNodeRecordServiceImpl extends ServiceImpl<ProcessInstanceNodeRecordMapper, ProcessInstanceNodeRecord> implements IProcessInstanceNodeRecordService {
    /**
     * 节点开始
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public R start(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {

        ProcessInstanceNodeRecord processInstanceNodeRecord = BeanUtil.copyProperties(processInstanceNodeRecordParamDto, ProcessInstanceNodeRecord.class);
        processInstanceNodeRecord.setStartTime(new Date());
        processInstanceNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());

        this.save(processInstanceNodeRecord);
        return R.success();
    }

    /**
     * 节点结束
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public R complete(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {

        log.info("节点结束---{}", JSON.toJSONString(processInstanceNodeRecordParamDto));

        //TODO 完成节点和完成任务要区分下
        this.lambdaUpdate()
                .set(ProcessInstanceNodeRecord::getStatus,NodeStatusEnum.YJS.getCode())
                .set(ProcessInstanceNodeRecord::getEndTime,new Date())
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceNodeRecordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceNodeRecord::getNodeId, processInstanceNodeRecordParamDto.getNodeId()).update(new ProcessInstanceNodeRecord());
        return R.success();
    }
}
