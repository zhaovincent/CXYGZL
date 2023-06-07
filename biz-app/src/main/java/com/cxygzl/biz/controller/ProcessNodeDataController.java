package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 保存节点数据
     * @param processNodeDataDto
     * @return
     */
    @PostMapping("saveNodeData")
    public R saveNodeData(@RequestBody ProcessNodeDataDto processNodeDataDto){
        return processNodeDataService.saveNodeData(processNodeDataDto);
    }

    /**
     * 获取节点数据
     * @param processId
     * @param nodeId
     * @return
     */
    @GetMapping("getNodeData")
    public R<String> getNodeData(String processId,String nodeId){
        return processNodeDataService.getNodeData(processId, nodeId);
    }

}
