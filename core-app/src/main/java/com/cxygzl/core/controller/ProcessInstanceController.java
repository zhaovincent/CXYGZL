package com.cxygzl.core.controller;

import com.cxygzl.common.dto.IndexPageStatistics;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.VariableQueryParamDto;
import com.cxygzl.core.service.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.task.api.TaskQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
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
    private RuntimeService runtimeService;


    @Resource
    private IProcessInstanceService processInstanceService;


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

        long count = runtimeService.createProcessInstanceQuery().processInstanceId(paramDto.getExecutionId()).count();
        if(count>0){

            Map<String, Object> variables = runtimeService.getVariables(paramDto.getExecutionId());


            return R.success(variables);
        }

        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(paramDto.getExecutionId()).list();

        Map<String, Object> variables=new HashMap<>();
        for (HistoricVariableInstance historicVariableInstance : list) {
            variables.put(historicVariableInstance.getVariableName(),historicVariableInstance.getValue());
        }

        return R.success(variables);


    }

    /**
     * 删除流程
     * @param processInstanceParamDto
     * @return
     */
    @PostMapping("delete")
    public R delete(@RequestBody ProcessInstanceParamDto processInstanceParamDto){
        return processInstanceService.delete(processInstanceParamDto);
    }


}
