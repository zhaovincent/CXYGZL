package com.cxygzl.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.ProcessInstanceCopy;
import com.cxygzl.biz.mapper.ProcessInstanceCopyMapper;
import com.cxygzl.biz.service.IProcessInstanceCopyService;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.biz.service.IProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 流程抄送数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@Service
public class ProcessInstanceCopyServiceImpl extends ServiceImpl<ProcessInstanceCopyMapper, ProcessInstanceCopy> implements IProcessInstanceCopyService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

}
