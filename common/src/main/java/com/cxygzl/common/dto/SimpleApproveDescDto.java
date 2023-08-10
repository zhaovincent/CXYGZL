package com.cxygzl.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SimpleApproveDescDto {

    private Date date;

    private String message;
    private String msgId;
    private String userId;
    private String type;
    private Boolean sys;

}
