package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节点用户对象
 */
@NoArgsConstructor
@Data
public class NodeGroupDto {


    private String groupType;
    private List<NodeConditionDto> conditions;
    private List<String> cids;
}
