package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程-执行id关系
 * </p>
 *
 * @author Vincent
 * @since 2023-08-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_execution`")
public class ProcessExecution  extends BaseEntity {


    /**
     * 执行id
     */
    @TableField("`execution_id`")
    private String executionId;

    /**
     *  子级执行id
     */
    @TableField("`child_execution_id`")
    private String childExecutionId;
}
