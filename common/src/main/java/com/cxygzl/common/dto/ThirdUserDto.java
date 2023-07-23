package com.cxygzl.common.dto;

import lombok.Data;

/**
 * 第三方登录对象
 */
@Data
public class ThirdUserDto {
    /**
     * 前端获取的code 看小程序api
     */
    private String code;

    private String openId;

    private Long userId;
}
