package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 指定部门主管
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
public class AssignUserFixedDeptLeaderStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        Set<String> userIdList = new HashSet<>();


        List<NodeUser> userDtoList = node.getNodeUserList();
        //部门id
        List<String> deptIdList = userDtoList.stream()
                .filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
                .map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

        //去获取主管


        if (CollUtil.isNotEmpty(deptIdList)) {

            List<DeptDto> deptDtoList = BizHttpUtil.queryDeptList(deptIdList);
            for (DeptDto deptDto : deptDtoList) {
                List<String> leaderUserIdList = deptDto.getLeaderUserIdList();

                if (CollUtil.isNotEmpty(leaderUserIdList)) {
                    userIdList.addAll(leaderUserIdList);
                }
            }

        }

        return new ArrayList<>(userIdList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FIXED_DEPT_LEADER);

    }
}
