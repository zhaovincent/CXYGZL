package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IBaseService;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"base", "api/base"})
public class BaseController {

    @Resource
    private IBaseService baseService;

    /**
     * 首页数据
     *
     * @return
     */
    @GetMapping("index")
    public R index() {
        return baseService.index();
    }


    /**
     * 获取所有地区数据
     *
     * @return
     */
    @GetMapping("areaList")
    public R areaList() {
        return baseService.areaList();
    }

    /**
     * 同步数据
     *
     * @return
     */
    @PostMapping("loadRemoteData")
    public R loadRemoteData() {
        return baseService.loadRemoteData();
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    @PostMapping("formatStartNodeShow")
    public R formatStartNodeShow(@RequestBody NodeFormatParamVo nodeFormatParamVo) {
        return baseService.formatStartNodeShow(nodeFormatParamVo);
    }
    /**
     * 查询头部显示数据
     *
     * @param nodeFormatParamVo
     * @return
     */
    @PostMapping("queryHeaderShow")
    public R queryHeaderShow(@RequestBody NodeFormatParamVo nodeFormatParamVo) {
        return baseService.queryHeaderShow(nodeFormatParamVo);
    }
}
