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

    private HttpSetting frontNotify;
    private HttpSetting backNotify;


}
