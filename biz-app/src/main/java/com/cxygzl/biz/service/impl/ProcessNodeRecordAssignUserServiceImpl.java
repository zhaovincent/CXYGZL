package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.mapper.ProcessNodeRecordAssignUserMapper;
import com.cxygzl.biz.service.IProcessNodeRecordAssignUserService;
import com.cxygzl.common.dto.ProcessNodeRecordAssignUserParamDto;
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
public class ProcessNodeRecordAssignUserServiceImpl extends ServiceImpl<ProcessNodeRecordAssignUserMapper, ProcessNodeRecordAssignUser> implements IProcessNodeRecordAssignUserService {
    /**
     * 设置执行人
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        if (StrUtil.isNotBlank(processNodeRecordAssignUserParamDto.getApproveDesc())) {
            List<ProcessNodeRecordAssignUser> list = this.lambdaQuery()
                    .eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
                    .orderByDesc(ProcessNodeRecordAssignUser::getCreateTime)
                    .list();
            if (CollUtil.isNotEmpty(list)) {
                ProcessNodeRecordAssignUser processNodeRecordAssignUser = list.get(0);
                processNodeRecordAssignUser.setApproveDesc(processNodeRecordAssignUserParamDto.getApproveDesc());
                processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
                processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
                processNodeRecordAssignUser.setEndTime(new Date());
                this.updateById(processNodeRecordAssignUser);
            }

        }

        ProcessNodeRecordAssignUser processNodeRecordAssignUser = BeanUtil.copyProperties(processNodeRecordAssignUserParamDto, ProcessNodeRecordAssignUser.class);
        processNodeRecordAssignUser.setStartTime(new Date());
        processNodeRecordAssignUser.setStatus(NodeStatusEnum.JXZ.getCode());
        processNodeRecordAssignUser.setApproveDesc("");
        processNodeRecordAssignUser.setTaskType("");
        this.save(processNodeRecordAssignUser);

        return R.success();
    }

    /**
     * 任务完成通知
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R completeTaskEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        List<ProcessNodeRecordAssignUser> list = this.lambdaQuery()
                .eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
                .eq(ProcessNodeRecordAssignUser::getUserId, processNodeRecordAssignUserParamDto.getUserId())
                .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processNodeRecordAssignUserParamDto.getProcessInstanceId())
                .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                .orderByDesc(ProcessNodeRecordAssignUser::getCreateTime)
                .list();
        ProcessNodeRecordAssignUser processNodeRecordAssignUser = list.get(0);
        processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
        processNodeRecordAssignUser.setApproveDesc(processNodeRecordAssignUserParamDto.getApproveDesc());
        processNodeRecordAssignUser.setEndTime(new Date());
        processNodeRecordAssignUser.setData(processNodeRecordAssignUserParamDto.getData());
        processNodeRecordAssignUser.setLocalData(processNodeRecordAssignUserParamDto.getLocalData());
        processNodeRecordAssignUser.setTaskType("COMPLETE");
        this.updateById(processNodeRecordAssignUser);
        return R.success();
    }
}
