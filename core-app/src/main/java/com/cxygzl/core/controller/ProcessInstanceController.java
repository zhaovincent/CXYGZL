package com.cxygzl.core.controller;

import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.VariableQueryParamDto;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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


    @Resource
    private RuntimeService runtimeService;


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
