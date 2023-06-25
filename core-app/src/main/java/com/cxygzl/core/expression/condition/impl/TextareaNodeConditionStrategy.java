package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 字符类型处理器
 */
@Component
public class TextareaNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     *
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();
        if(StrUtil.equals(compare, "==")){
            return StrUtil.format("(expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id,value);
        }
        if(StrUtil.equals(compare, "!=")){
            return StrUtil.format("(!expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id,value);
        }
        return "(2==2)";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.TEXTAREA.getType());
    }
}
