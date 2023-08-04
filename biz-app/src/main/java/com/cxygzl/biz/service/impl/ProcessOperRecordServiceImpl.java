package com.cxygzl.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessOperRecord;
import com.cxygzl.biz.mapper.ProcessOperRecordMapper;
import com.cxygzl.biz.service.IProcessOperRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessInstanceRecordParamDto;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.common.dto.third.UserDto;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 流程操作记录 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-07-30
 */
@Service
public class ProcessOperRecordServiceImpl extends ServiceImpl<ProcessOperRecordMapper, ProcessOperRecord> implements IProcessOperRecordService {

    /**
     * 开始流程
     *
     * @param processInstanceRecordParamDto
     */
    @Override
    public void startProcessInstance(ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(processInstanceRecordParamDto.getUserId());

        ProcessOperRecord processOperRecord = new ProcessOperRecord();
        processOperRecord.setFlowId(processInstanceRecordParamDto.getFlowId());
        processOperRecord.setProcessInstanceId(processInstanceRecordParamDto.getProcessInstanceId());
        processOperRecord.setData(processInstanceRecordParamDto.getFormData());
        processOperRecord.setNodeId(ProcessInstanceConstant.VariableKey.STARTER);
//        processOperRecord.setTaskId();
        processOperRecord.setNodeName("发起人");
        processOperRecord.setStatus(NodeStatusEnum.YJS.getCode());
        processOperRecord.setStartTime(new Date());
        processOperRecord.setEndTime(new Date());
//        processOperRecord.setExecutionId();
        processOperRecord.setUserId(processInstanceRecordParamDto.getUserId());
        processOperRecord.setRemark(StrUtil.format("[{}]发起了流程", user.getName()));


        this.save(processOperRecord);
    }

    /**
     * 完成任务
     *
     * @param processNodeRecordAssignUserParamDto
     */
    @Override
    public void completeTask(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(processNodeRecordAssignUserParamDto.getUserId());

        String taskType = processNodeRecordAssignUserParamDto.getTaskType();

        ProcessOperRecord processOperRecord = new ProcessOperRecord();
        processOperRecord.setFlowId(processNodeRecordAssignUserParamDto.getFlowId());
        processOperRecord.setProcessInstanceId(processNodeRecordAssignUserParamDto.getProcessInstanceId());
        processOperRecord.setData(processNodeRecordAssignUserParamDto.getData());
        processOperRecord.setNodeId(processNodeRecordAssignUserParamDto.getNodeId());
        processOperRecord.setTaskId(processNodeRecordAssignUserParamDto.getTaskId());
        processOperRecord.setNodeName(processNodeRecordAssignUserParamDto.getNodeName());
        processOperRecord.setStatus(NodeStatusEnum.YJS.getCode());
        processOperRecord.setStartTime(new Date());
        processOperRecord.setEndTime(new Date());
        processOperRecord.setExecutionId(processNodeRecordAssignUserParamDto.getExecutionId());
        processOperRecord.setUserId(processNodeRecordAssignUserParamDto.getUserId());
        processOperRecord.setRemark(StrUtil.format("[{}]提交了任务[{}]", user.getName(),
                processNodeRecordAssignUserParamDto.getNodeName()));




        this.save(processOperRecord);
    }
}
