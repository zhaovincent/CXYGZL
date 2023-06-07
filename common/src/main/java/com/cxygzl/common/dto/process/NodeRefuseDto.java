package com.cxygzl.common.dto.process;

import lombok.Data;

/**
 * 用户任务拒绝之后的操作
 */
@Data
public class NodeRefuseDto {

    /**
     * 类型
     */
    private String type;
    /**
     * 具体到某个节点
     */
    private String target;
}
