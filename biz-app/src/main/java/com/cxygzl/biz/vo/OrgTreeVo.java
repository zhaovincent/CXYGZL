package com.cxygzl.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgTreeVo    {



    /**
     * 用户od
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 选择
     */
    private Boolean selected;


    private String avatar;

    private Integer status;

}
