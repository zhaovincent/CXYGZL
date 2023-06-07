package com.cxygzl.common.dto.process;

import lombok.Data;

/**
 * http请求头
 */
@Data
public class NodeHttpHeaderDto {

    private Boolean isField;

    private String name;

    private String value;

}
