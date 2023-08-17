package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.common.dto.R;

/**
 * <p>
 * 流程抄送数据 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
public interface IProcessCopyService extends IService<ProcessCopy> {
    /**
     * 查询单个抄送详细信息
     * @param id
     * @return
     */
    R querySingleDetail(long id);

}
