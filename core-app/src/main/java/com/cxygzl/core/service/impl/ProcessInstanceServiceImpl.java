package com.cxygzl.core.service.impl;

import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.core.service.IProcessInstanceService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-11-08 17:30
 */
@Component
public class ProcessInstanceServiceImpl implements IProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;

    /**
     * 删除流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @Transactional
    @Override
    public R delete(ProcessInstanceParamDto processInstanceParamDto) {
        String processInstanceId = processInstanceParamDto.getProcessInstanceId();
        if (runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count() > 0) {
            runtimeService.deleteProcessInstance(processInstanceId, processInstanceParamDto.getReason());
        } else if (historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).count() > 0) {
            historyService.deleteHistoricProcessInstance(processInstanceId);
        } else {
            return R.fail("流程实例不存在");
        }
        return R.success();
    }
}
