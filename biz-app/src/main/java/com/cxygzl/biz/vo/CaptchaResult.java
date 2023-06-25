package com.cxygzl.biz.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 验证码响应对象
 *
 * @author haoxr
 * @since 2023/03/24
 */
@Builder
@Data
public class CaptchaResult {

    private String verifyCodeKey;

    private String verifyCodeBase64;

}
