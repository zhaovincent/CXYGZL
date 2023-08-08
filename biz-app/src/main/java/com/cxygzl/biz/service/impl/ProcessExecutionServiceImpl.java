package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.entity.ProcessExecution;
import com.cxygzl.biz.mapper.ProcessExecutionMapper;
import com.cxygzl.biz.service.IProcessExecutionService;
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
public class ProcessExecutionServiceImpl extends ServiceImpl<ProcessExecutionMapper, ProcessExecution> implements IProcessExecutionService {

}
