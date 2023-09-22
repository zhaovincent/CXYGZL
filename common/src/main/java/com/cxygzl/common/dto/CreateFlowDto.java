package com.cxygzl.common.dto;

import com.cxygzl.common.dto.flow.Node;
import lombok.Data;

/**
 * 创建流程对象
 * @author Huijun Zhao
 * @description
 * @date 2023-09-22 10:45
 */
@Data
public class CreateFlowDto {

    private String userId;

    private Node node;

    private String processName;

}
