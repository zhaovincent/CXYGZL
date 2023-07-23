package com.cxygzl.common.dto.third;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户字段
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserFieldDto  {


    /**
     * 用户名
     */
    private String name;


    /**
     * 字段类型
     */
    private String type;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 配置json字符串
     */
    private String props;

    /**
     * 字段
     */
    private String key;
}
