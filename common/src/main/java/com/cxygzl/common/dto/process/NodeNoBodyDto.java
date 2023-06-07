package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节点用户对象
 */
@NoArgsConstructor
@Data
public class NodeNoBodyDto {


    private String handler;

    private List<NodeUserDto> assignedUser;

}
