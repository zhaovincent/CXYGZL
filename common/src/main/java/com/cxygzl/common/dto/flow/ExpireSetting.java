package com.cxygzl.common.dto.flow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 过期设置
 */
@Data
public class ExpireSetting {
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enable;
    /**
     * 提醒时间数值
     */
    @ApiModelProperty("提醒时间数值")
    private Integer value;
    /**
     * 提醒时间单位
     */
    @ApiModelProperty("提醒时间单位")
    private String valueUnit;
    /**
     * 1通知2自动通过3自动拒绝4指定人员
     */
    @ApiModelProperty("1通知2自动通过3自动拒绝4指定人员")
    private Integer type;

    /**
     * 一次提醒还是循环
     */
    @ApiModelProperty("一次提醒还是循环")
    private Boolean once;

}
