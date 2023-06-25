package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IBaseService;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"base","api/base"})
public class BaseController {

    @Resource
    private IBaseService baseService;

    /**
     * 首页数据
     * @return
     */
    @GetMapping("index")
    public R index(){
        return baseService.index();
    }

}
