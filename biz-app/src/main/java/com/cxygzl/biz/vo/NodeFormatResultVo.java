package com.cxygzl.biz.vo;

import com.cxygzl.biz.vo.node.NodeVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeFormatResultVo {

    private List<NodeVo> processNodeShowDtoList;
    /**
     * 是否禁止选择用户
     */
    private Boolean disableSelectUser;
    /**
     * 要选择用户的节点集合
     */
    private List<String> selectUserNodeIdList;

}
