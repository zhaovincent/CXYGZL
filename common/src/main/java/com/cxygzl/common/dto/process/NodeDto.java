package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节点对象
 */
@NoArgsConstructor
@Data
public class NodeDto {


    private String headId;

    private String tailId;



    /**
     * 唯一id
     */
    private String id;
    /**
     * 上级id
     */
    private String parentId;
    /**
     * 类型
     */
    private String type;
    /**
     * 节点名字
     */
    private String name;
    /**
     * 表达式
     */
    private String expression;
    /**
     * 节点描述
     */
    private String desc;
    /**
     * 子级
     */
    private NodeDto children;
    /**
     * 节点属性
     */
    private NodePropDto props;
    /**
     * 分支
     */
    private List<NodeDto> branchs;

}
