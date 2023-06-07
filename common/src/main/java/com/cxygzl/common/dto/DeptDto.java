package com.cxygzl.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
@Getter
@Setter
public class DeptDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    private Long id;

    /**
     * 部门名
     */
    private String name;

    /**
     * 上级部门id
     */
    private Long parentId;

    /**
     * 逻辑删除字段
     */
    private Boolean delFlag;

    /**
     * 主管user_id
     */
    private Long leaderUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
