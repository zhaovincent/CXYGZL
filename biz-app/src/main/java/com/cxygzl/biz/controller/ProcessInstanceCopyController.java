package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IProcessInstanceCopyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 流程抄送数据 前端控制器
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@RestController
@RequestMapping(value = {"processInstanceCopy","api/processInstanceCopy"})
public class ProcessInstanceCopyController {
    @Resource
    private IProcessInstanceCopyService processCopyService;



}
