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
public class ProcessDto {

    private String flowId;

    private String oriFlowId;

    private String name;

    private String description;

    private List<FormItemVO> formItemVOList;

}
