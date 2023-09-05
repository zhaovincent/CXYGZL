package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.FormRemoteSelectOptionParamVo;
import com.cxygzl.biz.vo.QueryFormListParamVo;
import com.cxygzl.common.dto.R;

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
    R getFormList(QueryFormListParamVo taskDto, boolean handleForm);

}
