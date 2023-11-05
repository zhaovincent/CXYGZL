package com.cxygzl.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.ProcessInstanceOperRecord;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.mapper.ProcessInstanceOperRecordMapper;
import com.cxygzl.biz.service.IProcessInstanceOperRecordService;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.constants.OperTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.flow.UploadValue;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-11-03 17:46
 */
@Service
@Slf4j
public class ProcessInstanceOperRecordServiceImpl extends ServiceImpl<ProcessInstanceOperRecordMapper, ProcessInstanceOperRecord>
        implements IProcessInstanceOperRecordService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    /**
     * 保存记录
     *
     * @param userId
     * @param taskParamDto
     * @param operType
     * @param desc
     * @return
     */
    @Override
    public R saveRecord(String userId, TaskParamDto taskParamDto, String operType, String desc) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        ProcessInstanceOperRecord processInstanceOperRecord=new ProcessInstanceOperRecord();
        processInstanceOperRecord.setUserId(userId);
        processInstanceOperRecord.setProcessInstanceId(taskParamDto.getProcessInstanceId());
        processInstanceOperRecord.setComment(taskParamDto.getApproveDesc());

        TaskResultDto taskResultDto = CoreHttpUtil.queryTask(taskParamDto.getTaskId(), null).getData();

        processInstanceOperRecord.setNodeId(taskResultDto.getNodeId());
        processInstanceOperRecord.setNodeName(taskResultDto.getNodeName());
        processInstanceOperRecord.setFlowId(taskResultDto.getFlowId());

        List<UploadValue> approveImageList = taskParamDto.getApproveImageList();
        List<UploadValue> approveFileList = taskParamDto.getApproveFileList();

        processInstanceOperRecord.setImageList(JsonUtil.toJSONString(approveImageList));
        processInstanceOperRecord.setFileList(JsonUtil.toJSONString(approveFileList));

            processInstanceOperRecord.setOperType(operType);
            processInstanceOperRecord.setOperDesc(StrUtil.format("{}[{}] / {} / {} / {} / {} / {} ",user.getName(),
                    userId,
                    taskResultDto.getNodeName(), DateUtil.formatDateTime(new Date()),desc,
                    OperTypeEnum.getByValue(operType).getName(), taskParamDto.getApproveDesc()

            ));


        this.save(processInstanceOperRecord);
        return R.success();
    }

    /**
     * 撤销流程
     *
     * @param userId
     * @param processInstanceId
     * @return
     */
    @Override
    public R saveCancelProcessRecord(String userId, String processInstanceId) {

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        ProcessInstanceOperRecord processInstanceOperRecord=new ProcessInstanceOperRecord();
        processInstanceOperRecord.setUserId(userId);
        processInstanceOperRecord.setProcessInstanceId(processInstanceId);
        processInstanceOperRecord.setFlowId(processInstanceRecord.getFlowId());



        processInstanceOperRecord.setOperType(OperTypeEnum.CANCEL.getValue());
        processInstanceOperRecord.setOperDesc(StrUtil.format("{}[{}] / {} / {} / {} / {} / {}",user.getName(),
                userId,
                "", DateUtil.formatDateTime(new Date()),"撤销流程",
                OperTypeEnum.CANCEL.getName(), ""

        ));


        this.save(processInstanceOperRecord);
        return R.success();
    }

    /**
     * 发起流程
     *
     * @param userId
     * @param processInstanceId
     * @param flowId
     * @return
     */
    @Override
    public R saveStartProcessRecord(String userId, String processInstanceId, String flowId) {

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        ProcessInstanceOperRecord processInstanceOperRecord=new ProcessInstanceOperRecord();
        processInstanceOperRecord.setUserId(userId);
        processInstanceOperRecord.setProcessInstanceId(processInstanceId);
        processInstanceOperRecord.setFlowId(flowId);



        processInstanceOperRecord.setOperType(OperTypeEnum.START.getValue());
        processInstanceOperRecord.setOperDesc(StrUtil.format("{}[{}] / {} / {} / {} / {} / {}",user.getName(),
                userId,
                "开始节点", DateUtil.formatDateTime(new Date()),"发起流程",
                OperTypeEnum.START.getName(), ""

        ));


        this.save(processInstanceOperRecord);
        return R.success();
    }
}
