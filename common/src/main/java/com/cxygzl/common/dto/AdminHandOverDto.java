package com.cxygzl.common.dto;

import com.cxygzl.common.dto.flow.NodeUser;
import lombok.Data;

import java.util.List;

/**
 * 管理员转交
 */
@Data
public class AdminHandOverDto {

    private List<TaskDto> taskDtoList;

    private NodeUser user;

    private String processInstanceId;

}
