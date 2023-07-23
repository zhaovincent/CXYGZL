package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 流程关联下的子流程
 * </p>
 *
 * @author cxygzl
 * @since 2023-07-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_sub_process`")
public class ProcessSubProcess extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 流程id
     */
    @TableField("`flow_id`")
    private String flowId;

    /**
     * 子流程id
     */
    @TableField("`sub_flow_id`")
    private String subFlowId;
}
