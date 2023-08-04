package com.cxygzl.core.service;

import com.cxygzl.common.dto.R;
import org.flowable.bpmn.model.FlowElement;

import java.util.List;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 16:40
 */
public interface IProcessService {


    R<List<FlowElement>> calApprovePath(String processInstanceId, String modelId, Map<String, Object> variableMap);
}
