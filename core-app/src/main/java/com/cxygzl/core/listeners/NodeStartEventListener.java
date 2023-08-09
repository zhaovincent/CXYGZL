package com.cxygzl.core.listeners;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.FLOW_UNIQUE_ID;

/**
 * 流程监听器
 */
@Slf4j
public class NodeStartEventListener implements FlowableEventListener {


    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {



        if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_STARTED.toString())) {
            //节点开始执行
            //org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl
            FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;



            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            String parentId = "";
            List<String> childExecutionIdList=new ArrayList<>();
            {
                DelegateExecution execution = flowableActivityEvent.getExecution();
                parentId=execution.getId();


                DelegateExecution parent = execution.getParent();
                if(parent.isMultiInstanceRoot()){
                    parentId=parent.getId();
                    List<? extends DelegateExecution> executions = parent.getExecutions();
                    List<String> collect = executions.stream().map(w -> w.getId()).collect(Collectors.toList());
                    childExecutionIdList.addAll(collect);
                }


                log.info("节点开始  节点id：{} 名字:{} executionId:{}, 上级id:{}", activityId, activityName,
                        flowableActivityEvent.getExecutionId(), parentId);

            }


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            Node node = NodeDataStoreFactory.getInstance().getNode(flowId, activityId);

            RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
            Map<String, Object> processVariables = runtimeService.getVariables(flowableActivityEvent.getExecutionId());


            ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
            processNodeRecordParamDto.setFlowId(flowId);
            processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
            processNodeRecordParamDto.setChildExecutionId(childExecutionIdList);
            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processNodeRecordParamDto.setNodeId(activityId);
            processNodeRecordParamDto.setParentNodeId(MapUtil.getStr(processVariables, StrUtil.format("{}_parent_id", activityId)));
            processNodeRecordParamDto.setFlowUniqueId(MapUtil.getStr(processVariables, FLOW_UNIQUE_ID));
            if (node != null) {

                processNodeRecordParamDto.setNodeType((node.getType()));

            }
            processNodeRecordParamDto.setNodeName(activityName);
            processNodeRecordParamDto.setExecutionId(parentId);
            CoreHttpUtil.startNodeEvent(processNodeRecordParamDto);

            //清除变量
            runtimeService.removeVariable(flowableActivityEvent.getExecutionId(), StrUtil.format("{}_parent_id", activityId));
        }

    }


    /**
     * @return whether or not the current operation should fail when this listeners execution throws an exception.
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * @return Returns whether this event listener fires immediately when the event occurs or
     * on a transaction lifecycle event (before/after commit or rollback).
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    /**
     * @return if non-null, indicates the point in the lifecycle of the current transaction when the event should be fired.
     */
    @Override
    public String getOnTransaction() {
        return null;
    }
}
