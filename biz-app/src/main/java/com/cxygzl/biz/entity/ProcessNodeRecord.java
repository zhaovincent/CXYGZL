package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 流程节点记录
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Getter
@Setter
@TableName("process_node_record")
public class ProcessNodeRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 流程id
     */
    private String flowId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 表单数据
     */
    private String data;

    private String nodeId;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点名字
     */
    private String nodeName;

    /**
     * 节点状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private String executionId;

}
