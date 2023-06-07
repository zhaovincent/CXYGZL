package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.process.NodeConditionDto;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数字类型处理器
 * {
 *                                     "id": "field7127079768367",
 *                                     "title": "数字输入框1",
 *                                     "value": [
 *                                         "45",
 *                                         "78"
 *                                     ],
 *                                     "compare": "B",
 *                                     "valueType": "Number"
 *                                 }
 */
@Component
public class NumberNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
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
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id,"==",
                    value.get(0));
        }
        if(StrUtil.equals(compare, ">=")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id,">=",
                    value.get(0));
        }
        if(StrUtil.equals(compare, ">")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id,">",value.get(0));
        }
        if(StrUtil.equals(compare, "<")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id,"<",value.get(0));
        }
        if(StrUtil.equals(compare, "<=")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id,"<=",
                    value.get(0));
        }
        if(StrUtil.equals(compare, "B")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},\"{}\",{},execution))", id,">",
                    value.get(0),"<",
                    value.get(1));

        }
        if(StrUtil.equals(compare, "AB")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},\"{}\",{},execution))", id,">=",
                    value.get(0),"<",
                    value.get(1));
        }
        if(StrUtil.equals(compare, "BA")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},\"{}\",{},execution))", id,">",
                    value.get(0),"<=",
                value.get(1));

        }
        if(StrUtil.equals(compare, "ABA")){
            return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},\"{}\",{},execution))", id,">=",
                    value.get(0),
                    "<=",
                    value.get(1));
        }
        if(StrUtil.equals(compare, "IN")){
            return StrUtil.format("(expressionHandler.numberContain(\"{}\", execution,{}))", id, CollUtil.join(value,
                    ","));
        }
        return "(2==2)";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("Number");
    }
}
