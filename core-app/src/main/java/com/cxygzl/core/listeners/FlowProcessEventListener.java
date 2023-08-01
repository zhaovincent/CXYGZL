package com.cxygzl.core.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.MessageTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableMultiInstanceActivityCompletedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.task.api.DelegationState;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程监听器
 */
@Slf4j
public class FlowProcessEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {
        log.debug("分支监听器 类型={} class={}", event.getType(), event.getClass().getCanonicalName());
        if (event.getType().toString().equals(FlowableEngineEventType.TASK_CREATED.toString())) {


            //任务被创建了
            FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;

            TaskEntity taskEntity = (TaskEntity) flowableEntityEvent.getEntity();


            String processInstanceId = taskEntity.getProcessInstanceId();
            String processDefinitionId = taskEntity.getProcessDefinitionId();

            String flowId = NodeUtil.getFlowId(processDefinitionId);

            String taskId = taskEntity.getId();
            String assignee = taskEntity.getAssignee();
            MessageDto messageDto = MessageDto.builder()
                    .userId(assignee)
                    .flowId(flowId)
                    .processInstanceId(processInstanceId)

                    .uniqueId(taskId)
                    .param(JSON.toJSONString(taskEntity.getVariables()))

                    .type(MessageTypeEnum.TODO_TASK.getType())
                    .readed(false).build();
            CoreHttpUtil.saveMessage(messageDto);
        }
        if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_STARTED.toString())) {
            //节点开始执行
            //org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl
            org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.debug("节点id：{} 名字:{}", activityId, activityName);


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            Node node = NodeDataStoreFactory.getInstance().getNode(flowId, activityId);


            ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
            processNodeRecordParamDto.setFlowId(flowId);
            processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processNodeRecordParamDto.setNodeId(activityId);
            if (node != null) {

                processNodeRecordParamDto.setNodeType(String.valueOf(node.getType()));

            }
            processNodeRecordParamDto.setNodeName(activityName);
            processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            CoreHttpUtil.startNodeEvent(processNodeRecordParamDto);

        }

        if (
                event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION.toString())
                        ||
                        event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED.toString())
        ) {
            //多实例任务
            org.flowable.engine.delegate.event.impl.FlowableMultiInstanceActivityCompletedEventImpl flowableActivityEvent = (FlowableMultiInstanceActivityCompletedEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.debug("节点id：{} 名字:{}", activityId, activityName);


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
            processNodeRecordParamDto.setFlowId(flowId);
            processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processNodeRecordParamDto.setNodeName(activityName);

            CoreHttpUtil.endNodeEvent(processNodeRecordParamDto);


        }
        if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_COMPLETED.toString())) {
            //节点完成执行

            org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.debug("节点id：{} 名字:{}", activityId, activityName);

            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
            processNodeRecordParamDto.setFlowId(flowId);
            processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processNodeRecordParamDto.setNodeName(activityName);

            CoreHttpUtil.endNodeEvent(processNodeRecordParamDto);

        }


        if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString())) {
            //流程开完成
            org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
            DelegateExecution execution = e.getExecution();
            String processInstanceId = e.getProcessInstanceId();
            ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();
            Map<String, Object> variables = execution.getVariables();


            ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
            processInstanceParamDto.setProcessInstanceId(processInstanceId);
            processInstanceParamDto.setCancel(MapUtil.getBool(variables,
                    ProcessInstanceConstant.VariableKey.CANCEL
                    , false));
            CoreHttpUtil.endProcessEvent(processInstanceParamDto);
            {
                String flowId = entity.getProcessDefinitionKey();

                {
                    //判断后置事件
                    FlowSettingDto flowSettingDto = CoreHttpUtil.queryProcessSetting(flowId).getData();
                    if (flowSettingDto != null) {
                        HttpSetting backNotify = flowSettingDto.getBackNotify();
                        if (backNotify != null && backNotify.getEnable()) {

                            Map<String, String> headerParamMap = new HashMap<>();
                            {
                                List<HttpSettingData> headerSetting = backNotify.getHeader();
                                for (HttpSettingData httpSettingData : headerSetting) {
                                    if (httpSettingData.getValueMode()) {
                                        headerParamMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                    } else {
                                        Object object = variables.get(httpSettingData.getValue());


                                        headerParamMap.put(httpSettingData.getField(), object == null ? null : (object instanceof String ? Convert.toStr(object) : JSON.toJSONString(object)));

                                    }
                                }

                            }


                            Map<String, Object> bodyMap = new HashMap<>();
                            {
                                //存入默认值
                                bodyMap.put("flowId", flowId);
                                bodyMap.put("cancel", MapUtil.getBool(variables,
                                        ProcessInstanceConstant.VariableKey.CANCEL
                                        , false));
                                bodyMap.put("processInstanceId", processInstanceId);
                                List<HttpSettingData> bodySetting = backNotify.getBody();
                                for (HttpSettingData httpSettingData : bodySetting) {
                                    if (httpSettingData.getValueMode()) {
                                        bodyMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                    } else {
                                        bodyMap.put(httpSettingData.getField(), (variables.get(httpSettingData.getValue())));
                                    }
                                }

                            }
                            log.info("后置事件url：{} 请求头：{} 请求体：{} ", backNotify.getUrl(), JSON.toJSONString(headerParamMap), JSON.toJSONString(bodyMap));


                            String result = null;
                            try {
                                result = HttpRequest.post(backNotify.getUrl())
                                        .header(Header.USER_AGENT, "CXYGZL")//头信息，多个头信息多次调用此方法即可
                                        .headerMap(headerParamMap, true)
                                        .body(JSON.toJSONString(bodyMap))
                                        .timeout(10000)//超时，毫秒
                                        .execute().body();
                                log.info(" 返回值:{}", result);
                            } catch (Exception ex) {
                                log.error("后置事件异常", e);
                            }

                            if (StrUtil.isNotBlank(result)) {
                                Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
                                });
                                List<HttpSettingData> resultSetting = backNotify.getResult();
                                for (HttpSettingData httpSettingData : resultSetting) {
                                    execution.setVariable(httpSettingData.getValue(), resultMap.get(httpSettingData.getField()));
                                }
                            }

                        }
                    }
                }
            }

        }

        if (event.getType().toString().equals(FlowableEngineEventType.TASK_COMPLETED.toString())) {

            TaskService taskService = SpringUtil.getBean(TaskService.class);

            //任务完成
            FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
            org.flowable.task.service.impl.persistence.entity.TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
            //执行人id
            String assignee = task.getAssignee();

            //nodeid
            String nodeId = task.getTaskDefinitionKey();

            //实例id
            String processInstanceId = task.getProcessInstanceId();

            String processDefinitionId = task.getProcessDefinitionId();
            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);
            ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
            processNodeRecordAssignUserParamDto.setFlowId(flowId);
            processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
            processNodeRecordAssignUserParamDto.setData(JSON.toJSONString(taskService.getVariables(task.getId())));
            processNodeRecordAssignUserParamDto.setLocalData(JSON.toJSONString(taskService.getVariablesLocal(task.getId())));
            processNodeRecordAssignUserParamDto.setNodeId(nodeId);
            processNodeRecordAssignUserParamDto.setUserId((assignee));
            processNodeRecordAssignUserParamDto.setTaskId(task.getId());
            processNodeRecordAssignUserParamDto.setNodeName(task.getName());
            processNodeRecordAssignUserParamDto.setTaskType(ProcessInstanceConstant.TaskType.PASS);

            Object approveResult = task.getVariableLocal(ProcessInstanceConstant.VariableKey.APPROVE_RESULT);
            if (approveResult != null && !Convert.toBool(approveResult)) {
                processNodeRecordAssignUserParamDto.setTaskType(ProcessInstanceConstant.TaskType.REFUSE);
            }

            processNodeRecordAssignUserParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal(ProcessInstanceConstant.VariableKey.APPROVE_DESC)));
            processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());

            CoreHttpUtil.taskEndEvent(processNodeRecordAssignUserParamDto);

        }
        if (event.getType().toString().equals(FlowableEngineEventType.TASK_ASSIGNED.toString())) {
            //任务被指派了人员
            FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
            org.flowable.task.service.impl.persistence.entity.TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
            //执行人id
            String assignee = task.getAssignee();
            //任务拥有者
            String owner = task.getOwner();
            //
            String delegationStateString = task.getDelegationStateString();


            //nodeid
            String taskDefinitionKey = task.getTaskDefinitionKey();

            //实例id
            String processInstanceId = task.getProcessInstanceId();

            String processDefinitionId = task.getProcessDefinitionId();
            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);
            ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
            processNodeRecordAssignUserParamDto.setFlowId(flowId);
            processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
//        processNodeRecordAssignUserParamDto.setData();
            processNodeRecordAssignUserParamDto.setNodeId(taskDefinitionKey);
            processNodeRecordAssignUserParamDto.setUserId((assignee));
            processNodeRecordAssignUserParamDto.setTaskId(task.getId());
            processNodeRecordAssignUserParamDto.setNodeName(task.getName());
            processNodeRecordAssignUserParamDto.setTaskType(StrUtil.equals(DelegationState.PENDING.toString(), delegationStateString) ? "DELEGATION" : (StrUtil.equals(DelegationState.RESOLVED.toString(), delegationStateString) ? "RESOLVED" : ""));
            processNodeRecordAssignUserParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal(ProcessInstanceConstant.VariableKey.APPROVE_DESC)));
            processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());

            CoreHttpUtil.startAssignUser(processNodeRecordAssignUserParamDto);


        }

        if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_STARTED.toString())) {
            //流程开始了
            org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

            ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
            DelegateExecution execution = flowableProcessStartedEvent.getExecution();
            String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();
            String flowId = entity.getProcessDefinitionKey();
            Object variable = execution.getVariable(
                    ProcessInstanceConstant.VariableKey.STARTER);
            String startUserId = (JSON.parseArray(JSON.toJSONString(variable), NodeUser.class).get(0).getId());
            //上级实例id
            String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();

            Map<String, Object> variables = execution.getVariables();
            log.info("流程开始了变量是：{}", JSON.toJSONString(variables));

            {


                ProcessInstanceRecordParamDto processInstanceRecordParamDto = new ProcessInstanceRecordParamDto();
                processInstanceRecordParamDto.setUserId(startUserId);
                processInstanceRecordParamDto.setParentProcessInstanceId(nestedProcessInstanceId);
                processInstanceRecordParamDto.setFlowId(flowId);
                processInstanceRecordParamDto.setProcessInstanceId(processInstanceId);
                processInstanceRecordParamDto.setFormData(JSON.toJSONString(variables));
                CoreHttpUtil.startProcessEvent(processInstanceRecordParamDto);
            }
            {
                //判断前置事件
                FlowSettingDto flowSettingDto = CoreHttpUtil.queryProcessSetting(flowId).getData();
                if (flowSettingDto != null) {
                    HttpSetting frontNotify = flowSettingDto.getFrontNotify();
                    if (frontNotify != null && frontNotify.getEnable()) {

                        Map<String, String> headerParamMap = new HashMap<>();
                        {
                            List<HttpSettingData> headerSetting = frontNotify.getHeader();
                            for (HttpSettingData httpSettingData : headerSetting) {
                                if (httpSettingData.getValueMode()) {
                                    headerParamMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                } else {
                                    Object object = variables.get(httpSettingData.getValue());
                                    headerParamMap.put(httpSettingData.getField(), object == null ? null : (object instanceof String ? Convert.toStr(object) : JSON.toJSONString(object)));
                                }
                            }

                        }


                        Map<String, Object> bodyMap = new HashMap<>();
                        {
                            //存入默认值
                            bodyMap.put("flowId", flowId);
                            bodyMap.put("processInstanceId", processInstanceId);
                            List<HttpSettingData> bodySetting = frontNotify.getBody();
                            for (HttpSettingData httpSettingData : bodySetting) {
                                if (httpSettingData.getValueMode()) {
                                    bodyMap.put(httpSettingData.getField(), httpSettingData.getValue());
                                } else {
                                    bodyMap.put(httpSettingData.getField(), JSON.toJSONString(variables.get(httpSettingData.getValue())));
                                }
                            }

                        }

                        log.info("前置事件url：{} 请求头：{} 请求体：{} ", frontNotify.getUrl(), JSON.toJSONString(headerParamMap), JSON.toJSONString(bodyMap));

                        String result = null;
                        try {
                            result = HttpRequest.post(frontNotify.getUrl())
                                    .header(Header.USER_AGENT, "CXYGZL")//头信息，多个头信息多次调用此方法即可
                                    .headerMap(headerParamMap, true)
                                    .body(JSON.toJSONString(bodyMap))
                                    .timeout(10000)//超时，毫秒
                                    .execute().body();
                            log.info("  返回值:{}", result);
                        } catch (Exception e) {
                            log.error("前置事件异常", e);
                        }

                        if (StrUtil.isNotBlank(result)) {
                            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
                            });
                            List<HttpSettingData> resultSetting = frontNotify.getResult();
                            for (HttpSettingData httpSettingData : resultSetting) {
                                execution.setVariable(httpSettingData.getValue(), resultMap.get(httpSettingData.getField()));
                            }
                        }

                    }
                }
            }


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
