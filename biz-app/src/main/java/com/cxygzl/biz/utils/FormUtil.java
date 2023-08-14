package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.constants.FormTypeEnum;

public class FormUtil {

    public static void handValue(FormItemVO formItemVO, Object value){

        formItemVO.getProps().setValue(value);

        if (formItemVO.getType().equals(FormTypeEnum.NUMBER.getType())
                ||
                formItemVO.getType().equals(FormTypeEnum.MONEY.getType())
        ) {
            if (StrUtil.isBlankIfStr(value)) {
                formItemVO.getProps().setValue(null);

            }
        }

    }

}
