package com.cxygzl.biz.vo;

import lombok.Data;

import java.util.Map;

@Data
public class NodeFormatParamVo {

    private String flowId;
    private String processInstanceId;
    private String taskId;
    private String ccId;

    private String from;

    private Map<String,Object> paramMap;

}
