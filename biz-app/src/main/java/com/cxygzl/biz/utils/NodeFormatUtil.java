package com.cxygzl.biz.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessExecution;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessNodeRecord;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.vo.ProcessFormatNodeApproveDescVo;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.biz.vo.node.NodeFormatUserVo;
import com.cxygzl.common.constants.ApproveDescTypeEnum;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.SimpleApproveDescDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.cxygzl.common.constants.ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN;

/**
 * 节点格式化显示工具
 */
@Slf4j
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
     * @param processInstanceId
     * @param paramMap
     * @param processNodeRecordParamDtoList
     */
    public static List<NodeVo> formatProcessNodeShow(Node node,

                                                     String processInstanceId,
                                                     Map<String, Object> paramMap, List<ProcessNodeRecordParamDto> processNodeRecordParamDtoList) {
        List<NodeVo> list = new ArrayList();

        if (!NodeUtil.isNode(node)) {
            return list;
        }

        String name = node.getNodeName();
        Integer type = node.getType();


        NodeVo nodeVo = new NodeVo();
        nodeVo.setId(node.getId());
        nodeVo.setName(name);
        nodeVo.setType(type);
        nodeVo.setStatus(NodeStatusEnum.WKS.getCode());
        String executionId = node.getExecutionId();
        ProcessNodeRecord processNodeRecord = null;
        if (StrUtil.isAllNotBlank(executionId, processInstanceId)) {

            IProcessNodeRecordService processNodeRecordService = SpringUtil.getBean(IProcessNodeRecordService.class);
            List<ProcessNodeRecord> processNodeRecordList = processNodeRecordService.lambdaQuery()
                    .eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId)
                    .eq(ProcessNodeRecord::getExecutionId, executionId)
                    .eq(!StrUtil.startWith(node.getId(), ProcessInstanceConstant.VariableKey.STARTER), ProcessNodeRecord::getNodeId, node.getId())
                    .likeRight(StrUtil.startWith(node.getId(), ProcessInstanceConstant.VariableKey.STARTER),
                            ProcessNodeRecord::getNodeId, ProcessInstanceConstant.VariableKey.STARTER)
                    .eq(ProcessNodeRecord::getFlowUniqueId, node.getFlowUniqueId())
                    .orderByDesc(ProcessNodeRecord::getCreateTime)
                    .list();
            processNodeRecord = processNodeRecordList.get(0);

            nodeVo.setStatus(processNodeRecord.getStatus());

        }

        {

            nodeVo.setPlaceholder(node.getPlaceHolder());

        }
        if (StrUtil.isAllNotBlank(processInstanceId, node.getExecutionId(), node.getFlowUniqueId())) {

            if (processNodeRecord != null) {
                nodeVo.setShowTimeStr(nodeDateShow(processNodeRecord.getStartTime()));
                if (processNodeRecord.getEndTime() != null) {
                    nodeVo.setShowTimeStr(nodeDateShow(processNodeRecord.getEndTime()));
                }


            }
        }


        List<NodeFormatUserVo> nodeFormatUserVoList = new ArrayList<>();
        if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

            Integer assignedType = node.getAssignedType();

            boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
            nodeVo.setSelectUser(selfSelect);
            if (selfSelect) {
                nodeVo.setMultiple(node.getMultiple());
            }
            if (StrUtil.isNotBlank(processInstanceId)) {
                nodeVo.setSelectUser(false);
            }

            // 用户列表
            if (StrUtil.isAllNotBlank(processInstanceId, node.getExecutionId())) {

                buildApproveDesc(node, processInstanceId, nodeVo, nodeFormatUserVoList);

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT) {
                //发起人自选
                Object variable = paramMap.get(StrUtil.format("{}_assignee_select", node.getId()));
                if (variable == null) {
                    variable = new ArrayList<>();
                }
                List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);

                List<String> collect = nodeUserDtos.stream().map(w -> (w.getId())).collect(Collectors.toList());
                for (String aLong : collect) {
                    NodeFormatUserVo nodeFormatUserVo = buildUser(aLong);
                    nodeFormatUserVoList.add(nodeFormatUserVo);
                }
            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.USER) {
                //指定用户

                List<NodeUser> nodeUserList = node.getNodeUserList();
                List<NodeFormatUserVo> tempList = buildUser(nodeUserList);
                nodeFormatUserVoList.addAll(tempList);


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
                            nodeFormatUserVoList.add(buildUser(aLong));
                        }
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FORM_DEPT) {
                //表单部门
                String formUser = node.getFormUserId();

                Object o = paramMap.get(formUser);
                if (o != null) {
                    String jsonString = JSON.toJSONString(o);
                    if (StrUtil.isNotBlank(jsonString)) {
                        List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
                        List<String> deptIdList =
                                nodeUserDtoList.stream().map(w -> (w.getId())).collect(Collectors.toList());

                        if (CollUtil.isNotEmpty(deptIdList)) {

                            String deptUserType = node.getDeptUserType();
                            if (!ProcessInstanceConstant.AssignedTypeFormDeptUserTypeClass.LEADER.equals(deptUserType)) {
                                List<String> userIdList = ApiStrategyFactory.getStrategy().loadUserIdListByDeptIdList(deptIdList);
                                for (String aLong : userIdList) {
                                    long count = nodeFormatUserVoList.stream()
                                            .filter(w -> StrUtil.equals(aLong, w.getId())).count();
                                    if (count > 0) {
                                        continue;
                                    }
                                    nodeFormatUserVoList.add(buildUser(aLong));
                                }
                            } else {
                                List<DeptDto> deptDtoList = SpringUtil.getBean(IRemoteService.class).queryDeptList(deptIdList).getData();
                                for (DeptDto deptDto : deptDtoList) {
                                    List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                                    for (String s : leaderUserIdList) {
                                        long count = nodeFormatUserVoList.stream()
                                                .filter(w -> StrUtil.equals(s, w.getId())).count();
                                        if (count > 0) {
                                            continue;
                                        }
                                        nodeFormatUserVoList.add(buildUser(s));
                                    }
                                }
                            }


                        }


                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FIXED_DEPT_LEADER) {
                //指定部门主管

                List<NodeUser> nodeUserDtoList = node.getNodeUserList();
                List<String> deptIdList =
                        nodeUserDtoList.stream().map(w -> (w.getId())).collect(Collectors.toList());

                if (CollUtil.isNotEmpty(deptIdList)) {

                    {
                        List<DeptDto> deptDtoList = SpringUtil.getBean(IRemoteService.class).queryDeptList(deptIdList).getData();
                        for (DeptDto deptDto : deptDtoList) {
                            List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                            for (String s : leaderUserIdList) {
                                long count = nodeFormatUserVoList.stream()
                                        .filter(w -> StrUtil.equals(s, w.getId())).count();
                                if (count > 0) {
                                    continue;
                                }
                                nodeFormatUserVoList.add(buildUser(s));
                            }
                        }
                    }


                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                //发起人自己
                nodeFormatUserVoList.addAll(CollUtil.newArrayList(buildUser(StpUtil.getLoginIdAsString())));
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

                        List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                        for (String s : leaderUserIdList) {
                            NodeFormatUserVo nodeFormatUserVo = buildUser(s);
                            nodeFormatUserVoList.add(nodeFormatUserVo);
                        }


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


                        List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                        for (String s : leaderUserIdList) {
                            NodeFormatUserVo nodeFormatUserVo = buildUser(s);
                            nodeFormatUserVoList.add(nodeFormatUserVo);
                        }

                        index++;
                    }
                }
            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.ROLE) {

                //角色
                List<NodeUser> nodeUserList = node.getNodeUserList();

                List<String> roleIdList = nodeUserList.stream().map(w -> w.getId()).collect(Collectors.toList());
                //去获取主管

                IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);

                List<String> userIdList = remoteService.queryUserIdListByRoleIdList(roleIdList).getData();


                for (String s : userIdList) {
                    NodeFormatUserVo nodeFormatUserVo = buildUser(s);
                    nodeFormatUserVoList.add(nodeFormatUserVo);
                }

            }


        } else if (node.getType().intValue() == NodeTypeEnum.ROOT.getValue()) {
            //发起节点
            if (StrUtil.isBlank(processInstanceId)) {
                NodeFormatUserVo nodeFormatUserVo = buildUser(StpUtil.getLoginIdAsString());

                nodeFormatUserVoList.addAll(CollUtil.newArrayList(nodeFormatUserVo));

            } else {


                buildApproveDesc(node, processInstanceId, nodeVo, nodeFormatUserVoList);

            }
        } else if (node.getType().intValue() == NodeTypeEnum.CC.getValue()) {
            //抄送节点

            List<NodeUser> nodeUserList = node.getNodeUserList();

            List<NodeFormatUserVo> tempList = buildUser(nodeUserList);
            //如果当前节点已执行  则用户为已执行
            if(StrUtil.isAllNotBlank(node.getExecutionId(),node.getFlowUniqueId()))
            for (NodeFormatUserVo nodeFormatUserVo : tempList) {
                nodeFormatUserVo.setStatus(NodeStatusEnum.YJS.getCode());

            }
            nodeFormatUserVoList.addAll(tempList);

        }
        nodeVo.setUserVoList(nodeFormatUserVoList);


        List<NodeVo> branchList = new ArrayList<>();

        List<Node> branchs = node.getConditionNodes();

        if (NodeTypeEnum.getByValue(type).getBranch() && CollUtil.isNotEmpty(branchs)) {
            //条件分支

                //判断当前分支是否执行了
                boolean executed = processNodeRecordParamDtoList.stream()
                        .filter(w -> StrUtil.equals(w.getNodeId(), node.getId()))
                        .filter(w -> StrUtil.equals(w.getFlowUniqueId(), node.getFlowUniqueId()))
                        .filter(w -> StrUtil.equals(w.getExecutionId(), node.getExecutionId()))
                        .count()>0;


            for (Node branch : branchs) {
                Node children = branch.getChildNode();
                if (children == null) {
                    continue;
                }


                    long count = processNodeRecordParamDtoList.stream()
                            .filter(w -> StrUtil.equals(w.getNodeId(), children.getId()))
                            .filter(w -> StrUtil.equals(w.getFlowUniqueId(), children.getFlowUniqueId()))
                            .filter(w -> StrUtil.equals(w.getExecutionId(), children.getExecutionId()))
                            .count();

                 if(!executed||(count>0)){

                     List<NodeVo> processNodeShowDtos = formatProcessNodeShow(children, processInstanceId, paramMap, processNodeRecordParamDtoList);

                     NodeVo p = new NodeVo();
                     p.setChildren(processNodeShowDtos);

                     p.setPlaceholder(branch.getPlaceHolder());
                     branchList.add(p);
                 }

            }

        }
        nodeVo.setBranch(branchList);


        list.add(nodeVo);

        List<NodeVo> next = formatProcessNodeShow(node.getChildNode(), processInstanceId, paramMap, processNodeRecordParamDtoList);
        list.addAll(next);


        return list;
    }

    private static List<ProcessNodeRecordAssignUser> buildApproveDesc(Node node, String processInstanceId, NodeVo nodeVo, List<NodeFormatUserVo> nodeFormatUserVoList) {
        IProcessExecutionService processExecutionService = SpringUtil.getBean(IProcessExecutionService.class);
        List<ProcessExecution> processExecutionList = processExecutionService.lambdaQuery()
                .eq(ProcessExecution::getExecutionId, node.getExecutionId())
                .list();
        List<String> childExecutionIdList = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toList());

        if (CollUtil.isEmpty(processExecutionList)) {
            childExecutionIdList.add(node.getExecutionId());
        }

        IProcessNodeRecordAssignUserService processNodeRecordAssignUserService = SpringUtil.getBean(IProcessNodeRecordAssignUserService.class);
        List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService
                .lambdaQuery()
                .ne(ProcessNodeRecordAssignUser::getUserId, DEFAULT_EMPTY_ASSIGN)
                .eq(!StrUtil.startWith(node.getId(), ProcessInstanceConstant.VariableKey.STARTER), ProcessNodeRecordAssignUser::getNodeId, node.getId())
                .like(StrUtil.startWith(node.getId(), ProcessInstanceConstant.VariableKey.STARTER),
                        ProcessNodeRecordAssignUser::getNodeId, ProcessInstanceConstant.VariableKey.STARTER)
                .in(ProcessNodeRecordAssignUser::getExecutionId, childExecutionIdList)
                .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processInstanceId)
                .orderByAsc(ProcessNodeRecordAssignUser::getCreateTime)
                .list();

        //处理用户评论
        if (CollUtil.isNotEmpty(processNodeRecordAssignUserList)) {
            Set<String> taskIdList = processNodeRecordAssignUserList.stream().map(w -> w.getTaskId()).collect(Collectors.toSet());

            List<ProcessFormatNodeApproveDescVo> descList = new ArrayList();

            for (String taskId : taskIdList) {
                List<SimpleApproveDescDto> simpleApproveDescDtoList = CoreHttpUtil.queryTaskComments(taskId).getData();

                for (SimpleApproveDescDto simpleApproveDescDto : simpleApproveDescDtoList) {
                    NodeFormatUserVo nodeFormatUserVo = buildUser(simpleApproveDescDto.getUserId());
                    ProcessFormatNodeApproveDescVo descVo = ProcessFormatNodeApproveDescVo.builder()
                            .user(nodeFormatUserVo)
                            .desc(simpleApproveDescDto.getMessage())
                            .descType(simpleApproveDescDto.getType())
                            .sys(simpleApproveDescDto.getSys())
                            .descTypeStr(ApproveDescTypeEnum.get(simpleApproveDescDto.getType()).getName())
                            .showTimeStr(nodeDateShow(simpleApproveDescDto.getDate()))
                            .date(simpleApproveDescDto.getDate())
                            .build();

                    descList.add(descVo);
                }

            }


            CollUtil.sort(descList, new Comparator<ProcessFormatNodeApproveDescVo>() {
                @Override
                public int compare(ProcessFormatNodeApproveDescVo t0,
                                   ProcessFormatNodeApproveDescVo t1) {
                    long time0 = t0.getDate().getTime();
                    long time1 = t1.getDate().getTime();
                    return time0 > time1 ? 1 : -1;
                }
            });

            nodeVo.setApproveDescList(descList);

        }


        Set<String> userIdSet = processNodeRecordAssignUserList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());
        if (CollUtil.isEmpty(userIdSet) && node.getId().equals(ProcessInstanceConstant.VariableKey.STARTER)) {

            IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
            String userId = processInstanceRecord.getUserId();


            NodeFormatUserVo nodeFormatUserVo = buildUser((userId));
            nodeFormatUserVo.setShowTime(processInstanceRecord.getCreateTime());
            nodeFormatUserVo.setShowTimeStr(nodeDateShow(processInstanceRecord.getCreateTime()));
            nodeFormatUserVo.setStatus(NodeStatusEnum.YJS.getCode());
//            userVo.setOperType(w.getTaskType());

            nodeFormatUserVoList.add(nodeFormatUserVo);
        }

        for (String userId : userIdSet) {

            List<ProcessNodeRecordAssignUser> list =
                    processNodeRecordAssignUserList.stream().filter(k -> StrUtil.equals(k.getUserId(), userId))
                            .collect(Collectors.toList());
            ProcessNodeRecordAssignUser w = list.get(list.size() - 1);


            NodeFormatUserVo nodeFormatUserVo = buildUser((userId));
            nodeFormatUserVo.setShowTime(w.getEndTime());
            nodeFormatUserVo.setShowTimeStr(nodeDateShow(w.getEndTime()));
            nodeFormatUserVo.setStatus(w.getStatus());
            nodeFormatUserVo.setOperType(w.getTaskType());

            nodeFormatUserVoList.add(nodeFormatUserVo);
        }
        return processNodeRecordAssignUserList;
    }


    /**
     * 根据实例id
     *
     * @param processInstanceId
     * @return
     */
    private static NodeFormatUserVo buildRootUser(String processInstanceId) {

        IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
        String userId = processInstanceRecord.getUserId();
        NodeFormatUserVo nodeFormatUserVo = buildUser(userId);
        return nodeFormatUserVo;
    }

    /**
     * 根据用户id
     *
     * @param userId
     * @return
     */
    private static NodeFormatUserVo buildUser(String userId) {

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        if (user == null) {
            return null;
        }

        NodeFormatUserVo nodeUserDto = NodeFormatUserVo.builder().id(userId).name(user.getName())
                .avatar(user.getAvatarUrl())
                .build();
        return nodeUserDto;
    }

    private static List<NodeFormatUserVo> buildUser(List<NodeUser> nodeUserList) {
        List<NodeFormatUserVo> nodeFormatUserVoList = new ArrayList<>();
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
                nodeFormatUserVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
            }
        }
        return nodeFormatUserVoList;
    }

}
