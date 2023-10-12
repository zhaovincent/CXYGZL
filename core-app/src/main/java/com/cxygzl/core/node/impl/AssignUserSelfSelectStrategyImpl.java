package com.cxygzl.core.node.impl;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.node.AssignUserStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 发起人自选
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
@Slf4j
public class AssignUserSelfSelectStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        List<String> assignList = new ArrayList<>();

        Object variable = variables.get(StrUtil.format("{}_assignee_select", node.getId()));
        log.info("{}-发起人自选参数:{}", node.getNodeName(), variable);
        if (variable == null) {
            return assignList;
        }
        List<NodeUser> nodeUserDtos = JsonUtil.parseArray(JsonUtil.toJSONString(variable), NodeUser.class);

        List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

        assignList.addAll(collect);
        return assignList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT);

    }
}
