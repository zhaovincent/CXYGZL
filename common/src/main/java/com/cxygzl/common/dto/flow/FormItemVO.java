package com.cxygzl.common.dto.flow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormItemVO {

    private String id;

    private String perm;

    private String icon;

    private String name;

    private String type;


    private Boolean required;
    /**
     * 是否可以打印
     */
    private Boolean printable;

    private String typeName;

    private String placeholder;

    private Props props;

    @Data
    public static class Props{
        private Object value;
        private Object options;
        private Boolean self;
        private Boolean multi;
        private Boolean defaultRoot;
        private Object oriForm;
        private Integer minLength;
        private Integer maxLength;
        private Integer radixNum;
        private Integer maxSize;
        private Boolean showChinese;
        private String regex;
        private String regexDesc;
        private String unit;
        private Boolean halfSelect;
        private List expList;
        /**
         * 是否使用上次的内容
         * 签名表单
         */
        private Boolean lastContent;

        private Object suffixArray;
        private Object max;
        private Object min;
    }

}
