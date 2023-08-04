package com.cxygzl.core.controller;

import com.cxygzl.common.dto.IndexPageStatistics;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.VariableQueryParamDto;
import com.cxygzl.core.service.IProcessService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.task.api.TaskQuery;
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
@RequestMapping("process-instance")
public class ProcessInstanceController {

    @Autowired
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ManagementService managementService;

//    @Resource
    private IProcessService processService;


    @Resource
    private RuntimeService runtimeService;

    @PostMapping("calApprovePath")
    public R<List<FlowElement>> calApprovePath(String processInstanceId, String modelId,@RequestBody Map<String, Object> variableMap) {
        return processService.calApprovePath(processInstanceId, modelId, variableMap);
    }

    /**
     * 查询统计数量
     *
     * @param userId
     * @return
     */
    @GetMapping("querySimpleData")
    public R<IndexPageStatistics> querySimpleData(String userId) {
        TaskQuery taskQuery = taskService.createTaskQuery();

        //待办数量
        long pendingNum = taskQuery.taskAssignee((userId)).count();
        //已完成任务
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        long completedNum = historicActivityInstanceQuery.taskAssignee(String.valueOf(userId)).finished().count();


        IndexPageStatistics indexPageStatistics = IndexPageStatistics.builder().pendingNum(pendingNum).completedNum(completedNum).build();

        return R.success(indexPageStatistics);
    }

    /**
     * 查询变量
     *
     * @param paramDto
     * @return
     */
    @PostMapping("queryVariables")
    public R queryVariables(@RequestBody VariableQueryParamDto paramDto) {


        Map<String, Object> variables = runtimeService.getVariables(paramDto.getExecutionId());

        return R.success(variables);


    }


}
