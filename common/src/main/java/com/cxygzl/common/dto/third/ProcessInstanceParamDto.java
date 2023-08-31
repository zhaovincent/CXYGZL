package com.cxygzl.common.dto.third;

import lombok.Data;

/**
 * 流程实例参数对象
 */
@Data
public class ProcessInstanceParamDto {

    /**
     * 实例id
     */
    private String processInstanceId;

    /**
     * 结果
     */
    private Integer result;

}
