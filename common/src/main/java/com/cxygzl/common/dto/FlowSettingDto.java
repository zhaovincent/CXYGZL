package com.cxygzl.common.dto;

import com.cxygzl.common.dto.flow.HttpSetting;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程设置
 */
@NoArgsConstructor
@Data
public class FlowSettingDto {

    /**
     * 前置通知
     */
    private HttpSetting frontNotify;
    /**
     * 后置通知
     */
    private HttpSetting backNotify;
    /**
     * 前置检查
     */
    private HttpSetting frontCheck;

    private DbRecord  dbRecord;

    @Data
    public static class DbRecord{

        private Boolean enable;
    }

}
