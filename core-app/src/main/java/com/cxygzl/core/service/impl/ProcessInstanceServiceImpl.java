package com.cxygzl.core.service.impl;

import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.core.service.IProcessInstanceService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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


        List<String> processInstanceIdList = processInstanceParamDto.getProcessInstanceIdList();
        for (String processInstanceId : processInstanceIdList) {
            if (runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count() > 0) {
                runtimeService.deleteProcessInstance(processInstanceId, processInstanceParamDto.getReason());
            }
        }


        return R.success();
    }
}
