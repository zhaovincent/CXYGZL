package com.cxygzl.biz.service;


import com.cxygzl.common.dto.R;

/**
 * 聚合接口
 */
public interface ICombinationGroupService {

    /**
     * 查询表单组包含流程
     *
     * @return 表单组数据
     */
    R listGroupWithProcess(Boolean hidden);

    /**
     * 查询所有我可以发起的表单组
     *
     * @return
     */
    R listCurrentUserStartGroup( );

}
