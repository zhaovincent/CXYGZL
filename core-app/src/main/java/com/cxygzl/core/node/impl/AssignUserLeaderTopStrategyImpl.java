package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 连续多级主管
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
public class AssignUserLeaderTopStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        List<String> userIdList = new ArrayList<>();


        //去获取主管

        R<List<com.cxygzl.common.dto.third.DeptDto>> r = BizHttpUtil.queryParentDepListByUserId((rootUser.getId()));

        List<com.cxygzl.common.dto.third.DeptDto> deptDtoList = r.getData();

        //上级主管依次审批

        //第几级主管审批截止
        Integer level = node.getDeptLeaderLevel();


        if (CollUtil.isNotEmpty(deptDtoList)) {
            int index = 1;
            for (DeptDto deptDto : deptDtoList) {
                if (level != null && level < index) {
                    break;
                }
                List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                if (CollUtil.isNotEmpty(leaderUserIdList)) {
                    for (String s : leaderUserIdList) {
                        if (StrUtil.isNotBlank(s) && !userIdList.contains(s)) {
                            userIdList.add((s));
                        }
                    }
                }

                index++;
            }
        }


        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.LEADER_TOP);

    }
}
