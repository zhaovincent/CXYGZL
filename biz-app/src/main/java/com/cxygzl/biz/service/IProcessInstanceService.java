package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.common.dto.PageDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;

/**
 * 流程实例进程
 */
public interface IProcessInstanceService  {
    /**
     * 启动流程
     * @param processInstanceParamDto
     * @return
     */
    Object startProcessInstance(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 查询当前登录用户的待办任务
     * @param pageVO
     * @return
     */
    Object queryMineTask(PageDto pageVO);

    /**
     *  查询已办任务
     * @param pageVO
     * @return
     */
    Object queryMineEndTask(PageDto pageVO);

    /**
     * 流程结束
     * @param processsInstanceId
     * @return
     */
    R end(String processsInstanceId);

    /**
     * 查询我发起的
     * @param pageDto
     * @return
     */
    Object queryMineStarted(PageDto pageDto);

    /**
     * 查询抄送给我的
     * @param pageDto
     * @return
     */
    Object queryMineCC(PageDto pageDto);

    /**
     * 显示流程实例图片
     * @param procInsId
     * @return
     */
    Object showImg(String procInsId);

    /**
     * 格式化流程显示
     * @param nodeFormatParamVo
     * @return
     */
    Object formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo);

    /**
     * 流程详情
     * @param processInstanceId
     * @return
     */
    Object detail(String processInstanceId);
}
