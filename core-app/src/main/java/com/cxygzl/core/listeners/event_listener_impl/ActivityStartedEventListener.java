package com.cxygzl.core.listeners.event_listener_impl;

import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 实例节点开始了
 * @author Huijun Zhao
 * @description
 * @date 2023-10-10 10:12
 */
@Slf4j
@Component
public class ActivityStartedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {
        //节点开始执行
        FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
        String activityId = flowableActivityEvent.getActivityId();
        String activityName = flowableActivityEvent.getActivityName();
        DelegateExecution execution = flowableActivityEvent.getExecution();

        String executionId = flowableActivityEvent.getExecutionId();
        log.info("节点开始  节点id：{} 名字:{} executionId:{}", activityId, activityName,
                executionId);
        {
            DelegateExecution parent = execution.getParent();
            if (parent.isMultiInstanceRoot() && !execution.isMultiInstanceRoot()) {
                List<String> childExecutionIdList = parent.getExecutions().stream().map(w -> w.getId()).collect(Collectors.toList());
                if (childExecutionIdList.contains(execution.getId())) {

                    //记录父子级关系
                    ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
                    processInstanceNodeRecordParamDto.setExecutionId(parent.getId());
                    processInstanceNodeRecordParamDto.setChildExecutionId(childExecutionIdList);
                    BizHttpUtil.saveParentChildExecution(processInstanceNodeRecordParamDto);


                    return;
                }
            }


        }


        String processInstanceId = flowableActivityEvent.getProcessInstanceId();

        String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
        String flowId = NodeUtil.getFlowId(processDefinitionId);


        saveStartEventContent(flowId, processInstanceId, activityId, activityName, execution.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.ACTIVITY_STARTED.toString());

    }
}
