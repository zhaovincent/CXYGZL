package com.cxygzl.biz.controller;

import com.cxygzl.biz.constants.ValidGroup;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.vo.ProcessDataQueryVO;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.dto.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Validated
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
    public R<ProcessVO> getDetail(String flowId,boolean handleForm) {
        return processService.getDetail(flowId,handleForm );
    }

    /**
     * 创建流程
     *
     * @param processVO
     * @return
     */
    @PostMapping("create")
    public R create(@Validated(value= ValidGroup.Crud.Create.class) @RequestBody ProcessVO processVO) {
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
    /**
     * 查询数据列表
     * @param pageDto
     * @return
     */
    @PostMapping("queryDataList")
    public R queryDataList(@RequestBody ProcessDataQueryVO pageDto){
        return processService.queryDataList(pageDto);
    }

}
