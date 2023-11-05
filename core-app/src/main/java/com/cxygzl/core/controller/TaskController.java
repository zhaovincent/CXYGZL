package com.cxygzl.core.controller;

import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.VariableQueryParamDto;
import com.cxygzl.core.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 任务控制器
 */
@RestController
@Slf4j
@RequestMapping("task")
public class TaskController {

    @Autowired
    private ITaskService taskService;



    /**
     * 查询任务变量
     *
     * @param paramDto
     * @return
     */
    @PostMapping("queryTaskVariables")
    public R queryTaskVariables(@RequestBody VariableQueryParamDto paramDto) {
        return  taskService.queryTaskVariables(paramDto);

    }

    /**
     * 查询任务评论
     *
     * @param paramDto
     * @return
     */
    @PostMapping("queryTaskComments")
    public R queryTaskComments(@RequestBody VariableQueryParamDto paramDto) {
            return taskService.queryTaskComments(paramDto);
    }
    /**
     * 查询流程实例评论
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping("queryProcessInstanceComments")
    public R queryProcessInstanceComments(String processInstanceId){
        return taskService.queryProcessInstanceComments(processInstanceId);
    }

    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    @GetMapping("queryTask")
    public R queryTask(String taskId, String userId) {
        return taskService.queryTask(taskId, userId);
    }


    /**
     * 提交评论
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @PostMapping("/submitComment")
    public R submitComment(@RequestBody TaskParamDto taskParamDto) {
        return taskService.submitComment(taskParamDto);
    }
    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @PostMapping("/complete")
    public R complete(@RequestBody TaskParamDto taskParamDto) {
        return taskService.complete(taskParamDto);
    }

    /**
     * 前加签
     *
     * @return
     */
    @PostMapping("delegateTask")
    public R delegateTask(@RequestBody TaskParamDto taskParamDto) {
        return taskService.delegateTask(taskParamDto);
    }

    /**
     * 加签任务完成
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("resolveTask")
    public R resolveTask(@RequestBody TaskParamDto taskParamDto) {
        return taskService.resolveTask(taskParamDto);
    }

    /**
     * 设置执行人 后加签
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("setAssignee")
    public R setAssignee(@RequestBody TaskParamDto taskParamDto) {
        return taskService.setAssignee(taskParamDto);
    }


    /**
     * 添加执行人
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("addAssignee")
    public R addAssignee(@RequestBody TaskParamDto taskParamDto) {
        return taskService.addAssignee(taskParamDto);
    }



    /**
     * 删除执行人
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("delAssignee")
    public R delAssignee(@RequestBody TaskParamDto taskParamDto) {
        return taskService.delAssignee(taskParamDto);
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


}
