package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.PageDto;
import com.cxygzl.common.dto.flow.NodeUser;
import lombok.Data;

import java.util.List;

@Data
public class ProcessDataQueryVO extends PageDto {



    private String flowId;
    private String processBizCode;
    /**
     * 流程id
     */
    private List<String> flowIdList;
    private List<String> finishTime;
    private List<String> startTime;
    private List<NodeUser> starterList;
    private Integer status;


}
