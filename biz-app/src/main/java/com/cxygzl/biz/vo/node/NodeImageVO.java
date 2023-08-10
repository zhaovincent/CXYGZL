package com.cxygzl.biz.vo.node;

import com.cxygzl.common.dto.flow.Node;
import lombok.Data;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-10 17:37
 */
@Data
public class NodeImageVO  extends Node {


    private Integer x;
    private Integer y;

    //分支下最大的数量
    private Integer maxChildrenNumAtBranch;

    //分支数量
    private Integer branchNum;
    //子级数量
    private Integer childrenNum;
}
