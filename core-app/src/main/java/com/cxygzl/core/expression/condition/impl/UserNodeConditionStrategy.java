package com.cxygzl.core.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.process.NodeConditionDto;
import com.cxygzl.core.expression.condition.NodeConditionStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户类型处理器
 */
@Component
public class UserNodeConditionStrategy implements NodeConditionStrategy, InitializingBean {
    /**
     * 抽象方法 处理表达式
     *
     * @param nodeConditionDto
     */
    @Override
    public String handle(NodeConditionDto nodeConditionDto) {

        String id = nodeConditionDto.getId();
        List value = nodeConditionDto.getValue();
        String userKey = nodeConditionDto.getUserKey();
        String userType = nodeConditionDto.getUserType();
        String compare = nodeConditionDto.getCompare();

        return StrUtil.format("(expressionHandler.userCompare(\"{}\",\"{}\",\"{}\",\"{}\",\"{}\",execution))", id,
              EscapeUtil.escape( JSON.toJSONString(value)),userKey,userType,compare);

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("User");
    }
}
