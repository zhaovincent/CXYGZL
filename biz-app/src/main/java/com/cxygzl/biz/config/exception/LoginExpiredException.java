package com.cxygzl.biz.config.exception;

import lombok.Getter;

/**
 * 登录失效
 * @author : vincent
 * @date : 2023/5/11
 */
@Getter
public class LoginExpiredException extends RuntimeException {
    private final int code;

    private final String description;

    public LoginExpiredException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description=description;
    }
    public LoginExpiredException(ResultCode resultCode, String description) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.description = description;
    }
}


