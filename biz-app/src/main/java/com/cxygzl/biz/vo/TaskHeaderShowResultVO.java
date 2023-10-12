package com.cxygzl.biz.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-12 09:47
 */
@Data
public class TaskHeaderShowResultVO {


    private String processInstanceId;
    private String starterName;
    private String starterAvatarUrl;
    private String processName;
    private Date startTime;
    private Integer processInstanceResult;


}
