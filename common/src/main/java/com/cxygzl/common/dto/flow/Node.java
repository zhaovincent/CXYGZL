package com.cxygzl.common.dto.flow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class Node {

    private String id;

    private String tempId;
    private String parentId;
    private String headId;
    private String tailId;
    private String placeHolder;


    private Integer type;

    @JsonProperty(value = "nodeName")
    private String name;

    private Boolean error;

    @JsonProperty("childNode")
    private Node children;
    /**
     * 人员指定类型
     */
    private Integer assignedType;
    /**
     * 是否是多选
     */
    private Boolean multiple;
    /**
     * 多人审批方式
     */
    private Integer multipleMode;

    private Boolean sequential;

    private Object multipleModeValue;

    /**
     * 多实例表单下传递子流程的表单
     */
    private String multipleSubFormId;
    /**
     * 会签完成比例
     */
    private BigDecimal completeRate;
    /**
     * 部门主管级别
     */
    private Integer deptLeaderLevel;
    private String formUserId;
    private String formUserName;
    private List<NodeUser> nodeUserList;
    private List<Node> conditionNodes;
    private List<Node> list;

    private String nodeId;


    private Map<String,String> formPerms;

    private Nobody nobody;

    private SameAsStarter sameAsStarter;
    private Refuse refuse;

    private Boolean mode;
    private Boolean groupRelationMode;

    private Integer priorityLevel;

    private List<GroupCondition> conditionList;

    private HttpSetting httpSetting;

    private Object value;

    private String delayUnit;
    private Object groupRelation;
    private String subFlowId;
    private String subFlowName;
    private Integer starterMode;
    private String starterValue;

    private List<HttpSettingData> pcFormList;
    private List<HttpSettingData> cpFormList;

    private List operList;

}
