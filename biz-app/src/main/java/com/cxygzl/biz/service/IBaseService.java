package com.cxygzl.biz.service;

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
}
