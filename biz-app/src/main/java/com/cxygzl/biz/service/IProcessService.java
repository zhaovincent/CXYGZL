package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.dto.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
public interface IProcessService extends IService<Process> {
    /**
     * 获取详细数据
     *
     * @param flowId
     * @param handleForm
     * @return
     */
    R<ProcessVO> getDetail(String flowId,boolean handleForm);


    Process getByFlowId(String flowId);

    void updateByFlowId(Process process);

    void hide(String flowId);

    /**
     * 创建流程
     *
     * @param processVO
     * @return
     */
    R create(ProcessVO processVO);




    /**
     * 编辑表单
     *
     * @param flowId 摸板ID
     * @param type       类型 stop using delete
     * @return 操作结果
     */
    R update(String flowId, String type, Long groupId);
}
