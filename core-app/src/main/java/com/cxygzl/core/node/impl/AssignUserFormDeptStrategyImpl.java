package com.cxygzl.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.core.node.AssignUserStrategy;
import com.cxygzl.core.utils.CoreHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


        List<String> assignList=new ArrayList<>();
        //表单值

        Object variable = variables.get(node.getFormUserId());
        if (variable == null) {

        } else if (StrUtil.isBlankIfStr(variable)) {

        } else {

            String jsonString = JSON.toJSONString(variable);
            List<NodeUser> nodeUserDtoList = CommonUtil.toArray(jsonString, NodeUser.class);

            List<String> deptIdList = nodeUserDtoList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
//部门id

            if (CollUtil.isNotEmpty(deptIdList)) {

                R<List<String>> r= CoreHttpUtil.queryUserIdListByDepIdList(deptIdList);

                List<String> data = r.getData();
                if (CollUtil.isNotEmpty(data)) {
                    assignList.addAll(data);
                }
            }

        }
        return assignList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FORM_DEPT);

    }
}
