package com.cxygzl.common.dto;

import lombok.Data;

/**
 * 获取微信小程序手机号对象
 */
@Data
public class WeixinMiniAppQueryPhoneDto {

    private String sessionKey;
    private String code;


}
