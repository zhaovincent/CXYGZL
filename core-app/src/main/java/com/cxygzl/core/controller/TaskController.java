package com.cxygzl.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.VariableQueryParamDto;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeFormPermDto;
import com.cxygzl.common.dto.process.NodePropDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.INodeDataStoreHandler;
import com.cxygzl.core.node.NodeDataStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 */
@RestController
@Slf4j
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ManagementService managementService;


    @Resource
    private RuntimeService runtimeService;



    /**
     * 查询任务变量
     * @param paramDto
     * @return
     */
    @PostMapping("queryTaskVariables")
    public R queryTaskVariables(@RequestBody VariableQueryParamDto paramDto){

        List<String> keyList = paramDto.getKeyList();
        if(CollUtil.isEmpty(keyList)){
            TaskQuery taskQuery = taskService.createTaskQuery();

            Task task = taskQuery.taskId(paramDto.getTaskId()).singleResult();
            if(task==null){
                return R.fail("任务不存在");
            }

            Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());

            return R.success(variables);
        }

        Map<String, Object> variables = taskService.getVariables(paramDto.getTaskId(), keyList);
        return R.success(variables);

    }



    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    @GetMapping("queryTask")
    public R queryTask(String taskId, String userId) {

        DelegationState delegationState =null;


        //实例id
        String processInstanceId =null;
        Object delegateVariable = false;


        String processDefinitionId = null;

        //nodeid
        String taskDefinitionKey =null;

        boolean taskExist=true;

        {
            TaskQuery taskQuery = taskService.createTaskQuery();

            Task task = taskQuery.taskId(taskId).taskAssignee(userId).singleResult();
            if (task == null) {
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).taskAssignee(userId).singleResult();
                if(historicTaskInstance==null){
                    return R.fail("任务不存在");
                }
                taskExist=false;
                taskDefinitionKey=historicTaskInstance.getTaskDefinitionKey();
                processInstanceId=historicTaskInstance.getProcessInstanceId();
                processDefinitionId = historicTaskInstance.getProcessDefinitionId();
            }  else{
                processDefinitionId = task.getProcessDefinitionId();
                taskDefinitionKey = task.getTaskDefinitionKey();
                delegationState = task.getDelegationState();
                processInstanceId = task.getProcessInstanceId();
                delegateVariable = taskService.getVariableLocal(taskId, "delegate");


            }
        }




        //流程id
        String processId = NodeUtil.getProcessId(processDefinitionId);
        //获取节点数据
        INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();

        String data = nodeDataStoreHandler.get(processId, taskDefinitionKey);

        NodeDto nodeDto = JSON.parseObject(data, NodeDto.class);

        NodePropDto props = nodeDto.getProps();
        //表单处理
        List<NodeFormPermDto> formPerms = props.getFormPerms();
        for (NodeFormPermDto formPerm : formPerms) {
            if(taskExist){
                Object variable = taskService.getVariable(taskId, formPerm.getId());
                formPerm.setValue(variable);
            }else{
//                HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId);
//                HistoricVariableInstance historicVariableInstance = historicVariableInstanceQuery.variableName(formPerm.getId()).singleResult();
//                if(historicVariableInstance!=null){
//                    formPerm.setValue(historicVariableInstance.getValue());
//                }
            }

        }


        TaskResultDto taskResultDto = new TaskResultDto();
        taskResultDto.setFlowId(processId);
        taskResultDto.setCurrentTask(taskExist);
        taskResultDto.setDelegate(Convert.toBool(delegateVariable,false));
        taskResultDto.setTaskNodeDto(nodeDto);
        taskResultDto.setProcessInstanceId(processInstanceId);
        taskResultDto.setDelegationState(delegationState==null?null:delegationState.toString());


        return R.success(taskResultDto);
    }


    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("/complete")
    public R complete(@RequestBody TaskParamDto taskParamDto) {
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if(CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

        taskService.complete(taskParamDto.getTaskId(), taskParamDto.getParamMap());

        return R.success();
    }



}
