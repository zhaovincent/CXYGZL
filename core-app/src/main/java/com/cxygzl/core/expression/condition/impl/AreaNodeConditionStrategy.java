package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 地区类型处理器
 */
@Component
public class AreaNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();

        return StrUtil.format("(expressionHandler.areaHandler(\"{}\",\"{}\",\"{}\",execution))", id, compare,
                value==null?null: EscapeUtil.escape(JSON.toJSONString(value)));


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.AREA.getType());
    }
}
