package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.service.IBaseService;
import com.cxygzl.biz.service.IProcessCopyService;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.dto.IndexPageStatistics;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class BaseServiceImpl implements IBaseService {

    @Resource
    private IProcessCopyService processCopyService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    /**
     * 首页数据
     *
     * @return
     */
    @Override
    public R index() {

        long userId = StpUtil.getLoginIdAsLong();


        Long coypNum = processCopyService.lambdaQuery().eq(ProcessCopy::getUserId, userId).count();

        Long startendNum = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getUserId, userId).count();

        IndexPageStatistics indexPageStatistics = CoreHttpUtil.querySimpleData(userId).getData();
        indexPageStatistics.setCopyNum(coypNum);
        indexPageStatistics.setStartedNum(startendNum);

        return R.success(indexPageStatistics);
    }
}
