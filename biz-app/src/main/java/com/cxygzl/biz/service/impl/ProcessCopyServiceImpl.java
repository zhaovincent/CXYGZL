package com.cxygzl.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.biz.mapper.ProcessCopyMapper;
import com.cxygzl.biz.service.IProcessCopyService;
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
public class ProcessCopyServiceImpl extends ServiceImpl<ProcessCopyMapper, ProcessCopy> implements IProcessCopyService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

}
