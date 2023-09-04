package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IFormService;
import com.cxygzl.biz.vo.FormRemoteSelectOptionParamVo;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 表单相关控制器
 */
@RestController
@RequestMapping(value = {"form", "api/form"})
public class FormController {
    @Resource
    private IFormService formService;

    /**
     * 获取下拉选项
     *
     * @return
     */
    @PostMapping("selectOptions")
    public Object selectOptions(@RequestBody FormRemoteSelectOptionParamVo formRemoteSelectOptionParamVo) {

        return formService.selectOptions(formRemoteSelectOptionParamVo);
    }

    /**
     * 获取表单数据
     *
     * @param taskDto
     * @param handleForm
     * @return
     */
    @PostMapping("getFormList")
    public R getFormList(@RequestBody TaskDto taskDto, boolean handleForm) {
        return formService.getFormList(taskDto, handleForm);
    }
}
