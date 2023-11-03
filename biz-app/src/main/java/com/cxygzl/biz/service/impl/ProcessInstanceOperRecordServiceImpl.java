package com.cxygzl.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.ProcessInstanceOperRecord;
import com.cxygzl.biz.mapper.ProcessInstanceOperRecordMapper;
import com.cxygzl.biz.service.IProcessInstanceOperRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-11-03 17:46
 */
@Service
@Slf4j
public class ProcessInstanceOperRecordServiceImpl extends ServiceImpl<ProcessInstanceOperRecordMapper, ProcessInstanceOperRecord>
        implements IProcessInstanceOperRecordService {
}
