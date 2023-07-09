package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.ICombinationGroupService;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 组聚合接口控制器
 */
@RestController
@RequestMapping(value = {"combination/group","api/combination/group"})
public class CombinationGroupController {

    @Resource
    private ICombinationGroupService combinationGroupService;


    /**
     * 查询表单组包含流程
     *
     * @return 表单组数据
     */
    @GetMapping("listGroupWithProcess")
    public R listGroupWithProcess(Boolean hidden){
        return combinationGroupService.listGroupWithProcess(hidden);
    }

    /**
     * 查询所有我可以发起的表单组
     *
     * @return
     */
    @GetMapping("listCurrentUserStartGroup")
    public R listCurrentUserStartGroup(){
        return combinationGroupService.listCurrentUserStartGroup();
    }
}
