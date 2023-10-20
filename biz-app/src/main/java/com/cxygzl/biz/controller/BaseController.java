package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IBaseService;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.QueryFormListParamVo;
import com.cxygzl.biz.vo.WebVersionVO;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = {"base", "api/base"})
public class BaseController {

    @Resource
    private IBaseService baseService;

    /**
     * 修改前端版本号
     *
     * @param webVersionVO
     * @return
     */
    @PostMapping("setWebVersion")
    public R setWebVersion(@RequestBody WebVersionVO webVersionVO) {
        return baseService.setWebVersion(webVersionVO);
    }


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
    public R queryHeaderShow(@RequestBody QueryFormListParamVo nodeFormatParamVo) {
        return baseService.queryHeaderShow(nodeFormatParamVo);
    }

    /**
     * 获取任务操作列表
     *
     * @param taskId
     * @return
     */
    @GetMapping("queryTaskOperData")
    public R queryTaskOperData(String taskId) {
        return baseService.queryTaskOperData(taskId);
    }

    /**
     * 查询打印数据
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping("queryPrintData")
    public R queryPrintData(String processInstanceId) {
        return baseService.queryPrintData(processInstanceId);
    }
}
