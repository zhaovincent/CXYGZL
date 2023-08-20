package com.cxygzl.common.dto.flow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String typeName;

    private String placeholder;

    private Props props;

    @Data
    public static class Props{
        private Object value;
        private Object options;
        private Boolean self;
        private Boolean multi;
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

        private Object suffixArray;
        private Object max;
        private Object min;
    }

}
