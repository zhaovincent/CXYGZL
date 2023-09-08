package com.cxygzl.common.dto.flow;

import lombok.Data;

@Data
public class HttpSettingData {
    private String field;
    private Boolean valueMode;
    private String value;
    private String contentConfig;
}
