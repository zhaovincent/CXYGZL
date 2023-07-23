package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_group`")
public class ProcessGroup  extends BaseEntity {


    /**
     * 分组名
     */
    @TableField("`group_name`")
    private String groupName;

    /**
     * 排序
     */
    @TableField("`sort`")
    private Integer sort;
}
