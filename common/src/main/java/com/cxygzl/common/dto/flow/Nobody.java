package com.cxygzl.common.dto.flow;

import lombok.Data;

import java.util.List;

@Data
public class Nobody {

    private String handler;

    private List<NodeUser> assignedUser;

}
