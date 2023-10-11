package com.cxygzl.biz.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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


    public static NodeStatusEnum get(int code){
       return Arrays.stream(NodeStatusEnum.values()).filter(w->w.getCode()==code).findAny().get();
    }

}
