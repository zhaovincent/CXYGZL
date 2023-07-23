package com.cxygzl.core.node;

import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;

import java.util.List;
import java.util.Map;

/**
 * 指定用户策略处理器
 */
public interface AssignUserStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(Integer key) {
        AssignUserStrategyFactory.register(key, this);
    }

    /**
     * 抽象方法 处理表达式
     *
     * @param node
     * @param rootUser
     * @param variables
     */
    List<String> handle(Node node, NodeUser rootUser, Map<String,Object> variables);


}
