package com.cxygzl.common.dto.flow;

import lombok.Data;

import java.util.List;

@Data
public class HttpSetting {

    private Boolean enable;
    private String url;
    private List<HttpSettingData> header;
    private List<HttpSettingData> body;
    private List<HttpSettingData> result;
}
