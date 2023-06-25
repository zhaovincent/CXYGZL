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
     * 前加签
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("delegateTask")
    public Object delegateTask(@RequestBody TaskParamDto completeParamDto) {

        return taskService.delegateTask(completeParamDto);

    }


    /**
     * 加签完成任务
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("resolveTask")
    public Object resolveTask(@RequestBody TaskParamDto completeParamDto) {

        return taskService.resolveTask(completeParamDto);

    }

    /**
     * 设置执行人
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("setAssignee")
    public Object setAssignee(@RequestBody TaskParamDto completeParamDto) {

        return taskService.setAssignee(completeParamDto);

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


    /**
     * 退回
     * @param taskParamDto
     * @return
     */
    @PostMapping("back")
    public Object back(@RequestBody TaskParamDto taskParamDto){
        return taskService.back(taskParamDto);
    }



}
