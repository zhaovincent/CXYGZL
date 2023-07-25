package com.cxygzl.biz.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.service.IMessageService;
import com.cxygzl.common.config.NotWriteLogAnno;
import com.cxygzl.common.dto.R;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

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
    @SneakyThrows
    @GetMapping("/unreadNum")
    public R<Long> queryUnreadNum() {
        return messageService.queryUnreadNum();
    }
}
