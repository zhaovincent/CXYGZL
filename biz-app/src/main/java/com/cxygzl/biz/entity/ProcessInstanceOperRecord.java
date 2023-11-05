package com.cxygzl.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程操作记录
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("`process_instance_oper_record`")
public class ProcessInstanceOperRecord extends BaseEntity {



    /**
     * 用户id
     */
    @TableField("`user_id`")
    private String userId;


    /**
     * 流程id
     */
    @TableField("`flow_id`")
    private String flowId;

    /**
     * 节点id
     */
    @TableField("`node_id`")
    private String nodeId;

    /**
     * 节点名称
     */
    @TableField("`node_name`")
    private String nodeName;

    /**
     * 流程实例id
     */
    @TableField("`process_instance_id`")
    private String processInstanceId;

    /**
     * 评论内容
     */
    @TableField("`comment`")
    private String comment;

    /**
     * 操作类型
     */
    @TableField("`oper_type`")
    private String operType;

    /**
     * 操作描述
     */
    @TableField("`oper_desc`")
    private String operDesc;

    /**
     * 图片列表
     */
    @TableField("`image_list`")
    private String imageList;

    /**
     * 文件列表
     */
    @TableField("`file_list`")
    private String fileList;

}
