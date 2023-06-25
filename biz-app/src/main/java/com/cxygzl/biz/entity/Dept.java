package com.cxygzl.biz.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
public class Dept extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 部门名
     */
    private String name;

    /**
     * 上级部门id
     */
    private Long parentId;



    /**
     * 主管user_id
     */
    private Long leaderUserId;


    private Integer status;
    private Integer sort;
}
