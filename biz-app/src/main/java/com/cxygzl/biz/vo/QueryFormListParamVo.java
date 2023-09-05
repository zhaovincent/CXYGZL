package com.cxygzl.biz.vo;

import lombok.Data;

import java.util.Map;

@Data
public class QueryFormListParamVo {
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 参数集合
     */
    private Map<String,Object> paramMap;


    /**
     * 实例id
     */
    private String processInstanceId;

    /**
     * 任务id
     */
    private String taskId;
    /**
     * 抄送id
     */
    private Long ccId;
}
