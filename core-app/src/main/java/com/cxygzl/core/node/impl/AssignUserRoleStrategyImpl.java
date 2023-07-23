package com.cxygzl.core.node.impl;

import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.CoreHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来自角色
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
public class AssignUserRoleStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        List<String> assignList=new ArrayList<>();

        //角色

        List<NodeUser> nodeUserList = node.getNodeUserList();

        List<String> roleIdList = nodeUserList.stream().map(w -> w.getId()).collect(Collectors.toList());


        R<List<String>> r = CoreHttpUtil.queryUserIdListByRoleIdList(roleIdList);

        List<String> data = r.getData();


        assignList.addAll(data);
        return assignList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.ROLE);

    }
}
