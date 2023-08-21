package com.cxygzl.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.core.cmd.ExpressCmd;
import org.flowable.engine.ManagementService;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-21 17:03
 */
public class DataUtil {

    public static boolean expression(String symbol, Dict value){
        ManagementService managementService = SpringUtil.getBean(ManagementService.class);


        Object result = managementService.executeCommand(new ExpressCmd(symbol, value));

        return Convert.toBool(result);
    }

}
