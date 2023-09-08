package com.cxygzl.common.dto.flow;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class Node  implements Cloneable, Serializable {

    private String id;

    private String executionId;
    private String flowUniqueId;

    private String tempId;
    private String parentId;
    private String headId;
    private String tailId;
    private String placeHolder;


    private Integer type;


    private String nodeName;

    private Boolean error;


    private Node childNode;
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

    private String deptUserType;


    private Map<String,String> formPerms;

    private Nobody nobody;

    private SameAsStarter sameAsStarter;
    private Refuse refuse;

    private Boolean mode;
    private Boolean groupRelationMode;

    private Integer priorityLevel;

    private List<GroupCondition> conditionList;

    private HttpSetting httpSetting;
    /**
     * 动态表单配置
     */
    private HttpSetting dynamicFormConfig;

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



    //分支下的最大数量
    private Integer containNodeNum;
    private Integer containBranchNum;
    private Integer nodeBeforeNum;
    private Integer xPosition;
    private Integer yPosition;




}
