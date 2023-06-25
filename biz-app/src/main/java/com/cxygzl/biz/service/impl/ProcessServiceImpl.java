package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.mapper.ProcessMapper;
import com.cxygzl.biz.service.IProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {
    @Override
    public Process getByFlowId(String flowId) {
        return this.lambdaQuery().eq(Process::getFlowId, flowId).one();
    }

    @Override
    public void updateByFlowId(Process process) {
        this.lambdaUpdate().eq(Process::getFlowId,process.getFlowId()).update(process);
    }

    @Override
    public void hide(String flowId) {
        this.lambdaUpdate().set(Process::getIsHidden,true).eq(Process::getFlowId,flowId).update(new Process());
    }
}
