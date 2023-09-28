package com.cxygzl.biz.config.exception;

import lombok.Getter;

/**
 * @author : willian fu
 * @date : 2022/6/27
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;


    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.EXCEPTION.getCode();
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }
}


