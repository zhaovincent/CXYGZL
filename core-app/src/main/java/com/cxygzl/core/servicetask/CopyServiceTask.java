package com.cxygzl.core.servicetask;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.ProcessCopyDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.CoreHttpUtil;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抄送任务处理器--java服务任务
 */
public class CopyServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity= (ExecutionEntityImpl) execution;
        String nodeId = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);



        //指定人员
        List<NodeUser> userDtoList = node.getNodeUserList();
        //用户id
        List<String> userIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());
        //部门id
        List<String> deptIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(deptIdList)) {

            R<List<String>> r = CoreHttpUtil.queryUserIdListByDepIdList(deptIdList);

            List<String> data = r.getData();
            if (CollUtil.isNotEmpty(data)) {
                for (String datum : data) {
                    if (!userIdList.contains(datum)) {
                        userIdList.add(datum);
                    }
                }
            }
        }


        //发起人
        Object rootUserObj = execution.getVariable("root");
        NodeUser rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj), NodeUser.class).get(0);

        Map<String, Object> variables = execution.getVariables();

        for (String userIds : userIdList) {
            //发送抄送任务
            ProcessCopyDto processCopyDto = new ProcessCopyDto();
            processCopyDto.setNodeTime(new Date());
            processCopyDto.setStartUserId(Long.parseLong(rootUser.getId()));
            processCopyDto.setFlowId(flowId);
            processCopyDto.setProcessInstanceId(execution.getProcessInstanceId());
            processCopyDto.setNodeId(nodeId);
            processCopyDto.setNodeName(node.getName());
            processCopyDto.setFormData(JSON.toJSONString(variables));
            processCopyDto.setUserId(Long.parseLong(userIds));

            CoreHttpUtil.saveCC(processCopyDto);
        }


    }
}
