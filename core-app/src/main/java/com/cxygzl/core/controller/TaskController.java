package com.cxygzl.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.VariableQueryParamDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.utils.FlowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
        String flowId = NodeUtil.getFlowId(processDefinitionId);

        Map<String, Object> variableAll=new HashMap<>();

        //表单处理


            if(taskExist){


                Map<String, Object> variables = taskService.getVariables(taskId);
                variableAll.putAll(variables);

            }else{

            }



        TaskResultDto taskResultDto = new TaskResultDto();
        taskResultDto.setFlowId(flowId);
        taskResultDto.setNodeId(taskDefinitionKey);
        taskResultDto.setCurrentTask(taskExist);
        taskResultDto.setDelegate(Convert.toBool(delegateVariable,false));
        taskResultDto.setVariableAll(variableAll);
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

    /**
     * 前加签
     * @param taskId
     * @param userId
     * @return
     */
    @PostMapping("delegateTask")
    public R delegateTask(@RequestBody TaskParamDto taskParamDto){
//
//
//        String taskId = taskCompleteParamDto.getTaskId();
//        TaskQuery taskQuery = taskService.createTaskQuery();
//
//        Task task = taskQuery.taskId(taskId).singleResult();
//
//        String newNodeId = "node_new_"+ RandomUtil.randomString(8);
//
//        runtimeService.setVariable(task.getExecutionId(), StrUtil.format("{}_assignee_default_list", newNodeId),
//                CollUtil.newArrayList(Dict.create().set("id",taskCompleteParamDto.getUserId()))
//                );
//
//        managementService.executeCommand(new InjectMultiInstanceUserTaskCmd(
//                task.getProcessInstanceId(),
//                task.getTaskDefinitionKey(),
//                new DynamicUserTaskBuilder()
//                        .id(newNodeId)
//                        .name("测试新增节点")
//        ));
//        String newNodeId2 = "node_new_"+ RandomUtil.randomString(8);
//        runtimeService.setVariable(task.getExecutionId(), StrUtil.format("{}_assignee_default_list", newNodeId2),
//                CollUtil.newArrayList(Dict.create().set("id",task.getAssignee()))
//        );
//
//        managementService.executeCommand(new InjectMultiInstanceUserTaskCmd(
//                task.getProcessInstanceId(),
//                newNodeId,
//                new DynamicUserTaskBuilder()
//                        .id(newNodeId2)
//                        .name("测试新增2节点")
//        ));
//


        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if(CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }
        taskService.setOwner(taskParamDto.getTaskId(), taskParamDto.getUserId());

        taskService.delegateTask(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());
        return R.success();
    }

    /**
     * 加签任务完成
     * @param taskParamDto
     * @return
     */
    @PostMapping("resolveTask")
    public R resolveTask(@RequestBody TaskParamDto taskParamDto){
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if(CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

        taskService.resolveTask(taskParamDto.getTaskId(), taskParamDto.getParamMap());
        return R.success();
    }

    /**
     * 设置执行人 后加签
     * @param taskParamDto
     * @return
     */
    @PostMapping("setAssignee")
    public R setAssignee(@RequestBody TaskParamDto taskParamDto){
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if(CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

        taskService.setOwner(taskParamDto.getTaskId(), taskParamDto.getUserId());
        taskService.setAssignee(taskParamDto.getTaskId(), taskParamDto.getTargetUserId());
        return R.success();
    }


    /**
     * 退回
     * @param taskParamDto
     * @return
     */
    @PostMapping("back")
    public R back(@RequestBody TaskParamDto taskParamDto){
        String taskId = taskParamDto.getTaskId();
        String targetKey = taskParamDto.getTargetNodeId();



        if (taskService.createTaskQuery().taskId(taskId).singleResult().isSuspended()) {
            return R.fail("任务处于挂起状态");
        }



        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if(StrUtil.equals(targetKey,"root")){
            targetKey=StrUtil.format("{}_start_user_task", targetKey);
            runtimeService.setVariable(task.getExecutionId(),"root_start_user_task_auto_complete",false);
        }

        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(targetKey)) {
                    target = flowElement;
                }
            }
        }



        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, targetKey, null, null);
        if (!isSequential) {
            return R.fail("当前节点相对于目标节点，不属于串行关系，无法回退");
        }



        // 获取所有正常进行的执行任务节点的活动ID，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Execution> runExecutionList = runtimeService.createExecutionQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runActivityIdList = new ArrayList<>();
        runExecutionList.forEach(item -> {
            if (StrUtil.isNotBlank(item.getActivityId())) {
                runActivityIdList.add(item.getActivityId());
            }
        });
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runExecutionList 比对，获取需要撤回的任务
        List<FlowElement> currentFlowElementList = FlowableUtils.iteratorFindChildUserTasks(target, runActivityIdList, null, null);
        currentFlowElementList.forEach(item -> currentIds.add(item.getId()));


        // 2021.03.03修改：添加需撤回的节点为网关时，添加网关的删除信息
        AtomicReference<List<HistoricActivityInstance>> tmp = new AtomicReference<>();
        // currentIds 为活动ID列表
        // currentExecutionIds 为执行任务ID列表
        // 需要通过执行任务ID来设置驳回信息，活动ID不行
        final String tk=targetKey;
        List<String> currentExecutionIds = new ArrayList<>();
        currentIds.forEach(currentId -> runExecutionList.forEach(runExecution -> {
            if (StrUtil.isNotBlank(runExecution.getActivityId()) && currentId.equals(runExecution.getActivityId())) {
                currentExecutionIds.add(runExecution.getId());
                // 查询当前节点的执行任务的历史数据
                tmp.set(historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).executionId(runExecution.getId()).activityId(runExecution.getActivityId()).list());
                // 如果这个列表的数据只有 1 条数据
                // 网关肯定只有一条，且为包容网关或并行网关
                // 这里的操作目的是为了给网关在扭转前提前加上删除信息，结构与普通节点的删除信息一样，目的是为了知道这个网关也是有经过跳转的
                if (tmp.get() != null && tmp.get().size() == 1 && StrUtil.isNotBlank(tmp.get().get(0).getActivityType())
                        && ("parallelGateway".equals(tmp.get().get(0).getActivityType()) || "inclusiveGateway".equals(tmp.get().get(0).getActivityType()))) {
                    // singleResult 能够执行更新操作
                    // 利用 流程实例ID + 执行任务ID + 活动节点ID 来指定唯一数据，保证数据正确

                    historyService.createNativeHistoricActivityInstanceQuery().sql("UPDATE ACT_HI_ACTINST SET " +
                            "DELETE_REASON_ = 'Change activity to "+ tk +"'  WHERE PROC_INST_ID_='"+ task.getProcessInstanceId() +"' AND EXECUTION_ID_='"+ runExecution.getId() +"' AND ACT_ID_='"+ runExecution.getActivityId() +"'").singleResult();
                }
            }
        }));
        // 设置驳回信息
        AtomicReference<Task> atomicCurrentTask = new AtomicReference<>();
        currentExecutionIds.forEach(item -> {
            atomicCurrentTask.set(taskService.createTaskQuery().executionId(currentExecutionIds.get(0)).singleResult());
            // 类型为网关时，获取用户任务为 null
            if (atomicCurrentTask.get() != null) {
                taskService.addComment(atomicCurrentTask.get().getId(), task.getProcessInstanceId(), "taskStatus", "return");
                taskService.addComment(atomicCurrentTask.get().getId(), task.getProcessInstanceId(), "taskMessage", "已退回");
                taskService.addComment(atomicCurrentTask.get().getId(), task.getProcessInstanceId(), "taskComment", "流程回退到" + atomicCurrentTask.get().getName() + "节点");
            }
        });

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId()).moveActivityIdsToSingleActivityId(currentIds, targetKey).changeState();
        } catch (FlowableObjectNotFoundException e) {
            return  R.fail("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            return R.fail("无法取消或开始活动");
        }
        return R.success();
    }




}
