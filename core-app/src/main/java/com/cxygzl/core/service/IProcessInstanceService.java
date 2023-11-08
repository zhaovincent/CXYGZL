package com.cxygzl.core.service;

import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 16:40
 */
public interface IProcessInstanceService {

    /**
     * 删除流程
     * @param processInstanceParamDto
     * @return
     */
    R delete(  ProcessInstanceParamDto processInstanceParamDto);



}
