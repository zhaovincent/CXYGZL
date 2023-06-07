package com.cxygzl.common.dto.process;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 节点属性对象
 */
@NoArgsConstructor
@Data
public class NodePropDto {

    private String assignedType;

    private String mode;

    private String formUser;
    private String groupType;
    private String groupsType;

    private Boolean sign;

    /**
     * 模式完成比例
     * 比如会签 只要50%完成即可
     */
    private BigDecimal modePercentage;

    private String expression;
    private String placeholder;

    private List<String> cids;

    private List<NodeConditionDto> conditions;

    private List<NodeGroupDto> groups;

    private NodeHttpDto http;



    private NodeEmailDto email;

    private NodeNoBodyDto nobody;

    private List<NodeFormMappingDto> formMapping;
    private List<NodeFormMappingDto> inputFormMapping;
    private List<NodeFormMappingDto> outputFormMapping;

    private List<NodeUserDto> assignedUser;
    private List<NodeUserDto> startUser;
    private Integer startUserType;
    private String startUserForm;


    private List<NodeFormPermDto> formPerms;

    private List<Integer> operationBtn;

    private NodeSelfSelectDto selfSelect;

    private NodeLeaderTopDto leaderTop;

    private NodeLeaderDto leader;


    private String type;
    private String time;
    private String unit;
    private String dateTime;

}
