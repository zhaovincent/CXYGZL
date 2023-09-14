package com.cxygzl.common.dto;

import lombok.Data;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-09-14 17:01
 */
@Data
public class NotifyMessageDto {

    private String flowId;

    private String processInstanceId;

    private String messageNotifyId;

}
