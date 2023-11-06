package com.cxygzl.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorRspVo {

    private Integer code;

    private String msg;

    private String desp;
}
