package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.flow.Node;
import lombok.Data;

import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-12 09:47
 */
@Data
public class TaskOperDataResultVO {


    private String processInstanceId;
    private String nodeId;
    private Boolean taskExist;
    private Boolean frontJoinTask;
    private List operList;
    private Node node;
    private Node process;

    private Boolean needSignature;

}
