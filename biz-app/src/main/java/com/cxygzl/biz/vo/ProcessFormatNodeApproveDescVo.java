package com.cxygzl.biz.vo;

import com.cxygzl.biz.vo.node.UserVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 节点审批描述对象
 * @author Huijun Zhao
 * @description
 * @date 2023-08-10 10:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessFormatNodeApproveDescVo {

    private String desc;

    private String descType;
    private Boolean sys;
    private String descTypeStr;
    private String showTimeStr;
    private UserVo user;

    private Date date;

}
