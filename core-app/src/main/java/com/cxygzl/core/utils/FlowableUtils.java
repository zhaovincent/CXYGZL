package com.cxygzl.core.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.constants.TaskTypeEnum;
import com.cxygzl.common.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.api.history.HistoricVariableInstanceQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FlowableUtils {

    /**
     * 在未结束的流程中 查询审批通过的数据
     *
     * @param processInstanceId
     * @param userId
     * @return
     */
    public static List<TaskDto> queryApproveDataAtUnFinishedProcess(String processInstanceId, String userId) {

        HistoryService historyService = SpringUtil.getBean(HistoryService.class);
        TaskService taskService = SpringUtil.getBean(TaskService.class);


        HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService.createHistoricVariableInstanceQuery();

        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        List<HistoricActivityInstance> list = historicActivityInstanceQuery
                .taskAssignee(userId)
                .processInstanceId(processInstanceId).list();

        List<TaskDto> taskDtoList = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : list) {
            Object variableLocal = null;


            String taskId = historicActivityInstance.getTaskId();
            if (taskService.createTaskQuery().taskId(taskId).count() > 0) {
                variableLocal = taskService.getVariableLocal(taskId, ProcessInstanceConstant.VariableKey.TASK_TYPE);

            } else {
                HistoricVariableInstance historicVariableInstance = historicVariableInstanceQuery.taskId(taskId).variableName(ProcessInstanceConstant.VariableKey.TASK_TYPE).singleResult();
                variableLocal = historicVariableInstance.getValue();
            }


            String taskType = Convert.toStr(variableLocal);
            log.info("用户：{} 流程实例：{} 任务类型：{}  节点：{}", userId, processInstanceId, taskType, historicActivityInstance.getActivityName());
            if (StrUtil.equals(taskType, TaskTypeEnum.PASS.getValue())) {
                TaskDto taskDto = new TaskDto();
                taskDto.setAssign(userId);
                taskDto.setNodeId(historicActivityInstance.getActivityId());
                taskDto.setTaskName(historicActivityInstance.getActivityName());
                taskDtoList.add(taskDto);
            }
        }

        return taskDtoList;

    }

    public static Map<String, List<ExtensionElement>> generateFlowNodeIdExtensionMap(String nodeId) {
        Map<String, List<ExtensionElement>> extensionElements = new HashMap<>();
        ExtensionElement extensionElement = generateFlowNodeIdExtension(nodeId);
        extensionElements.put(ProcessInstanceConstant.VariableKey.SYS_CODE, CollUtil.newArrayList(extensionElement));
        return extensionElements;
    }


    public static ExtensionElement generateFlowNodeIdExtension(String nodeId) {
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setElementText(nodeId);
        extensionElement.setName("nodeId");
        extensionElement.setNamespacePrefix("flowable");
        extensionElement.setNamespace("nodeId");
        return extensionElement;
    }


    public static String getNodeIdFromExtension(FlowElement flowElement) {

        Map<String, List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();

        return extensionElements.get("nodeId").get(0).getElementText();
    }


    public static void getLastNode(String flowId) {

    }


    public static ExtensionAttribute generate(String key, String val) {
        ExtensionAttribute ea = new ExtensionAttribute();
        ea.setNamespace("flowable");
        ea.setName(key);
        ea.setNamespacePrefix("custom");
        ea.setValue(val);
        return ea;
    }

    /**
     * 获取节点数据
     *
     * @param processInstanceId
     * @param nodeId
     * @return
     */
    public static FlowNode getFlowNode(String processInstanceId, String nodeId) {

        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);

        String definitionld = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();        //获取bpm（模型）对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionld);
        //传节点定义key获取当前节点
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(nodeId);
        return flowNode;
    }

}
