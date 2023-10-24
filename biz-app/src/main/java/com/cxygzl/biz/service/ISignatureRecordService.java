package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.SignatureRecord;
import com.cxygzl.common.dto.R;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-24 10:26
 */
public interface ISignatureRecordService extends IService<SignatureRecord> {
    /**
     * 保存记录
     * @param signatureRecord
     * @return
     */
    R create(SignatureRecord signatureRecord);

}
