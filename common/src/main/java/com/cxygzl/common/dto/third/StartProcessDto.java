package com.cxygzl.common.dto.third;

import com.cxygzl.common.dto.flow.FormItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-14 14:06
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StartProcessDto {
    /**
     * 流程id
     */
    private String flowId;


    /**
     * 流程发起人
     */
    private String userId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 值对象集合
     */
    private Map<String,Object> valueMap;
    /**
     * 表单集合
     */
    private List<FormItemVO> formItemVOList;

}
