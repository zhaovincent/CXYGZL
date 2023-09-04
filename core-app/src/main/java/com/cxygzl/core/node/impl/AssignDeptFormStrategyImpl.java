package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 来自表单部门
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
public class AssignDeptFormStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        Set<String> assignList = new HashSet<>();
        //表单值

        Object variable = variables.get(node.getFormUserId());
        if (variable == null) {

        } else if (StrUtil.isBlankIfStr(variable)) {

        } else {

            String deptUserType = node.getDeptUserType();

            String jsonString = JSON.toJSONString(variable);
            List<NodeUser> nodeUserDtoList = CommonUtil.toArray(jsonString, NodeUser.class);
            List<String> deptIdList = nodeUserDtoList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

            if (!StrUtil.equals(deptUserType, ProcessInstanceConstant.AssignedTypeFormDeptUserTypeClass.ALL_USER)) {
                List<DeptDto> deptDtos = BizHttpUtil.queryDeptList(deptIdList);
                for (DeptDto deptDto : deptDtos) {
                    List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                    if (CollUtil.isNotEmpty(leaderUserIdList)) {
                        assignList.addAll(leaderUserIdList);
                    }
                }
            } else {
                List<String> userIdList = BizHttpUtil.queryUserIdListByDepIdList(deptIdList).getData();
                assignList.addAll(userIdList);
            }


        }
        return new ArrayList<>(assignList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FORM_DEPT);

    }
}
