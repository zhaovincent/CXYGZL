package com.cxygzl.common.dto.flow;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-07-25 09:34
 */
@Data
public class AreaFormValue {

    private String code;
    private String name;
    private List<String> value;
    private List<String> nameObj;

}
