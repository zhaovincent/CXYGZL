package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.mapper.ProcessNodeRecordAssignUserMapper;
import com.cxygzl.biz.service.IProcessNodeRecordAssignUserService;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

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


//    @Resource
//    private IProcessOperRecordService processOperRecordService;
//
//    @Resource
//    private IProcessNodeRecordApproveDescService processNodeRecordApproveDescService;

    /**
     * 设置执行人
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        {
            ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
                    .eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
                    .eq(ProcessNodeRecordAssignUser::getExecutionId, processNodeRecordAssignUserParamDto.getExecutionId())
//                    .eq(ProcessNodeRecordAssignUser::getUserId, processNodeRecordAssignUserParamDto.getUserId())
                    .orderByDesc(ProcessNodeRecordAssignUser::getId)
                    .last("limit 1").one();

            if (processNodeRecordAssignUser != null) {
                processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
                processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
                processNodeRecordAssignUser.setEndTime(new Date());
                this.updateById(processNodeRecordAssignUser);

//                List<SimpleApproveDescDto> simpleApproveDescDtoList = processNodeRecordAssignUserParamDto.getSimpleApproveDescDtoList();
//
//
//                saveApproveDescList(processNodeRecordAssignUser, simpleApproveDescDtoList);


                //修改任务
//                ProcessNodeRecordAssignUserParamDto p = BeanUtil.copyProperties(processNodeRecordAssignUserParamDto,
//                        ProcessNodeRecordAssignUserParamDto.class);
//                p.setUserId(processNodeRecordAssignUser.getUserId());
//                processOperRecordService.completeTask(p);


            }


        }


        ProcessNodeRecordAssignUser processNodeRecordAssignUser = BeanUtil.copyProperties(processNodeRecordAssignUserParamDto, ProcessNodeRecordAssignUser.class);
        processNodeRecordAssignUser.setStartTime(new Date());
        processNodeRecordAssignUser.setStatus(NodeStatusEnum.JXZ.getCode());
//        processNodeRecordAssignUser.setApproveDesc("");
        processNodeRecordAssignUser.setTaskType("");
        this.save(processNodeRecordAssignUser);


        //记录日志
      //  processOperRecordService.taskSetAssignee(processNodeRecordAssignUserParamDto);

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

//        List<SimpleApproveDescDto> simpleApproveDescDtoList = processNodeRecordAssignUserParamDto.getSimpleApproveDescDtoList();
//
//        saveApproveDescList(processNodeRecordAssignUser, simpleApproveDescDtoList);

        //记录日志

        // processOperRecordService.completeTask(processNodeRecordAssignUserParamDto);

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
        log.info("任务撤销:{} - {} -{}",processNodeRecordAssignUserParamDto.getNodeName(),
                processNodeRecordAssignUserParamDto.getUserId(),processNodeRecordAssignUserParamDto.getTaskType());
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

//        List<SimpleApproveDescDto> simpleApproveDescDtoList = processNodeRecordAssignUserParamDto.getSimpleApproveDescDtoList();
//
//        saveApproveDescList(processNodeRecordAssignUser, simpleApproveDescDtoList);

        //记录日志
//        processNodeRecordAssignUserParamDto.setUserId(processNodeRecordAssignUser.getUserId());
//        processOperRecordService.nodeCancel(processNodeRecordAssignUserParamDto);


        return R.success();
    }

//    private void saveApproveDescList(ProcessNodeRecordAssignUser processNodeRecordAssignUser, List<SimpleApproveDescDto> simpleApproveDescDtoList) {
//        if (CollUtil.isNotEmpty(simpleApproveDescDtoList)) {
//            for (SimpleApproveDescDto simpleApproveDescDto : simpleApproveDescDtoList) {
//
//                Long count = processNodeRecordApproveDescService.lambdaQuery().eq(ProcessNodeRecordApproveDesc::getDescId, simpleApproveDescDto.getMsgId()).count();
//                if (count > 0) {
//                    continue;
//                }
//
//
//                ProcessNodeRecordApproveDesc entity = new ProcessNodeRecordApproveDesc();
//                entity.setFlowId(processNodeRecordAssignUser.getFlowId());
//                entity.setProcessInstanceId(processNodeRecordAssignUser.getProcessInstanceId());
//                entity.setNodeId(processNodeRecordAssignUser.getNodeId());
//                entity.setUserId(processNodeRecordAssignUser.getUserId());
//                entity.setExecutionId(processNodeRecordAssignUser.getExecutionId());
//                entity.setTaskId(processNodeRecordAssignUser.getTaskId());
//                entity.setApproveDesc(simpleApproveDescDto.getMessage());
//                entity.setDescDate(simpleApproveDescDto.getDate());
//                entity.setDescId(simpleApproveDescDto.getMsgId());
//                entity.setDescType(simpleApproveDescDto.getType());
//                entity.setNodeName(processNodeRecordAssignUser.getNodeName());
//
//
//                processNodeRecordApproveDescService.save(entity);
//            }
//        }
//    }
}
