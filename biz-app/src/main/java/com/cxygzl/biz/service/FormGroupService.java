package com.cxygzl.biz.service;


import com.cxygzl.biz.entity.Process;

import java.util.List;

/**
 * @author : willian fu
 * @date : 2022/7/4
 */
public interface FormGroupService {

    /**
     * 查询表单组
     *
     * @return 表单组数据
     */
    Object getFormGroups( Boolean hidden);

    /**
     * 查询所有我可以发起的表单组
     * @return
     */
    Object getStartFormGroups( );

    /**
     * 表单及分组排序
     *
     * @param groups 分组数据
     * @return 排序结果
     */
    Object formGroupsSort(List<Integer> groups);

    /**
     * 查询表单模板数据
     *
     * @param templateId 模板id
     * @return 模板详情数据
     */
    Object getFormById(String templateId);


    /**
     * 修改分组
     *
     * @param id   分组ID
     * @param name 分组名
     * @return 修改结果
     */
    Object updateFormGroupName(Long id, String name);

    /**
     * 新增表单分组
     *
     * @param name 分组名
     * @return 添加结果
     */
    Object createFormGroup(String name);

    /**
     * 删除分组
     *
     * @param id 分组ID
     * @return 删除结果
     */
    Object deleteFormGroup(Integer id);

    /**
     * 编辑表单
     *
     * @param formId 摸板ID
     * @param type       类型 stop using delete
     * @return 操作结果
     */
    Object updateForm(String formId, String type, Long groupId);

    /**
     * 编辑表单详情
     *
     * @param template 表单模板信息
     * @return 修改结果
     */
    Object updateFormDetail(Process template);

    Object formsSort(Long groupId, List<String> fromIds);

    Object getFormGroupList();

    Object createForm(Process form);
}
