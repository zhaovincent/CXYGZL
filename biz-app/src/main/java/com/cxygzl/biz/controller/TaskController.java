package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.ITaskService;
import com.cxygzl.common.dto.AdminHandOverDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务实例
 */
@RestController
@RequestMapping(value = {"task", "api/task"})
public class TaskController {

    @Resource
    private ITaskService taskService;


    /**
     * 提交评论
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("submitComment")
    public R submitComment(@RequestBody TaskParamDto completeParamDto) {

        return taskService.submitComment(completeParamDto);

    }

    /**
     * 完成任务
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("completeTask")
    public R completeTask(@RequestBody TaskParamDto completeParamDto) {

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
    public R delegateTask(@RequestBody TaskParamDto completeParamDto) {

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
    public R resolveTask(@RequestBody TaskParamDto completeParamDto) {

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
    public R setAssignee(@RequestBody TaskParamDto completeParamDto) {

        return taskService.setAssignee(completeParamDto);

    }

    /**
     * 加签
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("addAssignee")
    public R addAssignee(@RequestBody TaskParamDto completeParamDto) {

        return taskService.addAssignee(completeParamDto);

    }

    /**
     * 减签
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("delAssignee")
    public R delAssignee(@RequestBody TaskParamDto completeParamDto) {

        return taskService.delAssignee(completeParamDto);

    }


    /**
     * 退回
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("back")
    public R back(@RequestBody TaskParamDto taskParamDto) {
        return taskService.back(taskParamDto);
    }


    /**
     * 撤回
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("revoke")
    public R revoke(@RequestBody TaskParamDto taskParamDto) {
        return taskService.revoke(taskParamDto);
    }


    /**
     * 管理员设置执行人--转交
     *
     * @param adminHandOverDto
     * @return
     */
    @PostMapping("setAssigneeByAdmin")
    public R setAssigneeByAdmin(@RequestBody AdminHandOverDto adminHandOverDto) {
        return taskService.setAssigneeByAdmin(adminHandOverDto);
    }

}
