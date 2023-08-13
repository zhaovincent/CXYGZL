package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.CoreHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指定主管
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
public class AssignUserLeaderStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        List<String> userIdList=new ArrayList<>();

        //指定主管审批
        //第几级主管审批
        Integer level = node.getDeptLeaderLevel();

        //去获取主管

        R<List<com.cxygzl.common.dto.third.DeptDto>> r = CoreHttpUtil.queryParentDepListByUserId((rootUser.getId()));

        List<com.cxygzl.common.dto.third.DeptDto> deptDtoList = r.getData();
        if (CollUtil.isNotEmpty(deptDtoList)) {
            if (deptDtoList.size() >= level) {
                DeptDto deptDto = deptDtoList.get(level - 1);

                List<String> leaderUserIdList = deptDto.getLeaderUserIdList();

                if(CollUtil.isNotEmpty(leaderUserIdList)){
                    userIdList.addAll(leaderUserIdList);
                }


            }
        }

        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.LEADER);

    }
}
