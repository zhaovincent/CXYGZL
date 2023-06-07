package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.process.NodeConditionDto;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字符类型处理器
 */
@Component
public class StringNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     *
     * @param nodeConditionDto
     */
    @Override
    public String handle(NodeConditionDto nodeConditionDto) {

        String compare = nodeConditionDto.getCompare();
        String id = nodeConditionDto.getId();
        List value = nodeConditionDto.getValue();
        if(StrUtil.equals(compare, "=")){
            return StrUtil.format("(expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id,value.get(0));
        }
        if(StrUtil.equals(compare, "IN")){

            StringBuilder sb=new StringBuilder();

            for (Object o : value) {
                sb.append(",\"").append(o.toString()).append("\"");
            }
            String string = sb.toString();
            if(CollUtil.isNotEmpty(value)){
                string=string.substring(1);
            }

            return StrUtil.format("(expressionHandler.stringContain(\"{}\", execution,{}))", id, string);
        }
        return "(2==2)";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("String");
    }
}
