package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessGroup;
import com.cxygzl.biz.service.FormGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : willian fu
 * @date : 2020/9/17
 */
@RestController
@RequestMapping(value = {"admin/flow","api/admin/flow"})
public class FormGroupController {

    @Autowired
    private FormGroupService formGroupService;


    /**
     * 查询所有表单分组
     * @return
     */
    @GetMapping("group")
    public Object getFormGroups(Boolean hidden){
        return formGroupService.getFormGroups(hidden);
    }

    /**
     * 查询所有我可以发起的表单分组
     * @return
     */
    @GetMapping("startGroup")
    public Object getStartFormGroups(){
        return formGroupService.getStartFormGroups();
    }

    /**
     * 查询所有表单分组
     * @return
     */
    @GetMapping("group/list")
    public Object getFormGroupList(){
        return formGroupService.getFormGroupList();
    }

    /**
     * 表单分组排序
     * @param groups 分组数据
     * @return 排序结果
     */
    @PutMapping("group/sort")
    public Object formGroupsSort(@RequestBody List<Integer> groups){
        return formGroupService.formGroupsSort(groups);
    }

    /**
     * 表单排序
     * @param groupId 分组数据
     * @param fromIds 表单ID
     * @return 排序结果
     */
    @PutMapping("sort/{groupId}")
    public Object formsSort(@PathVariable Long groupId,
                            @RequestBody List<String> fromIds){
        return formGroupService.formsSort(groupId, fromIds);
    }

    /**
     * 修改分组
     * @param id 分组ID
     * @param name 分组名
     * @return 修改结果
     */
    @PutMapping("group")
    public Object updateFormGroupName(@RequestParam Long id,
                                       @RequestParam String name){
        return formGroupService.updateFormGroupName(id, name);
    }

    /**
     * 新增表单分组
     * @param name 分组名
     * @return 添加结果
     */
    @PostMapping("createGroup")
    public Object createFormGroup(@RequestBody ProcessGroup processGroup){
        return formGroupService.createFormGroup(processGroup);
    }

    /**
     * 删除分组
     * @param groupId 分组ID
     * @return 删除结果
     */
    @DeleteMapping("group/{groupId}")
    public Object deleteFormGroup(@PathVariable long groupId){
        return formGroupService.deleteFormGroup(groupId);
    }
    /**
     * 删除分组
     * @return 删除结果
     */
    @DeleteMapping("group")
    public Object deleteFormGroup1( long id){
        return formGroupService.deleteFormGroup(id);
    }

    @PostMapping("create")
    public Object createFlow(@RequestBody Process form){
        return formGroupService.createFlow(form);
    }

    /**
     * 查询表单模板数据
     * @param flowId 模板id
     * @return 模板详情数据
     */
    @GetMapping("detail/{flowId}")
    public Object getFormById(@PathVariable String flowId){
        return formGroupService.getFormById(flowId);
    }

    /**
     * 编辑表单
     * @param flowId 摸板ID
     * @param type 类型 stop using delete
     * @return 操作结果
     */
    @PutMapping("{flowId}")
    public Object updateForm(@PathVariable String flowId,
                             @RequestParam String type,
                             @RequestParam(required = false) Long groupId){
        return formGroupService.updateForm(flowId, type, groupId);
    }

}
