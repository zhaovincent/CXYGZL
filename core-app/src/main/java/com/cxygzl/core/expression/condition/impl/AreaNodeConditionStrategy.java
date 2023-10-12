package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.expression.ExpressionHandler;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 地区类型处理器
 */
@Component
public class AreaNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     */
    @Override
    public String handleExpression(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();

        return StrUtil.format("(expressionHandler.areaHandler(\"{}\",\"{}\",\"{}\",execution))", id, compare,
                value==null?null: EscapeUtil.escape(JsonUtil.toJSONString(value)));


    }

    /**
     * 处理数据
     *
     * @param condition
     * @param paramMap
     * @return
     */
    @Override
    public boolean handleResult(Condition condition, Map<String, Object> paramMap) {

        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();


        ExpressionHandler bean = SpringUtil.getBean(ExpressionHandler.class);
        return bean.areaHandler(id,compare,value==null?null: EscapeUtil.escape(JsonUtil.toJSONString(value)),paramMap.get(id));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.AREA.getType());
    }
}
