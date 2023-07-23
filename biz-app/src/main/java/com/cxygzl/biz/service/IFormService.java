package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.FormRemoteSelectOptionParamVo;

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

}
