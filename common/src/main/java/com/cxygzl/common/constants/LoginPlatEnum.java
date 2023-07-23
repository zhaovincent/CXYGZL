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



    ;





    private String type;

    private String name;


}
