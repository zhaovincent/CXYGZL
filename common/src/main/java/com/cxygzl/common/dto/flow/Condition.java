package com.cxygzl.common.dto.flow;

import lombok.Data;

/**
 * 条件
 */
@Data
public class Condition {

    private String key;

    private String expression;

    private Object value;

    private String keyType;

}
