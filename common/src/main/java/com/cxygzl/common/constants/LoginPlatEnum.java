package com.cxygzl.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录平台
 */
@Getter
@AllArgsConstructor
public enum LoginPlatEnum {

    ADMIN("admin","管理员后台"),
    DING_TALK("dingtalk","钉钉登录"),
    WX_MIN_APP("wxMiniApp","微信小程序"),



    ;





    private String type;

    private String name;


}
