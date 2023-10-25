package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.ProcessDataQueryVO;
import com.cxygzl.common.dto.NotifyMessageDto;
import com.cxygzl.common.dto.PageDto;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.R;

/**
 * 流程实例进程
 */
public interface IProcessInstanceService  {
    /**
     * 消息通知事件
     * @param messageDto
     * @return
     */
    R notifyMsgEvent(NotifyMessageDto messageDto);
    /**
     * 启动流程
     *
     * @param processInstanceParamDto
     * @return
     */
    R startProcessInstance(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 查询当前登录用户的待办任务
     *
     * @param pageVO
     * @return
     */
    R queryMineTask(PageDto pageVO);

    /**
     *  查询已办任务
     * @param pageVO
     * @return
     */
    R queryMineEndTask(ProcessDataQueryVO pageVO);

    /**
     * 流程结束
     *
     * @param processInstanceParamDto
     * @return
     */
    R end(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 查询我发起的
     * @param pageDto
     * @return
     */
    R queryMineStarted(ProcessDataQueryVO pageDto);

    /**
     * 查询抄送给我的
     * @param pageDto
     * @return
     */
    R queryMineCC(ProcessDataQueryVO pageDto);

    /**
     * 显示流程实例图片
     *
     * @param procInsId
     * @return
     */
    R showImg(String procInsId);

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    R formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo);

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    R detail(String processInstanceId);


}
