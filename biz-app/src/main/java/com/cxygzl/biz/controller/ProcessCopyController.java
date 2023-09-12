package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IProcessInstanceCopyService;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = {"processCopy","api/processCopy"})
public class ProcessCopyController {
    @Resource
    private IProcessInstanceCopyService processCopyService;

    /**
     * 查询单个抄送详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("querySingleDetail")
    public Object querySingleDetail(long id) {
        return processCopyService.querySingleDetail(id);
    }

}
