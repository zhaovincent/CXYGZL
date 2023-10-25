package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.PageDto;
import lombok.Data;

import java.util.List;

@Data
public class ProcessDataQueryVO extends PageDto {



    private String flowId;
    /**
     * 流程id
     */
    private List<String> flowIdList;


}
