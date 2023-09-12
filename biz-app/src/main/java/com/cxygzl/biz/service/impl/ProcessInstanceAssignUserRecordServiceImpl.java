package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceAssignUserRecord;
import com.cxygzl.biz.mapper.ProcessInstanceAssignUserRecordMapper;
import com.cxygzl.biz.service.IProcessInstanceAssignUserRecordService;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.common.dto.R;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程节点记录-执行人 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Service
public class ProcessInstanceAssignUserRecordServiceImpl extends ServiceImpl<ProcessInstanceAssignUserRecordMapper, ProcessInstanceAssignUserRecord> implements IProcessInstanceAssignUserRecordService {
    /**
     * 设置执行人
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R addAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        if (StrUtil.isNotBlank(processInstanceAssignUserRecordParamDto.getApproveDesc())) {
            List<ProcessInstanceAssignUserRecord> list = this.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                    .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime)
                    .list();
            if (CollUtil.isNotEmpty(list)) {
                ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = list.get(0);
                processInstanceAssignUserRecord.setApproveDesc(processInstanceAssignUserRecordParamDto.getApproveDesc());
                processInstanceAssignUserRecord.setTaskType(processInstanceAssignUserRecordParamDto.getTaskType());
                processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YJS.getCode());
                processInstanceAssignUserRecord.setEndTime(new Date());
                this.updateById(processInstanceAssignUserRecord);
            }

        }

        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = BeanUtil.copyProperties(processInstanceAssignUserRecordParamDto, ProcessInstanceAssignUserRecord.class);
        processInstanceAssignUserRecord.setStartTime(new Date());
        processInstanceAssignUserRecord.setStatus(NodeStatusEnum.JXZ.getCode());
        processInstanceAssignUserRecord.setApproveDesc("");
        processInstanceAssignUserRecord.setTaskType("");
        this.save(processInstanceAssignUserRecord);

        return R.success();
    }

    /**
     * 任务完成通知
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R completeTaskEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        List<ProcessInstanceAssignUserRecord> list = this.lambdaQuery()
                .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                .eq(ProcessInstanceAssignUserRecord::getUserId, processInstanceAssignUserRecordParamDto.getUserId())
                .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceAssignUserRecordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime)
                .list();
        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = list.get(0);
        processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YJS.getCode());
        processInstanceAssignUserRecord.setApproveDesc(processInstanceAssignUserRecordParamDto.getApproveDesc());
        processInstanceAssignUserRecord.setEndTime(new Date());
        processInstanceAssignUserRecord.setData(processInstanceAssignUserRecordParamDto.getData());
        processInstanceAssignUserRecord.setLocalData(processInstanceAssignUserRecordParamDto.getLocalData());
        processInstanceAssignUserRecord.setTaskType("COMPLETE");
        this.updateById(processInstanceAssignUserRecord);
        return R.success();
    }
}
