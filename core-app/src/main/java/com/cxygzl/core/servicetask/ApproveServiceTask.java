package com.cxygzl.core.servicetask;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.Refuse;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.FlowableUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import liquibase.repackaged.org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.ServiceTask;
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
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.*;

/**
 * 审批任务处理器--java服务任务
 */
@Slf4j
public class ApproveServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String nodeIdO = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();
        String processInstanceId = entity.getProcessInstanceId();

        ServiceTask currentFlowElement = (ServiceTask) entity.getCurrentFlowElement();



        String nodeId = FlowableUtils.getNodeIdFromExtension(currentFlowElement);

        Integer result = execution.getVariable(StrUtil.format("{}_{}", nodeId, APPROVE_NODE_RESULT), Integer.class);

        if (result != null) {

            //判断整体流程是通过还是拒绝
            execution.setVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT),
                    result);


            RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);


            if (result == ProcessInstanceConstant.ApproveResult.REFUSE) {
                //跳转
                String targetKey = ProcessInstanceConstant.VariableKey.END;

                Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);
                Refuse refuse = node.getRefuse();
                if (refuse != null) {
                    String handler = refuse.getHandler();
                    if (StrUtil.equals(handler, ProcessInstanceConstant.UserTaskRefuseHandler.TO_NODE)) {
                        targetKey = refuse.getNodeId();
                    }
                }
                if (StrUtil.equals(targetKey, ProcessInstanceConstant.VariableKey.STARTER)) {
                    targetKey = StrUtil.format("{}_user_task", targetKey);
                    runtimeService.setVariable(execution.getId(),
                            ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE, true);
                }

                runtimeService.setVariable(execution.getId(), FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());

                if(!StrUtil.equals(END,targetKey)){
                    //不是结束节点的时候 再添加变量
                    runtimeService.setVariable(execution.getId(), StrUtil.format("{}_parent_id", targetKey), nodeId);

                }

                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(processInstanceId)
                        .moveActivityIdTo(nodeIdO, targetKey)
                        .changeState();

            }

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
            return JsonUtil.parseObject(returnObj.toString(), clazz);
        } catch (PropertyNotFoundException e) {
            log.error("流程变量的属性找不到，请确认!", e);
            throw new RuntimeException("流程变量的属性找不到，请确认!", e);
        }

    }

}
