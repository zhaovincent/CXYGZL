package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.common.dto.R;

public interface IBaseService {

    /**
     * 首页数据
     * @return
     */
    R index();

    /**
     * 获取所有地区数据
     * @return
     */
    R areaList();

    /**
     * 同步数据
     * @return
     */
    R loadRemoteData();

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    R formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo);
}
