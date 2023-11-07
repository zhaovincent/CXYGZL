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
    private Distinct  distinct;
    private CustomRule  customRule;

    /**
     * 自定义编码
     */
    @Data
    public static class CustomRule{

        private Boolean enable;
        private String prefix;
        private Integer middle;
        private Integer serino;
    }

    /**
     * 是否记录报表
     */
    @Data
    public static class DbRecord{

        private Boolean enable;
    }

    /**
     * 是否去重
     * {@link com.cxygzl.common.constants.ProcessInstanceConstant.ProcessSettingDistinctValueClass}
     */
    @Data
    public static class Distinct{

        private Boolean enable;
        private Integer value;
    }

}
