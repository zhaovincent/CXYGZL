package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.ProcessCopy;
import com.baomidou.mybatisplus.extension.service.IService;

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
    Object querySingleDetail(long id);

}
