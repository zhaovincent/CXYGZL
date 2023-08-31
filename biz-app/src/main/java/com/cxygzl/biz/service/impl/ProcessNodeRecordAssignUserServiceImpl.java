package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.mapper.ProcessNodeRecordAssignUserMapper;
import com.cxygzl.biz.service.IProcessNodeRecordAssignUserService;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
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
public class ProcessNodeRecordAssignUserServiceImpl extends ServiceImpl<ProcessNodeRecordAssignUserMapper, ProcessNodeRecordAssignUser> implements IProcessNodeRecordAssignUserService {


    /**
     * 设置执行人
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        {
            List<ProcessNodeRecordAssignUser> list = this.lambdaQuery()
                    .eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
                    .eq(ProcessNodeRecordAssignUser::getExecutionId, processNodeRecordAssignUserParamDto.getExecutionId())
                    .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                    .orderByDesc(ProcessNodeRecordAssignUser::getCreateTime)
                    .list();

            if (!list.isEmpty()) {
                ProcessNodeRecordAssignUser processNodeRecordAssignUser = list.get(0);
                processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
                processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
                processNodeRecordAssignUser.setEndTime(new Date());
                this.updateById(processNodeRecordAssignUser);

                //处理任务
                List<com.cxygzl.common.dto.third.TaskParamDto> taskParamDtoList=new ArrayList<>();
                com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new com.cxygzl.common.dto.third.TaskParamDto();
                taskParamDto.setProcessInstanceId(processNodeRecordAssignUser.getProcessInstanceId());
                taskParamDto.setUserId(processNodeRecordAssignUser.getUserId());
                taskParamDto.setTaskId(processNodeRecordAssignUser.getTaskId());
                taskParamDtoList.add(taskParamDto);
                ApiStrategyFactory.getStrategy().handleTask(taskParamDtoList,processNodeRecordAssignUser.getTaskType());

            }


        }


        ProcessNodeRecordAssignUser processNodeRecordAssignUser = BeanUtil.copyProperties(processNodeRecordAssignUserParamDto, ProcessNodeRecordAssignUser.class);
        processNodeRecordAssignUser.setStartTime(new Date());
        processNodeRecordAssignUser.setStatus(NodeStatusEnum.JXZ.getCode());
        processNodeRecordAssignUser.setTaskType("");
        this.save(processNodeRecordAssignUser);

        //添加待办
        com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new TaskParamDto();
        taskParamDto.setProcessInstanceId(processNodeRecordAssignUser.getProcessInstanceId());
        taskParamDto.setNodeId(processNodeRecordAssignUser.getNodeId());
        taskParamDto.setTaskId(processNodeRecordAssignUser.getTaskId());
        taskParamDto.setUserId(processNodeRecordAssignUser.getUserId());
        ApiStrategyFactory.getStrategy().addWaitTask(CollUtil.newArrayList(taskParamDto));


        return R.success();
    }

    /**
     * 任务完成通知
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
                .eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
                .eq(ProcessNodeRecordAssignUser::getUserId, processNodeRecordAssignUserParamDto.getUserId())
                .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processNodeRecordAssignUserParamDto.getProcessInstanceId())
                .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                .one();
        processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
        processNodeRecordAssignUser.setEndTime(new Date());
        processNodeRecordAssignUser.setData(processNodeRecordAssignUserParamDto.getData());
        processNodeRecordAssignUser.setLocalData(processNodeRecordAssignUserParamDto.getLocalData());
        processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
        this.updateById(processNodeRecordAssignUser);

        //通知第三方
        com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new com.cxygzl.common.dto.third.TaskParamDto();
        taskParamDto.setProcessInstanceId(processNodeRecordAssignUser.getProcessInstanceId());
        taskParamDto.setUserId(processNodeRecordAssignUser.getUserId());
        taskParamDto.setTaskId(processNodeRecordAssignUser.getTaskId());

        ApiStrategyFactory.getStrategy().handleTask(CollUtil.newArrayList(taskParamDto), processNodeRecordAssignUser.getTaskType());


        return R.success();
    }

    /**
     * 驳回任务
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R taskCancelEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        log.info("任务撤销:{} - {} -{}", processNodeRecordAssignUserParamDto.getNodeName(),
                processNodeRecordAssignUserParamDto.getUserId(), processNodeRecordAssignUserParamDto.getTaskType());
        ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
                .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processNodeRecordAssignUserParamDto.getProcessInstanceId())
                .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                .eq(ProcessNodeRecordAssignUser::getExecutionId, processNodeRecordAssignUserParamDto.getExecutionId())
                .one();
        if (processNodeRecordAssignUser == null) {
            return R.success();
        }
        processNodeRecordAssignUser.setStatus(NodeStatusEnum.YCX.getCode());
        processNodeRecordAssignUser.setEndTime(new Date());
        processNodeRecordAssignUser.setData(processNodeRecordAssignUserParamDto.getData());
        processNodeRecordAssignUser.setLocalData(processNodeRecordAssignUserParamDto.getLocalData());
        processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
        this.updateById(processNodeRecordAssignUser);


            //处理任务
        List<com.cxygzl.common.dto.third.TaskParamDto> taskParamDtoList=new ArrayList<>();
        com.cxygzl.common.dto.third.TaskParamDto taskParamDto = new com.cxygzl.common.dto.third.TaskParamDto();
        taskParamDto.setProcessInstanceId(processNodeRecordAssignUser.getProcessInstanceId());
        taskParamDto.setUserId(processNodeRecordAssignUser.getUserId());
        taskParamDto.setTaskId(processNodeRecordAssignUser.getTaskId());
        taskParamDtoList.add(taskParamDto);
        ApiStrategyFactory.getStrategy().handleTask(taskParamDtoList,processNodeRecordAssignUser.getTaskType());

        return R.success();
    }

}
