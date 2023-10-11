package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.QueryFormListParamVo;
import com.cxygzl.common.dto.R;

public interface IBaseService {

    /**
     * 首页数据
     *
     * @return
     */
    R index();

    /**
     * 获取所有地区数据
     *
     * @return
     */
    R areaList();

    /**
     * 同步数据
     *
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

    /**
     * 查询头部显示数据
     *
     * @param nodeFormatParamVo
     * @return
     */
    R queryHeaderShow(QueryFormListParamVo nodeFormatParamVo);

    /**
     * 获取任务操作列表
     * @param taskId
     * @return
     */
    R queryTaskOperData(String taskId);

    /**
     * 查询打印数据
     * @param processInstanceId
     * @return
     */
    R queryPrintData(String processInstanceId);
}
