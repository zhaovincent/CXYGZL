package com.cxygzl.common.dto.process;

import lombok.Data;

import java.util.List;

/**
 * http请求
 */
@Data
public class NodeHttpDto {

    private List<NodeHttpHeaderDto> headers;
    private List<NodeHttpHeaderDto> params;
    private List<NodeHttpHeaderDto> result;

    private String method;

    private Boolean status;

    private String url;
    private String contentType;



    private String success;
    private String fail;

}
