package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessInstanceUserCopy;
import com.cxygzl.biz.mapper.ProcessInstanceUserCopyMapper;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.service.IProcessInstanceUserCopyService;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.vo.ProcessDataQueryVO;
import com.cxygzl.biz.vo.ProcessInstanceCopyVo;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessInstanceUserCopyServiceImpl extends ServiceImpl<ProcessInstanceUserCopyMapper,
        ProcessInstanceUserCopy> implements IProcessInstanceUserCopyService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessService processService;
    /**
     * 查询抄送给我的(根据实例id去重)
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryMineCCProcessInstance(ProcessDataQueryVO pageDto) {

        List<String> flowIdList = pageDto.getFlowIdList();
        if(CollUtil.isNotEmpty(flowIdList)){
            flowIdList = processService.getAllRelatedFlowId(flowIdList).getData();
        }


        String userId = StpUtil.getLoginIdAsString();

        Page<ProcessInstanceUserCopy> page = this.lambdaQuery()
                .eq(ProcessInstanceUserCopy::getUserId, userId)
                .in(CollUtil.isNotEmpty(flowIdList), ProcessInstanceUserCopy::getFlowId,
                        flowIdList)
                .orderByDesc(ProcessInstanceUserCopy::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        List<ProcessInstanceUserCopy> records = page.getRecords();

        List<ProcessInstanceCopyVo> processCopyVoList = BeanUtil.copyToList(records, ProcessInstanceCopyVo.class);

        if (CollUtil.isNotEmpty(records)) {

            //发起人
            Set<String> startUserIdSet = records.stream().map(w -> w.getStartUserId()).collect(Collectors.toSet());

            List<UserDto> startUserList = new ArrayList<>();
            for (String s : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
                startUserList.add(user);
            }
            Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

            //流程实例记录
            List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceIdSet).list();

            for (ProcessInstanceCopyVo record : processCopyVoList) {

                ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                        record.getProcessInstanceId())).findFirst().get();


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(record.getStartUserId())).findAny().orElse(null);
                record.setStartUserName(startUser.getName());


                record.setStartTime(processInstanceRecord.getCreateTime());

                record.setProcessInstanceStatus(processInstanceRecord.getStatus());
                record.setProcessInstanceResult(processInstanceRecord.getResult());

            }
        }

        Page p = BeanUtil.copyProperties(page, Page.class);

        p.setRecords(processCopyVoList);

        return com.cxygzl.common.dto.R.success(p);
    }
}
