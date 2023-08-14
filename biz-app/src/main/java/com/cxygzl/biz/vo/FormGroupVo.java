package com.cxygzl.biz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author : willian fu
 * @date : 2020/9/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormGroupVo {
    private Long id;
    /**
     * 流程名字
     */
    private String name;

    /**
     * 流程
     */
    private List<FlowVo> items;

    @Data
    @Builder
    @AllArgsConstructor
    public static class FlowVo {

        private String flowId;

        /**
         * 发起范围
         */
        private String rangeShow;
        private String name;

        private String logo;

        private Boolean stop;

        private String remark;

        private Date updated;
    }


}
