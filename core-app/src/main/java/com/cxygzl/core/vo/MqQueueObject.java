package com.cxygzl.core.vo;

import lombok.Data;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-12 11:04
 */
@Data
public class MqQueueObject {

    private Object object;
    private String url;
    private String key;

}
