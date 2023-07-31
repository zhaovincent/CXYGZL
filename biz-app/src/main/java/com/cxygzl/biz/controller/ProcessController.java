package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"process", "api/process"})
public class ProcessController {

    @Resource
    private IProcessService processService;

    /**
     * 获取详细数据
     *
     * @param flowId
     * @return
     */
    @GetMapping("getDetail")
    public R<ProcessVO> getDetail(String flowId) {
        return processService.getDetail(flowId);
    }

    /**
     * 创建流程
     *
     * @param processVO
     * @return
     */
    @PostMapping("create")
    public R create(@RequestBody ProcessVO processVO) {
        return processService.create(processVO);
    }



    /**
     * 编辑表单
     *
     * @param flowId 摸板ID
     * @param type       类型 stop using delete
     * @return 操作结果
     */
    @PutMapping("update/{flowId}")
   public R update(@PathVariable String flowId,
                 @RequestParam String type,
                 @RequestParam(required = false) Long groupId){
        return processService.update(flowId, type, groupId);
    }


}
