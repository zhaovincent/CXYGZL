package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.Process;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
public interface IProcessService extends IService<Process> {

    Process getByFlowId(String flowId);

    void updateByFlowId(Process process);

    void hide(String flowId);
}
