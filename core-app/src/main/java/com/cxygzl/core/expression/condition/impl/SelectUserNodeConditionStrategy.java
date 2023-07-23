package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 字符类型处理器
 */
@Component
public class SelectUserNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     */
    @Override
    public String handle(Condition condition) {


        String compare = condition.getExpression();
        String id = condition.getKey();
        Object value = condition.getValue();


        ArrayList<Object> list = CollUtil.newArrayList(value);

        return StrUtil.format("(expressionHandler.userCompare(\"{}\",\"{}\",\"{}\", execution,\"{}\"))", id,
                EscapeUtil.escape(JSON.toJSONString(list)),compare,condition.getUserKey());


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.SELECT_USER.getType());
    }
}
