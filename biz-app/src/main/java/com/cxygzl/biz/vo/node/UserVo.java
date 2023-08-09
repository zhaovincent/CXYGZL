package com.cxygzl.biz.vo.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    /**
     * 用户od
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;


    private Date showTime;
    private String showTimeStr;


    private String avatar;



    private String operType;
    /**
     * 状态 1进行中2已完成
     */
    private Integer status=0;

}
