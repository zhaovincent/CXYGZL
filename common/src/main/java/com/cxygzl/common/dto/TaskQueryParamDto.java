package com.cxygzl.common.dto;

import lombok.Data;

/**
 * 用户任务查询参数
 */
@Data
public class TaskQueryParamDto extends PageDto{
    /**
     * 任务执行人
     */
    private String assign;

}
