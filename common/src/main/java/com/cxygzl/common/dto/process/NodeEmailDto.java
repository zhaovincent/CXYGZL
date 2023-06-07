package com.cxygzl.common.dto.process;

import lombok.Data;

import java.util.List;

/**
 * http请求
 */
@Data
public class NodeEmailDto {

    private List<String> to;
    private String subject;
    private String content;

}
