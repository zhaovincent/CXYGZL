package com.cxygzl.common.dto;

import lombok.Data;

/**
 * 结果类
 * 统一返回值
 *
 * @param <T> 数据对象
 */
@Data
public class R<T> {
    /**
     * 链路id
     */
    private String traceId;

    /**
     * 是否请求成功
     */
    private boolean ok;
    /**
     * 结果码
     */
    private Integer code;
    /**
     * 提示消息
     */
    private String msg = "业务处理成功";
    /**
     * 数据
     */
    private T data;

    public static R success() {
        R r = new R();
        r.setOk(true);
        return r;
    }

    public static <T> R success(T data) {
        R r = new R();
        r.setOk(true);
        r.setData(data);
        return r;
    }

    public static <T> R fail(String msg) {
        R r = new R();
        r.setOk(false);
        r.setMsg(msg);
        return r;
    }

    public static <T> R success(T data,String msg) {
        R r = new R();
        r.setOk(true);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
}
