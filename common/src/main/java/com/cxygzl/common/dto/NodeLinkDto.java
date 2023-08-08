package com.cxygzl.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-08 10:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeLinkDto {

    private String prevNodeId;
    private String prevId;
    private String prevName;

    private String nextNodeId;
    private String nextId;
    private String nextName;



}
