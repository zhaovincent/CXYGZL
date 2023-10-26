package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessInstanceUserCopy;
import com.cxygzl.biz.vo.ProcessDataQueryVO;
import com.cxygzl.common.dto.R;

public interface IProcessInstanceUserCopyService extends IService<ProcessInstanceUserCopy> {


    /**
     * 查询抄送给我的(根据实例id去重)
     * @param pageDto
     * @return
     */
    R queryMineCCProcessInstance(ProcessDataQueryVO pageDto);

}
