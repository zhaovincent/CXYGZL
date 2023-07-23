package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字符类型处理器
 */
@Component
public class SingleSelectNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();

        List<?> list = Convert.toList(value);


        StringBuilder sb = new StringBuilder();

        for (Object o : list) {
            sb.append(",\"").append(o.toString()).append("\"");
        }
        String string = sb.toString();
        if (CollUtil.isNotEmpty(list)) {
            string = string.substring(1);
        }
        if (compare.equals("in")) {
            return StrUtil.format("(expressionHandler.stringArrayContain(\"{}\", execution,{}))", id, string);
        }

        return StrUtil.format("(!expressionHandler.stringArrayContain(\"{}\", execution,{}))", id, string);


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.SINGLE_SELECT.getType());
    }
}
