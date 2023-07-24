package com.cxygzl.common.utils;

import cn.hutool.core.util.StrUtil;

public class AreaUtil {

    public static boolean contain(String code1, String code2) {
        String pcode = "";
        if (StrUtil.endWith(code1, "0000")) {
            pcode = StrUtil.subBefore(code1, "0000", true);
        } else if (StrUtil.endWith(code1, "00")) {
            pcode = StrUtil.subBefore(code1, "00", true);
        }else{
            pcode=code1;
        }
        return StrUtil.startWith(code2,pcode);

    }

}
