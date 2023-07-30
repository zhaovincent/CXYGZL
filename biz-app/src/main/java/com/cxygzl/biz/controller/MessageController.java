package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IMessageService;
import com.cxygzl.common.config.NotWriteLogAnno;
import com.cxygzl.common.dto.MessageDto;
import com.cxygzl.common.dto.R;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 消息控制器
 */
@RestController
@RequestMapping(value = {"message", "api/message"})
public class MessageController {

    @Resource
    private IMessageService messageService;


    /**
     * 未读数量
     */
    @NotWriteLogAnno(printResultLog = false)
    @SneakyThrows
    @GetMapping("/unreadNum")
    public R<Long> queryUnreadNum() {
        return messageService.queryUnreadNum();
    }

    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @PostMapping("queryList")
    public R queryList(@RequestBody MessageDto pageDto) {
        return messageService.queryList(pageDto);
    }

    /**
     * 删除消息
     *
     * @param messageDto
     * @return
     */
    @DeleteMapping("delete")
    public R delete(@RequestBody com.cxygzl.common.dto.MessageDto messageDto) {
        return messageService.delete(messageDto);
    }

    /**
     * 置为已读
     *
     * @param messageDto
     * @return
     */
    @PostMapping("read")
    public R read(@RequestBody com.cxygzl.common.dto.MessageDto messageDto) {
        return messageService.read(messageDto);
    }


}
