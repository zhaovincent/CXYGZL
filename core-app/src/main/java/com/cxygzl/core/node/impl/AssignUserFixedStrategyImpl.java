package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 指定具体用户
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component
public class AssignUserFixedStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        //指定人员
        List<NodeUser> userDtoList = node.getNodeUserList();
        //用户id
        List<String> userIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());
        //部门id
        List<String> deptIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(deptIdList)) {

            R<List<String>> r= BizHttpUtil.queryUserIdListByDepIdList(deptIdList);

            List<String> data = r.getData();
            if (CollUtil.isNotEmpty(data)) {
                for (String datum : data) {
                    if (!userIdList.contains(datum)) {
                        userIdList.add(datum);
                    }
                }
            }
        }
        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.USER);

    }
}
