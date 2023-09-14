package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
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
 * @author Huijun Zhao
 * @description
 * @date 2023-07-31 11:30
 */
@Component
public class AssignUserFormDeptStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        Set<String> assignList=new HashSet<>();
        //表单值

        Object variable = variables.get(node.getFormUserId());

        String deptUserType = node.getDeptUserType();

        if (variable == null) {

        } else if (StrUtil.isBlankIfStr(variable)) {

        } else {

            String jsonString = JSON.toJSONString(variable);
            List<NodeUser> nodeUserDtoList = CommonUtil.toArray(jsonString, NodeUser.class);

            List<String> deptIdList = nodeUserDtoList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
//部门id

            if (CollUtil.isNotEmpty(deptIdList)) {

                if(!ProcessInstanceConstant.AssignedTypeFormDeptUserTypeClass.LEADER.equals(deptUserType)){
                    //人员
                    R<List<String>> r= BizHttpUtil.queryUserIdListByDepIdList(deptIdList);

                    List<String> data = r.getData();
                    if (CollUtil.isNotEmpty(data)) {
                        assignList.addAll(data);
                    }
                }else{
                    //主管
                    List<DeptDto> deptDtoList = BizHttpUtil.queryDeptList(deptIdList);
                    for (DeptDto deptDto : deptDtoList) {
                        assignList.addAll(deptDto.getLeaderUserIdList());
                    }
                }


            }

        }
        return new ArrayList<>(assignList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FORM_DEPT);

    }
}