package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.process.NodeConditionDto;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日期类型处理器
 */
@Component
public class DateNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
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
            return StrUtil.format("(expressionHandler.dateCompare(\"{}\",\"{}\",\"{}\",execution,\"{}\"))", id,"==",
                    value.get(0),nodeConditionDto.getFormat());
        }
        if(StrUtil.equals(compare, ">=")){
            return StrUtil.format("(expressionHandler.dateCompare(\"{}\",\"{}\",\"{}\",execution,\"{}\"))", id,">=",
                    value.get(0),nodeConditionDto.getFormat());
        }
        if(StrUtil.equals(compare, ">")){
            return StrUtil.format("(expressionHandler.dateCompare(\"{}\",\"{}\",\"{}\",execution,\"{}\"))", id,">",
                    value.get(0),nodeConditionDto.getFormat());
        }
        if(StrUtil.equals(compare, "<")){
            return StrUtil.format("(expressionHandler.dateCompare(\"{}\",\"{}\",\"{}\",execution,\"{}\"))", id,"<",
                    value.get(0),nodeConditionDto.getFormat());
        }
        if(StrUtil.equals(compare, "<=")){
            return StrUtil.format("(expressionHandler.dateCompare(\"{}\",\"{}\",\"{}\",execution,\"{}\"))", id,"<=",
                    value.get(0),nodeConditionDto.getFormat());
        }

        return "(2==2)";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("Date");
    }
}
