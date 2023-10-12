package com.cxygzl.core.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cxygzl.common.config.NotWriteLogAnno;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.ModelUtil;
import com.cxygzl.core.utils.NodeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.ENABLE_SKIP_EXPRESSION;

/**
 * 工作流控制器
 * 负责流程创建编辑发起等功能
 */
@RestController
@Slf4j
@RequestMapping("flow")
public class FlowController {

    @Autowired
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;


    @Resource
    private RuntimeService runtimeService;

    /**
     * 创建流程
     *
     * @param createFlowDto
     * @return
     */
    @PostMapping("create")
    public R create(@RequestBody CreateFlowDto createFlowDto) {
        String flowId = "p" + RandomUtil.randomString(9) + StrUtil.fillBefore(createFlowDto.getUserId(), '0', 10);


        log.info("flowId={}", flowId);
        BpmnModel bpmnModel = ModelUtil.buildBpmnModel(createFlowDto.getNode(), createFlowDto.getProcessName(), flowId);
        {
            byte[] bpmnBytess = new BpmnXMLConverter().convertToXML(bpmnModel);
            String filename = "/tmp/flowable-deployment/" + flowId + ".bpmn20.xml";
            log.debug("部署时的模型文件：{}", filename);
            FileUtil.writeBytes(bpmnBytess, filename);
        }
        repositoryService.createDeployment()
                .addBpmnModel(StrUtil.format("{}.bpmn20.xml", flowId), bpmnModel).deploy();


        return R.success(flowId);
    }

    /**
     * 启动
     *
     * @param processInstanceParamDto
     * @return
     */
    @PostMapping("/start")
    public R start(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
        String flowId = processInstanceParamDto.getFlowId();
        {
            //前置检查
            R r = frontCheck(processInstanceParamDto);
            if (!r.isOk()) {
                return r;
            }
        }
        Authentication.setAuthenticatedUserId(processInstanceParamDto.getStartUserId());
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        //支持自动跳过
        paramMap.put(ENABLE_SKIP_EXPRESSION, true);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowId,
                paramMap);

        String processInstanceId = processInstance.getProcessInstanceId();
        return R.success(processInstanceId);

    }

    /**
     * 唤醒消息事件
     *
     * @param messageDto
     * @return
     */
    @PostMapping("notifyMsg")
    public R notifyMsg(@RequestBody NotifyMessageDto messageDto) {

        String msgId = NodeDataStoreFactory.getInstance().get("msgId", messageDto.getMessageNotifyId());
        String nodeId = NodeDataStoreFactory.getInstance().get("nodeId", messageDto.getMessageNotifyId());


        Execution execution = runtimeService.createExecutionQuery()
                .activityId(nodeId)
                .processInstanceId(messageDto.getProcessInstanceId())
                .singleResult();

        runtimeService.messageEventReceived(msgId, execution.getId());
        return R.success();
    }

    /**
     * 前置检查
     *
     * @param processInstanceParamDto
     * @return
     */
    private R frontCheck(ProcessInstanceParamDto processInstanceParamDto) {
        String flowId = processInstanceParamDto.getFlowId();
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        //前置检查
        FlowSettingDto flowSettingDto = BizHttpUtil.queryProcessSetting(flowId).getData();

        if (flowSettingDto != null) {
            HttpSetting frontCheck = flowSettingDto.getFrontCheck();
            if (frontCheck != null && frontCheck.getEnable()) {

                String result = HttpUtil.flowExtenstionHttpRequest(frontCheck, paramMap, flowId, null, null);


                if (StrUtil.isNotBlank(result)) {
                    R r = JsonUtil.parseObject(result, new JsonUtil.TypeReference<R>() {
                    });
                    return r;
                } else {
                    return R.fail("网络连接异常");
                }

            }
        }
        return R.success();
    }


    @NotWriteLogAnno(all = true, printResultLog = false)
    @GetMapping("/showImg")
    @SneakyThrows
    public void showImg(String procInsId, HttpServletResponse response) {


        String procDefId;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(procInsId)
                    .singleResult();
            procDefId = historicProcessInstance.getProcessDefinitionId();

        } else {
            procDefId = processInstance.getProcessDefinitionId();
        }

        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);


        // 创建默认的流程图生成器
        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
        // 生成图片的类型
        String imageType = "png";
        // 高亮节点集合
        List<String> highLightedActivities = new ArrayList<>();
        // 高亮连线集合
        List<String> highLightedFlows = new ArrayList<>();
        // 查询所有历史节点信息
        List<HistoricActivityInstance> hisActInsList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .list();


        // 遍历
        hisActInsList.forEach(historicActivityInstance -> {
            if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                // 添加高亮连线
                highLightedFlows.add(historicActivityInstance.getActivityId());
            } else {
                // 添加高亮节点
                highLightedActivities.add(historicActivityInstance.getActivityId());
            }
        });
        // 节点字体
        String activityFontName = "宋体";
        // 连线标签字体
        String labelFontName = "微软雅黑";
        // 连线标签字体
        String annotationFontName = "宋体";
        // 类加载器
        ClassLoader customClassLoader = null;
        // 比例因子，默认即可
        double scaleFactor = 1.0d;
        // 不设置连线标签不会画
        boolean drawSequenceFlowNameWithNoLabelDI = true;

        BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
        bpmnAutoLayout.setTaskHeight(120);
        bpmnAutoLayout.setTaskWidth(120);
        bpmnAutoLayout.execute();
        // 生成图片
        InputStream inputStream = defaultProcessDiagramGenerator.generateDiagram(bpmnModel,
                imageType,
                highLightedActivities,
                highLightedFlows,
                activityFontName,
                labelFontName,
                annotationFontName,
                customClassLoader,
                scaleFactor,
                drawSequenceFlowNameWithNoLabelDI); // 获取输入流
        IoUtil.write(response.getOutputStream(), true, IoUtil.readBytes(inputStream));
//        String content = Base64.encode(inputStream);
        // return R.success(content);
    }


    /**
     * 终止流程
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("stopProcessInstance")
    public R stopProcessInstance(@RequestBody TaskParamDto taskParamDto) {

        List<String> processInstanceIdList = taskParamDto.getProcessInstanceIdList();
        for (String processInstanceId : processInstanceIdList) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstance != null) {
                runtimeService.setVariable(processInstanceId, ProcessInstanceConstant.VariableKey.CANCEL, true);
                List<Execution> executions = runtimeService.createExecutionQuery().parentId(processInstanceId).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                runtimeService.createChangeActivityStateBuilder().moveExecutionsToSingleActivityId(executionIds,
                        ProcessInstanceConstant.VariableKey.END).changeState();
            }
        }


        return R.success();
    }

    /**
     * 查询用户已办任务
     *
     * @param taskQueryParamDto
     * @return
     */
    @PostMapping("/queryCompletedTask")
    public R queryCompletedTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        List<HistoricActivityInstance> list = historicActivityInstanceQuery
                .taskAssignee(taskQueryParamDto.getAssign())
                .finished()
                .orderByHistoricActivityInstanceEndTime().desc()
                .listPage((taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
                        taskQueryParamDto.getPageSize());

        long count = historicActivityInstanceQuery.taskAssignee(taskQueryParamDto.getAssign()).finished().count();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : list) {
            String activityId = historicActivityInstance.getActivityId();
            String activityName = historicActivityInstance.getActivityName();
            String executionId = historicActivityInstance.getExecutionId();
            String taskId = historicActivityInstance.getTaskId();
            Date startTime = historicActivityInstance.getStartTime();
            Date endTime = historicActivityInstance.getEndTime();
            Long durationInMillis = historicActivityInstance.getDurationInMillis();
            String processInstanceId = historicActivityInstance.getProcessInstanceId();

            String processDefinitionId = historicActivityInstance.getProcessDefinitionId();
            //流程id
            String flowId = com.cxygzl.core.utils.NodeUtil.getFlowId(processDefinitionId);


            TaskDto taskDto = new TaskDto();
            taskDto.setFlowId(flowId);
            taskDto.setTaskCreateTime(startTime);
            taskDto.setTaskEndTime(endTime);
            taskDto.setNodeId(activityId);
            taskDto.setExecutionId(executionId);
            taskDto.setProcessInstanceId(processInstanceId);
            taskDto.setDurationInMillis(durationInMillis);
            taskDto.setTaskId(taskId);
            taskDto.setAssign(historicActivityInstance.getAssignee());
            taskDto.setTaskName(activityName);


            taskDtoList.add(taskDto);
        }
        PageResultDto<TaskDto> pageResultDto = new PageResultDto<>();
        pageResultDto.setTotal(count);
        pageResultDto.setRecords(taskDtoList);


        return R.success(pageResultDto);
    }

    /**
     * 查询用户待办任务
     *
     * @param taskQueryParamDto
     * @return
     */
    @PostMapping("/queryAssignTask")
    public R queryAssignTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {

        String assign = taskQueryParamDto.getAssign();

        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> tasks =
                taskQuery.taskAssignee(assign).orderByTaskCreateTime().desc().listPage((taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
                        taskQueryParamDto.getPageSize());
        long count = taskQuery.taskAssignee(assign).count();

        List<TaskDto> taskDtoList = new ArrayList<>();
        log.debug("当前有" + count + " 个任务:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String taskId = task.getId();
            String processInstanceId = task.getProcessInstanceId();
            log.debug((i + 1) + ") (" + taskId + ") " + task.getName() + " processInstanceId={} executrionId={}",
                    processInstanceId, task.getExecutionId());

            Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
            log.debug("任务变量:{}", JSONUtil.toJsonStr(taskServiceVariables));


            //nodeid
            String taskDefinitionKey = task.getTaskDefinitionKey();

            String processDefinitionId = task.getProcessDefinitionId();
            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            TaskDto taskDto = new TaskDto();
            taskDto.setFlowId(flowId);
            taskDto.setTaskCreateTime(task.getCreateTime());
            taskDto.setNodeId(taskDefinitionKey);
            taskDto.setParamMap(taskServiceVariables);
            taskDto.setProcessInstanceId(processInstanceId);
            taskDto.setTaskId(taskId);
            taskDto.setAssign(task.getAssignee());
            taskDto.setTaskName(task.getName());

            taskDtoList.add(taskDto);

        }

        PageResultDto<TaskDto> pageResultDto = new PageResultDto<>();
        pageResultDto.setTotal(count);
        pageResultDto.setRecords(taskDtoList);


        return R.success(pageResultDto);
    }


}
