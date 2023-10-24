package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.SignatureRecord;
import com.cxygzl.biz.mapper.SignatureRecordMapper;
import com.cxygzl.biz.service.ISignatureRecordService;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-24 10:27
 */
@Slf4j
@Service
public class SignatureRecordServiceImpl extends ServiceImpl<SignatureRecordMapper,SignatureRecord> implements ISignatureRecordService {
    /**
     * 保存记录
     *
     * @param signatureRecord
     * @return
     */
    @Override
    public R create(SignatureRecord signatureRecord) {
        signatureRecord.setUserId(StpUtil.getLoginIdAsString());
        save(signatureRecord);
        return R.success();
    }
}
