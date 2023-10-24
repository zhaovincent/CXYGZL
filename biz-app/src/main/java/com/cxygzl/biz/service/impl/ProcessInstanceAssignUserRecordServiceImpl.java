package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceAssignUserRecord;
import com.cxygzl.biz.mapper.ProcessInstanceAssignUserRecordMapper;
import com.cxygzl.biz.service.IProcessInstanceAssignUserRecordService;
import com.cxygzl.common.dto.ProcessInstanceAssignUserRecordParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.TaskParamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Slf4j
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
        {
            List<ProcessInstanceAssignUserRecord> list = this.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                    .eq(ProcessInstanceAssignUserRecord::getExecutionId, processInstanceAssignUserRecordParamDto.getExecutionId())
                    .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                    .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime)
                    .list();

            if (!list.isEmpty()) {
                ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = list.get(0);
                processInstanceAssignUserRecord.setTaskType(processInstanceAssignUserRecordParamDto.getTaskType());
                processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YJS.getCode());
                processInstanceAssignUserRecord.setEndTime(new Date());
                this.updateById(processInstanceAssignUserRecord);

                //处理任务
                List<com.cxygzl.common.dto.third.TaskParamDto> taskParamDtoList=new ArrayList<>();
                com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new com.cxygzl.common.dto.third.TaskParamDto();
                taskParamDto.setProcessInstanceId(processInstanceAssignUserRecord.getProcessInstanceId());
                taskParamDto.setUserId(processInstanceAssignUserRecord.getUserId());
                taskParamDto.setTaskId(processInstanceAssignUserRecord.getTaskId());
                taskParamDtoList.add(taskParamDto);
                ApiStrategyFactory.getStrategy().handleTask(taskParamDtoList, processInstanceAssignUserRecord.getTaskType());

            }


        }


        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = BeanUtil.copyProperties(processInstanceAssignUserRecordParamDto, ProcessInstanceAssignUserRecord.class);
        processInstanceAssignUserRecord.setStartTime(new Date());
        processInstanceAssignUserRecord.setStatus(NodeStatusEnum.JXZ.getCode());
        processInstanceAssignUserRecord.setTaskType("");
        this.save(processInstanceAssignUserRecord);

        //添加待办
        com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new TaskParamDto();
        taskParamDto.setProcessInstanceId(processInstanceAssignUserRecord.getProcessInstanceId());
        taskParamDto.setNodeId(processInstanceAssignUserRecord.getNodeId());
        taskParamDto.setTaskId(processInstanceAssignUserRecord.getTaskId());
        taskParamDto.setUserId(processInstanceAssignUserRecord.getUserId());
        taskParamDto.setFlowId(processInstanceAssignUserRecord.getFlowId());
        ApiStrategyFactory.getStrategy().addWaitTask(CollUtil.newArrayList(taskParamDto));


        return R.success();
    }

    /**
     * 任务完成通知
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R taskCompletedEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = this.lambdaQuery()
                .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                .eq(ProcessInstanceAssignUserRecord::getUserId, processInstanceAssignUserRecordParamDto.getUserId())
                .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceAssignUserRecordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .one();
        processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YJS.getCode());
        processInstanceAssignUserRecord.setEndTime(new Date());
        processInstanceAssignUserRecord.setData(processInstanceAssignUserRecordParamDto.getData());
        processInstanceAssignUserRecord.setLocalData(processInstanceAssignUserRecordParamDto.getLocalData());
        processInstanceAssignUserRecord.setTaskType(processInstanceAssignUserRecordParamDto.getTaskType());
        this.updateById(processInstanceAssignUserRecord);

        //通知第三方
        com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new com.cxygzl.common.dto.third.TaskParamDto();
        taskParamDto.setProcessInstanceId(processInstanceAssignUserRecord.getProcessInstanceId());
        taskParamDto.setUserId(processInstanceAssignUserRecord.getUserId());
        taskParamDto.setTaskId(processInstanceAssignUserRecord.getTaskId());

        ApiStrategyFactory.getStrategy().handleTask(CollUtil.newArrayList(taskParamDto), processInstanceAssignUserRecord.getTaskType());


        return R.success();
    }

    /**
     * 任务结束
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = this.lambdaQuery()
                .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                .eq(ProcessInstanceAssignUserRecord::getUserId, processInstanceAssignUserRecordParamDto.getUserId())
                .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceAssignUserRecordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .one();
        if(processInstanceAssignUserRecord !=null) {
            processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YCX.getCode());
            processInstanceAssignUserRecord.setEndTime(new Date());
            processInstanceAssignUserRecord.setData(processInstanceAssignUserRecordParamDto.getData());
            processInstanceAssignUserRecord.setLocalData(processInstanceAssignUserRecordParamDto.getLocalData());
            processInstanceAssignUserRecord.setTaskType(processInstanceAssignUserRecordParamDto.getTaskType());
            this.updateById(processInstanceAssignUserRecord);

            //通知第三方
            com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new com.cxygzl.common.dto.third.TaskParamDto();
            taskParamDto.setProcessInstanceId(processInstanceAssignUserRecord.getProcessInstanceId());
            taskParamDto.setUserId(processInstanceAssignUserRecord.getUserId());
            taskParamDto.setTaskId(processInstanceAssignUserRecord.getTaskId());

            ApiStrategyFactory.getStrategy().handleTask(CollUtil.newArrayList(taskParamDto), processInstanceAssignUserRecord.getTaskType());
        }

        return R.success();
    }


}
