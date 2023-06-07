package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节点用户对象
 */
@NoArgsConstructor
@Data
public class NodeConditionDto {


    private String title;
    private String id;
    private String valueType;
    private String userType;
    private String userKey;
    private String dateType;
    private String format;
    private String compare;
    private List value;

}
