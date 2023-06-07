package com.cxygzl.biz.vo;

import lombok.Data;

import java.util.Map;

@Data
public class NodeFormatParamVo {

    private String formId;
    private String processInstanceId;
    private String taskId;

    private Map<String,Object> paramMap;

}
