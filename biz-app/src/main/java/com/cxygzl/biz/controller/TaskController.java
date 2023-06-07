package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.ITaskService;
import com.cxygzl.common.dto.TaskParamDto;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 任务实例
 */
@RestController
@RequestMapping(value = {"task","api/task"})
public class TaskController {

    @Resource
    private ITaskService taskService;

    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    @SneakyThrows
    @GetMapping("queryTask")
    public Object queryTask(String taskId) {

        return taskService.queryTask(taskId);

    }

    /**
     * 完成任务
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("completeTask")
    public Object completeTask(@RequestBody TaskParamDto completeParamDto) {

        return taskService.completeTask(completeParamDto);

    }



    /**
     * 结束流程
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("stopProcessInstance")
    public Object stopProcessInstance(@RequestBody TaskParamDto completeParamDto) {

        return taskService.stopProcessInstance(completeParamDto);

    }




}
