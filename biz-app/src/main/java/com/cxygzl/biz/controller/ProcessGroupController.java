package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.ProcessGroup;
import com.cxygzl.biz.service.IProcessGroupService;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = {"processGroup", "api/processGroup"})
public class ProcessGroupController {

    @Resource
    private IProcessGroupService processGroupService;

    /**
     * 组列表
     *
     * @return
     */
    @GetMapping("list")
    public R<List<ProcessGroup>> queryList() {
        return processGroupService.queryList();
    }

    /**
     * 新增流程分组
     *
     * @param processGroup 分组名
     * @return 添加结果
     */
    @PostMapping("create")
    public R create(@RequestBody ProcessGroup processGroup) {
        return processGroupService.create(processGroup);
    }


    /**
     *  删除分组
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable long id){
        return processGroupService.delete(id);
    }
}
