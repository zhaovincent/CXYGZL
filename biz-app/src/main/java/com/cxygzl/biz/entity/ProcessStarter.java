package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程发起人
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_starter`")
public class ProcessStarter  extends BaseEntity {


    /**
     * 用户id或者部门id
     */
    @TableField("`type_id`")
    private String typeId;

    /**
     *  类型 user dept
     */
    @TableField("`type`")
    private String type;

    /**
     * 流程id
     */
    @TableField("`process_id`")
    private Long processId;

    /**
     * 数据
     */
    @TableField("`data`")
    private String data;
}
