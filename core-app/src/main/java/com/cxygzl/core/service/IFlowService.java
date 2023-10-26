package com.cxygzl.core.service;

import com.cxygzl.common.dto.CreateFlowDto;
import com.cxygzl.common.dto.NotifyMessageDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 16:40
 */
public interface IFlowService {

    /**
     * 创建流程模型
     * @param createFlowDto
     * @return
     */
    R create(  CreateFlowDto createFlowDto);

    /**
     * 发起流程
     * @param processInstanceParamDto
     * @return
     */
    R start(  ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 消息唤醒异步触发器
     * @param messageDto
     * @return
     */
    R notifyMsg(@RequestBody NotifyMessageDto messageDto);


}
