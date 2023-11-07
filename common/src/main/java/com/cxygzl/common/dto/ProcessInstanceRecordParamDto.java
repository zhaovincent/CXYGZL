package com.cxygzl.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流程记录
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Getter
@Setter
public class ProcessInstanceRecordParamDto {


    /**
     * 用户id
     */
    private String userId;


    /**
     * 流程id
     */
    private String flowId;

    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 流程实例业务key
     */
    private String processInstanceBizKey;
    /**
     * 业务编码
     */
    private String processInstanceBizCode;

    private String parentProcessInstanceId;

    /**
     * 表单数据
     */
    private String formData;

}
