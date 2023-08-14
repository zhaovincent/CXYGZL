package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IProcessNodeDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 流程节点数据 前端控制器
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@RestController
@RequestMapping(value = {"processNodeData","api/processNodeData"})
public class ProcessNodeDataController {

    @Resource
    private IProcessNodeDataService processNodeDataService;


}
