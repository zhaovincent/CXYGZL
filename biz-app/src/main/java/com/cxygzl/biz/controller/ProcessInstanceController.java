package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IProcessInstanceService;
import com.cxygzl.biz.service.IProcessInstanceUserCopyService;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.ProcessDataQueryVO;
import com.cxygzl.common.dto.*;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 流程实例
 */
@RestController
@RequestMapping(value = {"process-instance", "api/process-instance"})
public class ProcessInstanceController {

    @Resource
    private IProcessInstanceService processInstanceService;

    @Resource
    private IProcessInstanceUserCopyService processInstanceUserCopyService;

    /**
     * 消息通知事件
     *
     * @param messageDto
     * @return
     */
    @PostMapping("notifyMsgEvent")
    public R notifyMsgEvent(@RequestBody NotifyMessageDto messageDto) {
        return processInstanceService.notifyMsgEvent(messageDto);
    }

    /**
     * 启动流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("startProcessInstance")
    public R startProcessInstance(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {

        return processInstanceService.startProcessInstance(processInstanceParamDto);

    }

    /**
     * 查询当前登录用户的待办任务
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineTask")
    public R queryMineTask(@RequestBody PageDto pageDto) {

        return processInstanceService.queryMineTask(pageDto);

    }

    /**
     * 查询当前登录用户已办任务
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineEndTask")
    public R queryMineEndTask(@RequestBody ProcessDataQueryVO pageDto) {

        return processInstanceService.queryMineEndTask(pageDto);

    }

    /**
     * 查询当前登录用户已办任务的流程实例
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineDoneProcessInstance")
    public R queryMineDoneProcessInstance(@RequestBody ProcessDataQueryVO pageDto) {

        return processInstanceService.queryMineDoneProcessInstance(pageDto);

    }

    /**
     * 查询我发起的
     *
     * @param pageDto
     * @return
     */
    @PostMapping("queryMineStarted")
    public R queryMineStarted(@RequestBody ProcessDataQueryVO pageDto) {
        return processInstanceService.queryMineStarted(pageDto);
    }

    /**
     * 发起的流程列表
     *
     * @param pageDto
     * @return
     */
    @PostMapping("queryList")
    public R queryList(@RequestBody ProcessDataQueryVO pageDto) {
        return processInstanceService.queryList(pageDto);
    }

    /**
     * 查询流程实例详情
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping("queryDetailByProcessInstanceId")
    public R queryDetailByProcessInstanceId(String processInstanceId) {
        return processInstanceService.queryDetailByProcessInstanceId(processInstanceId);
    }


    /**
     * 查询抄送我的
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineCC")
    public R queryMineCC(@RequestBody ProcessDataQueryVO pageDto) {
        return processInstanceService.queryMineCC(pageDto);
    }

    /**
     * 查询抄送我的流程实例
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineCCProcessInstance")
    public R queryMineCCProcessInstance(@RequestBody ProcessDataQueryVO pageDto) {
        return processInstanceUserCopyService.queryMineCCProcessInstance(pageDto);
    }

    /**
     * 显示流程图
     *
     * @param procInsId
     * @return
     */

    @SneakyThrows
    @GetMapping("showImg")
    public R showImg(String procInsId) {
        return processInstanceService.showImg(procInsId);
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    @PostMapping("formatStartNodeShow")
    public R formatStartNodeShow(@RequestBody NodeFormatParamVo nodeFormatParamVo) {
        return processInstanceService.formatStartNodeShow(nodeFormatParamVo);
    }

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping("detail")
    public R detail(String processInstanceId) {
        return processInstanceService.detail(processInstanceId);
    }
    /**
     * 导出流程详情
     *
     * @param processInstanceId
     * @return
     */
    @PostMapping("export/{processInstanceId}")
    public R export(@PathVariable String processInstanceId) {
        return processInstanceService.export(processInstanceId);
    }

    /**
     * 结束流程
     *
     * @param processInstanceId
     * @return
     */
    @SneakyThrows
    @PostMapping("stopProcessInstance/{processInstanceId}")
    public R stopProcessInstance(@PathVariable String processInstanceId) {

        return processInstanceService.stopProcessInstance(processInstanceId);

    }
    /**
     * 催办
     *
     * @param completeParamDto
     * @return
     */
    @SneakyThrows
    @PostMapping("urgeProcessInstance")
    public R urgeProcessInstance(@RequestBody TaskParamDto completeParamDto) {

        return processInstanceService.urgeProcessInstance(completeParamDto);

    }


    /**
     * 查询进行中的任务
     *
     * @param processInstanceId
     * @return
     */
    @SneakyThrows
    @PostMapping("queryTaskListInProgress/{processInstanceId}")
    public R queryTaskListInProgress(@PathVariable String processInstanceId) {

        return processInstanceService.queryTaskListInProgress(processInstanceId);

    }


    /**
     *  删除分组
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable String id){
        return processInstanceService.deleteProcessInstance(id);
    }

}
