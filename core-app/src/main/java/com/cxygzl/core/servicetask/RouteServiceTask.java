package com.cxygzl.core.servicetask;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import liquibase.repackaged.org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.PropertyNotFoundException;
import org.flowable.common.engine.impl.javax.el.ValueExpression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID;

/**
 * 触发器任务处理器--java服务任务
 */
@Slf4j
public class RouteServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String nodeIdO = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();
        String processInstanceId = entity.getProcessInstanceId();

        String nodeId = StrUtil.subBefore(nodeIdO, "_", true);
        int index = Integer.parseInt(StrUtil.subAfter(nodeIdO, "_", true));

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);


        Map<String, Object> variables = execution.getVariables();


        List<Node> list = node.getList();


        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);

        if (index < list.size()) {
            Node routeNode = list.get(index);


            //判断发起人
            String targetKey = routeNode.getNodeId();
            if (StrUtil.equals(targetKey, ProcessInstanceConstant.VariableKey.STARTER)) {
                targetKey = StrUtil.format("{}_user_task", targetKey);
                runtimeService.setVariable(execution.getId(),
                        ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE, true);
            }

            runtimeService.setVariable(execution.getId(), StrUtil.format("{}_parent_id", targetKey), nodeId);
            runtimeService.setVariable(execution.getId(), FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());

            //跳转
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(processInstanceId)
                    .moveActivityIdTo(nodeIdO, targetKey)
                    .changeState();
        }

    }

    /**
     * 原生的解析表达式
     *
     * @param params 变量的值
     * @param exp    表达式
     * @param clazz  映射出来的值
     * @return
     */
    public <T> T getValue(Map<String, Object> params, String exp, Class<T> clazz) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (v instanceof ObjectNode) {
                    JSONObject jsonObject = JSONObject.parseObject(v.toString());
                    Map<String, Object> vs = new HashMap<>();
                    for (String objkey : jsonObject.keySet()) {
                        vs.put(objkey, jsonObject.get(objkey));
                    }
                    context.setVariable(k, factory.createValueExpression(vs, Map.class));
                } else {
                    context.setVariable(k, factory.createValueExpression(v, Object.class));
                }
            });
        }

        try {
            ValueExpression e = factory.createValueExpression(context, exp, clazz);
            Object returnObj = e.getValue(context);
            log.info("表达式返回值：{}", returnObj);
            return JSON.parseObject(returnObj.toString(), clazz);
        } catch (PropertyNotFoundException e) {
            log.error("流程变量的属性找不到，请确认!", e);
            throw new RuntimeException("流程变量的属性找不到，请确认!", e);
        }

    }

}
