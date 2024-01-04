package com.cxygzl.core.listeners.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.common.constants.MessageTypeEnum;
import com.cxygzl.common.constants.NodeExpireSettingTypeEnum;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.ExpireSetting;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.FlowableUtils;
import com.cxygzl.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.job.service.impl.persistence.entity.JobEntityImpl;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时器执行了--暂时只有用户任务过期
 *
 * @author Huijun Zhao
 * @description
 * @date 2024-01-03 16:26
 */
@Slf4j
@Component
public class TimeFiredEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {

        log.info("任务超时了。。。");

        FlowableEntityEventImpl e = (FlowableEntityEventImpl) event;
        Object entity = e.getEntity();
        JobEntityImpl jobEntity = (JobEntityImpl) entity;

        String processInstanceId = e.getProcessInstanceId();
        String processDefinitionId = e.getProcessDefinitionId();
        //流程id
        String flowId = NodeUtil.getFlowId(processDefinitionId);


        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);



        //获取对应的节点id  非定时器的

        //定时器的节点id
        String jobHandlerConfiguration = jobEntity.getJobHandlerConfiguration();
        JSONObject jsonObject = JsonUtil.parseObject(jobHandlerConfiguration);
        String activityId = jsonObject.getString("activityId");

        FlowNode flowNode = FlowableUtils.getFlowNode(processInstanceId, activityId);

        String nodeId = FlowableUtils.getNodeIdFromExtension(flowNode);

        TaskService taskService = SpringUtil.getBean(TaskService.class);

        //获取流程名称
        String flowName = FlowableUtils.getFlowName(processDefinitionId);


        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);
        if (node != null) {
            if (node.getType().intValue() == NodeTypeEnum.APPROVAL.getValue()) {

                //查询所有进行中的用户任务
                List<Task> taskListAll = taskService.createTaskQuery()
                        .processInstanceId(processInstanceId)
                        .list();
                List<Task> taskList =
                        taskListAll.stream().filter(w -> w.getTaskDefinitionKey().equals(nodeId)).collect(Collectors.toList());

                //审批节点
                ExpireSetting expireSetting = node.getExpireSetting();
                if (expireSetting != null && expireSetting.getEnable()) {
                    //启用了
                    if(expireSetting.getType()== NodeExpireSettingTypeEnum.PASS.getValue()){
                        //自动通过
                        for (Task task : taskList) {

                            Dict param = Dict.create().set(StrUtil.format("{}_approve_condition", nodeId), true);
                            taskService.complete(task.getId(),param);
                        }

                    }
                    if(expireSetting.getType()== NodeExpireSettingTypeEnum.REFUSE.getValue()){
                        //自动拒绝
                        for (Task task : taskList) {
                            Dict param = Dict.create().set(StrUtil.format("{}_approve_condition", nodeId), false);

                            taskService.complete(task.getId(),param);
                        }

                    }
                    if(expireSetting.getType()== NodeExpireSettingTypeEnum.NOTIFY.getValue()){
                        //发送消息
                        for (Task task : taskList) {
                            Map<String, Object> processVariables = runtimeService.getVariables(task.getExecutionId());

                            //发起人
                            Object rootUserObj = processVariables.get(ProcessInstanceConstant.VariableKey.STARTER);
                            NodeUser rootUser = JsonUtil.parseArray(JsonUtil.toJSONString(rootUserObj), NodeUser.class).get(0);

                            MessageDto messageDto = MessageDto.builder()
                                    .userId(task.getAssignee())
                                    .flowId(flowId)
                                    .processInstanceId(processInstanceId)

                                    .title("任务超时提醒")
                                    .content(StrUtil.format("[{}]提交给您的的任务[{}]已超时，请及时处理",
                                            rootUser.getName(),
                                            flowName))
                                    .uniqueId(task.getId())
                                    .param(JsonUtil.toJSONString(processVariables))

                                    .type(MessageTypeEnum.EXPIRED_TASK.getType())
                                    .readed(false).build();

                            BizHttpUtil.saveMessage(messageDto);
                        }


                    }

                }
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.TIMER_FIRED.toString());

    }
}
