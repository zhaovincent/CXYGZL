package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 流程发起人
 * </p>
 *
 * @author Vincent
 * @since 2023-05-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("process_starter")
public class ProcessStarter {

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField( fill = FieldFill.INSERT)
    private Boolean delFlag;
    /**
     * 创建时间
     */
    @TableField( fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    private Long processId;

    /**
     * 用户id或者部门id
     */
    private Long typeId;

    /**
     *  类型 user dept
     */
    private String type;
}
