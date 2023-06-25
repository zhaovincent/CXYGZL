package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程节点数据
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("process_node_data")
public class ProcessNodeData extends BaseEntity{


    /**
     * 流程id
     */
    private String flowId;

    /**
     * 表单数据
     */
    private String data;

    private String nodeId;
}
