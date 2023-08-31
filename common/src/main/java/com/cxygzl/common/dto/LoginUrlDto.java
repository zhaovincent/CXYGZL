package com.cxygzl.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUrlDto {
    /**
     * 是否是内部地址  如果是内部地址，如下url类似于 /page/a 否则就是完整的http请求
     */
    private Boolean innerUrl;

    private String url;

}
