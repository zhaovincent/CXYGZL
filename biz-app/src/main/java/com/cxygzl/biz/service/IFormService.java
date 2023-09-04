package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.FormRemoteSelectOptionParamVo;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskDto;

/**
 * 表单服务
 */
public interface IFormService {
    /**
     *
     *  远程请求下拉选项
     * @param formRemoteSelectOptionParamVo
     * @return
     */
    Object selectOptions( FormRemoteSelectOptionParamVo formRemoteSelectOptionParamVo);

    /**
     * 获取表单数据
     *
     * @param taskDto
     * @param handleForm
     * @return
     */
    R getFormList(TaskDto taskDto,boolean handleForm);

}
