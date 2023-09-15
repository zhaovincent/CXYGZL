package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IProcessInstanceService;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.common.dto.NotifyMessageDto;
import com.cxygzl.common.dto.PageDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;
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
    public R queryMineEndTask(@RequestBody PageDto pageDto) {

        return processInstanceService.queryMineEndTask(pageDto);

    }

    /**
     * 查询我发起的
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineStarted")
    public R queryMineStarted(@RequestBody PageDto pageDto) {
        return processInstanceService.queryMineStarted(pageDto);
    }

    /**
     * 查询抄送我的
     *
     * @param pageDto
     * @return
     */
    @SneakyThrows
    @PostMapping("queryMineCC")
    public R queryMineCC(@RequestBody PageDto pageDto) {
        return processInstanceService.queryMineCC(pageDto);
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
}
