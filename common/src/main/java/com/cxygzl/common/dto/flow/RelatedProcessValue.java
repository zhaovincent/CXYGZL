package com.cxygzl.common.dto.flow;

import lombok.Data;

@Data
public class RelatedProcessValue {

    private String flowId;

    private String processInstanceId;
    private String processName;
    private Integer tabIndex;

}
