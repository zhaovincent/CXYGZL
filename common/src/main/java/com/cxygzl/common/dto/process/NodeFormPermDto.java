package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点用户对象
 */
@NoArgsConstructor
@Data
public class NodeFormPermDto {
    /**
     * 表单id
     */
    private String id;
    /**
     * 表单标题
     */
    private String title;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 表单权限
     */
    private String perm;
    /**
     * 表单值
     */
    private Object value;

}
