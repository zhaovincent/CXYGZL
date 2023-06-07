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
    public Process getByFormId(String formId) {
        return this.lambdaQuery().eq(Process::getFormId,formId).one();
    }

    @Override
    public void updateByFormId(Process process) {
        this.lambdaUpdate().eq(Process::getFormId,process.getFormId()).update(process);
    }

    @Override
    public void hide(String formId) {
        this.lambdaUpdate().set(Process::getIsHidden,true).eq(Process::getFormId,formId).update(new Process());
    }
}
