package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`dept`")
public class Dept  extends BaseEntity {

    /**
     * 部门名
     */
    @TableField("`name`")
    private String name;

    /**
     * 上级部门id
     */
    @TableField("`parent_id`")
    private Long parentId;


    /**
     * 主管user_id
     */
    @TableField("`leader_user_id`")
    private Long leaderUserId;


    @TableField("`status`")
    private Integer status;
    @TableField("`sort`")
    private Integer sort;
}
