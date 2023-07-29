package com.cxygzl.biz.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NodeStatusEnum {

    WKS(0,"未开始"),
    JXZ(1,"进行中"),
    YJS(2,"已结束"),
    YCX(3,"已撤销"),
    ;

    private int code;

    private String name;



}
