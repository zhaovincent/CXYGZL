package com.cxygzl.common.dto.third;

import com.cxygzl.common.dto.flow.FormItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-14 14:06
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateProcessDto {
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 旧的流程id
     */
    private String oriFlowId;
    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程简介
     */
    private String description;

    /**
     * 表单集合
     */
    private List<FormItemVO> formItemVOList;

}
