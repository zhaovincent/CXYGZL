package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.entity.ProcessInstanceExecution;
import com.cxygzl.biz.mapper.ProcessInstanceExecutionMapper;
import com.cxygzl.biz.service.IProcessInstanceExecutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程-执行id关系 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-08-09
 */
@Service
public class ProcessInstanceExecutionServiceImpl extends ServiceImpl<ProcessInstanceExecutionMapper, ProcessInstanceExecution> implements IProcessInstanceExecutionService {

}
