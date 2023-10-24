package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.SignatureRecord;
import com.cxygzl.biz.service.ISignatureRecordService;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 签名记录
 */

@RestController
@RequestMapping(value = {"signatureRecord","api/signatureRecord"})
public class SignatureRecordController {

    @Resource
    private ISignatureRecordService service;

    /**
     * 保存签名记录
     * @param signatureRecord
     * @return
     */
    @PostMapping("/create")
    public R create(@RequestBody SignatureRecord signatureRecord){
        return service.create(signatureRecord);
    }

}
