package com.cxygzl.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessInstanceDto {

    private String processInstanceId;
    private String flowId;
    private String processName;
    private String startUserId;
    private String startUserName;
    private String groupName;
    private Date startTime;
    private Date endTime;
    private Integer processInstanceResult;
    private Integer processInstanceStatus;

}
