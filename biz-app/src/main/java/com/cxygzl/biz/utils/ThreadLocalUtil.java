package com.cxygzl.biz.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;

public class ThreadLocalUtil {

    private static ThreadLocal<Dict> threadLocal = new ThreadLocal<>();

    public static void putUserId(long userId) {
        Dict dict = threadLocal.get();
        if (dict == null) {
            dict = new Dict();
        }
        dict.set("userId", userId);
        threadLocal.set(dict);
    }

    public static Long getUserId() {
        Dict dict = threadLocal.get();
        if (dict == null) {
            return null;
        }
        Object o = dict.get("userId");
        if (o == null) {
            return null;
        }
        return Convert.toLong(o);
    }

}
