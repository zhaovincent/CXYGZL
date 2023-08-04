package com.cxygzl.core.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("multiInstanceHandler")
@Slf4j
public class MultiInstanceHandler {
    @Resource
    private RuntimeService runtimeService;

    /**
     * 处理执行人
     *
     * @param execution
     * @return
     */
    public List<String> resolveAssignee(DelegateExecution execution) {
        //执行人集合
        List<String> assignList = new ArrayList<>();

        INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        String flowId = entity.getProcessDefinitionKey();
        String nodeId = entity.getActivityId();

        log.debug("nodeId={} nodeName={}", nodeId, entity.getActivityName());

        //发起人
        Object rootUserObj = execution.getVariable("root");
        NodeUser rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj), NodeUser.class).get(0);

        //节点数据
        Node node =  nodeDataStoreHandler.getNode(flowId, nodeId);
        if (node != null) {

            Map<String, Object> variables = execution.getVariables();

            Integer assignedType = node.getAssignedType();

            List<String> userIdList = AssignUserStrategyFactory.getStrategy(assignedType).handle(node, rootUser, variables);

            assignList.addAll(userIdList);

        } else {
            //默认值
            String format = StrUtil.format("{}_assignee_default_list", nodeId);
            Object variable = execution.getVariable(format);
            List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);
            if (CollUtil.isNotEmpty(nodeUserDtos)) {
                List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
                assignList.addAll(collect);
            }

        }

        if (CollUtil.isEmpty(assignList)) {
            assignList.add(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN);
        }

        return assignList;

    }



    /**
     * 会签或者或签完成条件检查
     *
     * @param execution
     */
    public boolean completionCondition(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String processDefinitionKey = entity.getProcessDefinitionKey();

        String nodeId = execution.getCurrentActivityId();

        Node node = NodeDataStoreFactory.getInstance().getNode(processDefinitionKey, nodeId);

        Integer multipleMode = node.getMultipleMode();



        Object variable = execution.getVariable(StrUtil.format("{}_approve_condition", nodeId));
        log.debug("当前节点审批结果：{}", variable);
        Boolean approve = Convert.toBool(variable);
//        if (StrUtil.equalsAny(mode, "AND", "NEXT")) {
        if (
                multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME||
                multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT

        ) {
            //会签或者顺序签署
            if (!approve) {
                return true;
            }
        }
        if (

                multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_ONE

        ) {
            //或签
            if (approve) {
                return true;
            }
        }

        //实例总数
        int nrOfInstances = (int) execution.getVariable("nrOfInstances");
        //完成的实例数
        int nrOfCompletedInstances = (int) execution.getVariable("nrOfCompletedInstances");

        if (nrOfCompletedInstances == nrOfInstances) {
            return true;
        }
        return false;
    }
}
