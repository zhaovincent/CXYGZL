package com.cxygzl.common.dto.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Node {

    private String id;
    private String parentId;
    private String headId;
    private String tailId;
    private String placeHolder;

    private Refuse refuse;

    private Integer type;

    private String nodeName;

    private Boolean error;

    private Node childNode;

    private Integer assignedType;
    private Boolean multiple;
    private Integer multipleMode;
    private Integer deptLeaderLevel;
    private String formUserId;
    private String deptUserType;
    private String formUserName;
    private List<NodeUser> nodeUserList;
    private List<Node> conditionNodes;

    private Map<String,String> formPerms;

    private Nobody nobody;

    private Boolean groupMode;

    private List<GroupCondition> conditionList;
    /**
     * 过期设置
     */
    @ApiModelProperty("过期设置")
    private ExpireSetting expireSetting;
}
