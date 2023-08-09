package com.cxygzl.biz.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.biz.vo.node.UserVo;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.NodeUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 节点格式化显示工具
 */
public class NodeFormatUtil {

    private static String nodeDateShow(Date date) {
        if (date == null) {
            return "";
        }
        if (DateUtil.isSameDay(date, new Date())) {
            return DateUtil.format(date, "HH:mm");
        }

        if (DateUtil.year(date) == DateUtil.year(new Date())) {
            return DateUtil.format(date, "MM-dd HH:mm");
        }
        return DateUtil.format(date, "yyyy-MM-dd HH:mm");

    }


    /**
     * 格式化流程节点显示
     *
     * @param node
     * @param endUniqueId
     * @param beingUniqueId
     * @param cancelUniqueId
     * @param processInstanceId
     * @param paramMap
     */
    public static List<NodeVo> formatProcessNodeShow(Node node,
                                                     Set<String> endUniqueId,
                                                     Set<String> beingUniqueId,
                                                     Set<String> cancelUniqueId,
                                                     String processInstanceId,
                                                     Map<String, Object> paramMap) {
        List<NodeVo> list = new ArrayList();

        if (!NodeUtil.isNode(node)) {
            return list;
        }

        String name = node.getNodeName();
        Integer type = node.getType();


        //SELF_SELECT


        NodeVo nodeVo = new NodeVo();
        nodeVo.setId(node.getId());
        nodeVo.setName(name);
        nodeVo.setType(type);
        nodeVo.setStatus(NodeStatusEnum.WKS.getCode());
        String executionId = node.getExecutionId();
        if (StrUtil.isNotBlank(executionId)) {
            if (endUniqueId.contains(StrUtil.format("{}@@{}@@{}", node.getId(), executionId, node.getFlowUniqueId()))) {
                nodeVo.setStatus(NodeStatusEnum.YJS.getCode());

            } else if (beingUniqueId.contains(StrUtil.format("{}@@{}@@{}", node.getId(), executionId, node.getFlowUniqueId()))) {
                nodeVo.setStatus(NodeStatusEnum.JXZ.getCode());

            } else if (cancelUniqueId.contains(StrUtil.format("{}@@{}@@{}", node.getId(), executionId, node.getFlowUniqueId()))) {
                nodeVo.setStatus(NodeStatusEnum.YCX.getCode());

            }
        }

        {

            nodeVo.setPlaceholder(node.getPlaceHolder());

        }
        if(StrUtil.isAllNotBlank(processInstanceId,node.getExecutionId(),node.getFlowUniqueId())){
            IProcessNodeRecordService processNodeRecordService = SpringUtil.getBean(IProcessNodeRecordService.class);
            ProcessNodeRecord processNodeRecord = processNodeRecordService.lambdaQuery()
                    .eq(ProcessNodeRecord::getExecutionId, node.getExecutionId())
                    .eq(ProcessNodeRecord::getFlowUniqueId, node.getFlowUniqueId())
                    .eq(ProcessNodeRecord::getNodeId, node.getId())
                    .eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId).one();
            if(processNodeRecord!=null){
                nodeVo.setShowTimeStr(nodeDateShow(processNodeRecord.getStartTime()));
                if(processNodeRecord.getEndTime()!=null){
                    nodeVo.setShowTimeStr(nodeDateShow(processNodeRecord.getEndTime()));

                }
            }
        }

        IProcessNodeRecordApproveDescService processNodeRecordApproveDescService = SpringUtil.getBean(IProcessNodeRecordApproveDescService.class);

        List<UserVo> userVoList = new ArrayList<>();
        if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

            Integer assignedType = node.getAssignedType();

            boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
            nodeVo.setSelectUser(selfSelect);
            if (selfSelect) {
                nodeVo.setMultiple(node.getMultiple());
            }

            // 用户列表
            if (StrUtil.isNotBlank(processInstanceId) && StrUtil.isNotBlank(node.getExecutionId())) {

                IProcessExecutionService processExecutionService = SpringUtil.getBean(IProcessExecutionService.class);
                List<ProcessExecution> processExecutionList = processExecutionService.lambdaQuery()
                        .eq(ProcessExecution::getExecutionId, node.getExecutionId())
                        .list();
                List<String> childExecutionIdList = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toList());

                IProcessNodeRecordAssignUserService processNodeRecordAssignUserService = SpringUtil.getBean(IProcessNodeRecordAssignUserService.class);
                List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService
                        .lambdaQuery().
                        eq(ProcessNodeRecordAssignUser::getNodeId, node.getId())
                        .in(ProcessNodeRecordAssignUser::getExecutionId, childExecutionIdList)
                        .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processInstanceId)
                        .orderByAsc(ProcessNodeRecordAssignUser::getCreateTime)
                        .list();


                Set<String> userIdSet = processNodeRecordAssignUserList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

                //  Map<String, List<ProcessNodeRecordAssignUser>> map = processNodeRecordAssignUserList.stream().collect(Collectors.groupingBy(w -> w.getTaskId()));

                for (String userId : userIdSet) {

                    ProcessNodeRecordAssignUser w = processNodeRecordAssignUserList.stream().filter(k -> StrUtil.equals(k.getUserId(), userId))
                            .findFirst().get();
                    List<ProcessNodeRecordApproveDesc> approveDescList = processNodeRecordApproveDescService.lambdaQuery()
                            .eq(ProcessNodeRecordApproveDesc::getNodeId, w.getNodeId())
                            .eq(ProcessNodeRecordApproveDesc::getTaskId, w.getTaskId())
                            .eq(ProcessNodeRecordApproveDesc::getProcessInstanceId, w.getProcessInstanceId())
                            .eq(ProcessNodeRecordApproveDesc::getUserId, w.getUserId()).list();

                    UserVo userVo = buildUser((userId));
                    userVo.setShowTime(w.getEndTime());
                    userVo.setShowTimeStr(nodeDateShow(w.getEndTime()));
                    userVo.setStatus(w.getStatus());
                    userVo.setOperType(w.getTaskType());

                    userVoList.add(userVo);
                }

                if (processNodeRecordAssignUserList.isEmpty()) {
                    if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                        //发起人自己
                        userVoList.addAll(CollUtil.newArrayList(buildRootUser(processInstanceId)));
                    }
                    if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT) {
                        //发起人自选
                        Object variable = paramMap.get(StrUtil.format("{}_assignee_select", node.getId()));
                        if (variable == null) {
                            variable = new ArrayList<>();
                        }
                        List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);

                        List<String> collect = nodeUserDtos.stream().map(w -> (w.getId())).collect(Collectors.toList());
                        for (String aLong : collect) {
                            UserVo userVo = buildUser(aLong);
                            userVoList.addAll(CollUtil.newArrayList(userVo));
                        }
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.USER) {
                //指定用户

                List<NodeUser> nodeUserList = node.getNodeUserList();
                List<UserVo> tempList = buildUser(nodeUserList);
                userVoList.addAll(tempList);


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FORM_USER) {
                //表单人员
                String formUser = node.getFormUserId();

                Object o = paramMap.get(formUser);
                if (o != null) {
                    String jsonString = JSON.toJSONString(o);
                    if (StrUtil.isNotBlank(jsonString)) {
                        List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
                        List<String> userIdList =
                                nodeUserDtoList.stream().map(w -> (w.getId())).collect(Collectors.toList());
                        for (String aLong : userIdList) {
                            userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
                        }
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                //发起人自己
                userVoList.addAll(CollUtil.newArrayList(buildUser(StpUtil.getLoginIdAsString())));
            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.LEADER) {
                //制定主管

                //指定主管审批
                //第几级主管审批
                Integer level = node.getDeptLeaderLevel();

                //去获取主管

                IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);

                R<List<com.cxygzl.common.dto.third.DeptDto>> r = remoteService.queryParentDepListByUserId(StpUtil.getLoginIdAsString());

                List<com.cxygzl.common.dto.third.DeptDto> deptDtoList = r.getData();
                if (CollUtil.isNotEmpty(deptDtoList)) {
                    if (deptDtoList.size() >= level) {
                        com.cxygzl.common.dto.third.DeptDto deptDto = deptDtoList.get(level - 1);


                        userVoList.addAll(CollUtil.newArrayList(buildUser(deptDto.getLeaderUserId())));

                    }
                }

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.LEADER_TOP) {

                //指定主管审批
                //第几级主管审批
                Integer level = node.getDeptLeaderLevel();

                //去获取主管

                IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);

                R<List<com.cxygzl.common.dto.third.DeptDto>> r = remoteService.queryParentDepListByUserId(StpUtil.getLoginIdAsString());

                List<com.cxygzl.common.dto.third.DeptDto> deptDtoList = r.getData();


                if (CollUtil.isNotEmpty(deptDtoList)) {
                    int index = 1;
                    for (DeptDto deptDto : deptDtoList) {
                        if (level != null && level < index) {
                            break;
                        }
                        userVoList.addAll(CollUtil.newArrayList(buildUser(deptDto.getLeaderUserId())));

                        index++;
                    }
                }
            }


        } else if (node.getType().intValue() == NodeTypeEnum.ROOT.getValue()) {
            //发起节点
            if (StrUtil.isBlank(processInstanceId)) {
                UserVo userVo = buildUser(StpUtil.getLoginIdAsString());

                userVoList.addAll(CollUtil.newArrayList(userVo));

            } else {

                IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
                ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


                UserVo userVo = buildRootUser(processInstanceId);
                userVo.setShowTime(processInstanceRecord.getCreateTime());
                userVo.setShowTimeStr(nodeDateShow(processInstanceRecord.getCreateTime()));
                userVo.setStatus(NodeStatusEnum.YJS.getCode());
                userVoList.addAll(CollUtil.newArrayList(userVo));

            }
        } else if (node.getType().intValue() == NodeTypeEnum.CC.getValue()) {
            //抄送节点

            List<NodeUser> nodeUserList = node.getNodeUserList();

            List<UserVo> tempList = buildUser(nodeUserList);
            userVoList.addAll(tempList);

        }
        nodeVo.setUserVoList(userVoList);


        List<NodeVo> branchList = new ArrayList<>();

        List<Node> branchs = node.getConditionNodes();

        if (NodeTypeEnum.getByValue(type).getBranch() && CollUtil.isNotEmpty(branchs)) {
            //条件分支

            for (Node branch : branchs) {
                Node children = branch.getChildNode();
                List<NodeVo> processNodeShowDtos = formatProcessNodeShow(children, endUniqueId, beingUniqueId, cancelUniqueId, processInstanceId, paramMap);

                NodeVo p = new NodeVo();
                p.setChildren(processNodeShowDtos);

                p.setPlaceholder(branch.getPlaceHolder());
                branchList.add(p);
            }
        }
        nodeVo.setBranch(branchList);


        list.add(nodeVo);

        List<NodeVo> next = formatProcessNodeShow(node.getChildNode(), endUniqueId, beingUniqueId, cancelUniqueId, processInstanceId, paramMap);
        list.addAll(next);


        return list;
    }


    /**
     * 根据实例id
     *
     * @param processInstanceId
     * @return
     */
    private static UserVo buildRootUser(String processInstanceId) {

        IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
        String userId = processInstanceRecord.getUserId();
        UserVo userVo = buildUser(userId);
        return userVo;
    }

    /**
     * 根据用户id
     *
     * @param userId
     * @return
     */
    private static UserVo buildUser(String userId) {

        IUserService userService = SpringUtil.getBean(IUserService.class);
        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        if (user == null) {
            return null;
        }

        UserVo nodeUserDto = UserVo.builder().id(userId).name(user.getName())
                .avatar(user.getAvatarUrl())
                .build();
        return nodeUserDto;
    }

    private static List<UserVo> buildUser(List<NodeUser> nodeUserList) {
        List<UserVo> userVoList = new ArrayList<>();
        //用户id
        List<String> userIdList = nodeUserList.stream().filter(w -> StrUtil.equals(w.getType(),
                NodeUserTypeEnum.USER.getKey())).map(w -> (w.getId())).collect(Collectors.toList());
        //部门id
        List<String> deptIdList = nodeUserList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> (w.getId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(deptIdList)) {

            IRemoteService iRemoteService = SpringUtil.getBean(IRemoteService.class);

            List<String> data =
                    iRemoteService.queryUserIdListByDepIdList(deptIdList.stream().map(w -> String.valueOf(w)).collect(Collectors.toList())).getData();

            if (CollUtil.isNotEmpty(data)) {
                for (String datum : data) {
                    if (!userIdList.contains((datum))) {
                        userIdList.add((datum));
                    }
                }
            }
        }
        {
            for (String aLong : userIdList) {
                userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
            }
        }
        return userVoList;
    }

}
