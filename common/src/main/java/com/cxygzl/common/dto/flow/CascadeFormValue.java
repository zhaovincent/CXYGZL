package com.cxygzl.common.dto.flow;

import lombok.Data;

import java.util.List;

/**
 * 级联
 */
@Data
public class CascadeFormValue {

    private String key;
    private String label;
    private List<String> value;
    private List<String> labelList;

}
