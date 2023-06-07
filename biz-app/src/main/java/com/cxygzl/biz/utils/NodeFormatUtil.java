package com.cxygzl.biz.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.service.IProcessNodeRecordAssignUserService;
import com.cxygzl.biz.service.IRemoteService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.biz.vo.node.UserVo;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodePropDto;
import com.cxygzl.common.dto.process.NodeUserDto;
import com.cxygzl.common.utils.NodeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 节点格式化显示工具
 */
public class NodeFormatUtil {

    /**
     * 格式化流程节点显示
     *
     * @param nodeDto
     * @param completeNodeSet
     * @param continueNodeSet
     * @param processInstanceId
     * @param paramMap
     */
    public static List<NodeVo> formatProcessNodeShow(NodeDto nodeDto,
                                                     Set<String> completeNodeSet,
                                                     Set<String> continueNodeSet,
                                                     String processInstanceId,
                                                     Map<String, Object> paramMap) {
        List<NodeVo> list = new ArrayList();

        if (!NodeUtil.isNode(nodeDto)) {
            return list;
        }

        String name = nodeDto.getName();
        String type = nodeDto.getType();
        if (StrUtil.equals(type, NodeTypeEnum.EMPTY.getKey())) {
            return formatProcessNodeShow(nodeDto.getChildren(), completeNodeSet, continueNodeSet, processInstanceId, paramMap);
        }

        //SELF_SELECT


        NodeVo nodeVo = new NodeVo();
        nodeVo.setId(nodeDto.getId());
        nodeVo.setName(name);
        nodeVo.setType(type);
        nodeVo.setStatus(NodeStatusEnum.WKS.getCode());
        if (completeNodeSet.contains(nodeDto.getId())) {
            nodeVo.setStatus(NodeStatusEnum.YJS.getCode());

        }
        if (continueNodeSet.contains(nodeDto.getId())) {
            nodeVo.setStatus(NodeStatusEnum.JXZ.getCode());

        }

        {
            NodePropDto props = nodeDto.getProps();

            nodeVo.setPlaceholder(props.getPlaceholder());

        }

        NodePropDto props = nodeDto.getProps();

        List<List<UserVo>> userDto1List = new ArrayList<>();
        if (StrUtil.equals(type, NodeTypeEnum.APPROVAL.getKey())) {
            String assignedType = props.getAssignedType();
            boolean selfSelect = StrUtil.equals(assignedType, "SELF_SELECT");
            nodeVo.setSelectUser(selfSelect);
            if (selfSelect) {
                Boolean multiple = props.getSelfSelect().getMultiple();
                nodeVo.setMultiple(multiple);
            }
            nodeVo.setPlaceholder(props.getPlaceholder());
            // 用户列表
            if (StrUtil.isNotBlank(processInstanceId)) {
                IProcessNodeRecordAssignUserService processNodeRecordAssignUserService = SpringUtil.getBean(IProcessNodeRecordAssignUserService.class);
                List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService
                        .lambdaQuery().
                        eq(ProcessNodeRecordAssignUser::getNodeId, nodeDto.getId())
                        .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processInstanceId)
                        .orderByAsc(ProcessNodeRecordAssignUser::getCreateTime)
                        .list();
                Map<String, List<ProcessNodeRecordAssignUser>> map = processNodeRecordAssignUserList.stream().collect(Collectors.groupingBy(w -> w.getTaskId()));

                for (Map.Entry<String, List<ProcessNodeRecordAssignUser>> entry : map.entrySet()) {
                    List<ProcessNodeRecordAssignUser> value = entry.getValue();
                    List<UserVo> collect = value.stream().map(w -> {
                        UserVo userVo = buildUser(Long.parseLong(w.getUserId()));
                        userVo.setShowTime(w.getEndTime());
                        userVo.setApproveDesc(w.getApproveDesc());
                        userVo.setStatus(w.getStatus());
                        userVo.setOperType(w.getTaskType());
                        return userVo;
                    }).collect(Collectors.toList());
                    userDto1List.add(collect);

                }

                if (processNodeRecordAssignUserList.isEmpty()) {
                    if (StrUtil.equals(props.getAssignedType(), "SELF")) {
                        //发起人自己
                        userDto1List.add(CollUtil.newArrayList(buildRootUser(processInstanceId)));
                    }
                    if (StrUtil.equals(props.getAssignedType(), "SELF_SELECT")) {
                        //发起人自选
                        Object variable = paramMap.get(StrUtil.format("{}_assignee_select", nodeDto.getId()));
                        List<NodeUserDto> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUserDto.class);

                        List<Long> collect = nodeUserDtos.stream().map(w -> (w.getId())).collect(Collectors.toList());
                        for (Long aLong : collect) {
                            UserVo userVo = buildUser(aLong);
                            userDto1List.add(CollUtil.newArrayList(userVo));
                        }
                    }
                }


            } else if (StrUtil.equals(props.getAssignedType(), "ASSIGN_USER")) {
                //指定用户
                List<NodeUserDto> userDtoTempList = props.getAssignedUser();
                //用户id
                List<Long> userIdList = userDtoTempList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toLong(w.getId())).collect(Collectors.toList());
                //部门id
                List<Long> deptIdList = userDtoTempList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toLong(w.getId())).collect(Collectors.toList());

                if (CollUtil.isNotEmpty(deptIdList)) {

                    IRemoteService iRemoteService = SpringUtil.getBean(IRemoteService.class);

                    List<Long> data = iRemoteService.queryUserIdListByDepIdList(deptIdList).getData();

                    if (CollUtil.isNotEmpty(data)) {
                        for (long datum : data) {
                            if (!userIdList.contains(datum)) {
                                userIdList.add(datum);
                            }
                        }
                    }
                }
                {
                    for (Long aLong : userIdList) {
                        userDto1List.add(CollUtil.newArrayList(buildUser(aLong)));
                    }
                }


            } else if (StrUtil.equals(props.getAssignedType(), "FORM_USER")) {
                //表单人员
                String formUser = props.getFormUser();

                Object o = paramMap.get(formUser);
                if (o != null) {
                    String jsonString = JSON.toJSONString(o);
                    if (StrUtil.isNotBlank(jsonString)) {
                        List<NodeUserDto> nodeUserDtoList = JSON.parseArray(jsonString, NodeUserDto.class);
                        List<Long> userIdList = nodeUserDtoList.stream().map(w -> (w.getId())).collect(Collectors.toList());
                        for (Long aLong : userIdList) {
                            userDto1List.add(CollUtil.newArrayList(buildUser(aLong)));
                        }
                    }
                }


            } else if (StrUtil.equals(props.getAssignedType(), "SELF")) {
                //发起人自己
                userDto1List.add(CollUtil.newArrayList(buildUser(StpUtil.getLoginIdAsLong())));
            }


        } else if (nodeDto.getType().equals(NodeTypeEnum.ROOT.getKey())) {
            //发起节点
            if (StrUtil.isBlank(processInstanceId)) {
                UserVo userVo = buildUser(StpUtil.getLoginIdAsLong());

                userDto1List.add(CollUtil.newArrayList(userVo));

            } else {


                UserVo userVo = buildRootUser(processInstanceId);
                userVo.setOperType("COMPLETE");
                userVo.setStatus(NodeStatusEnum.YJS.getCode());
                userDto1List.add(CollUtil.newArrayList(userVo));

            }
        } else if (nodeDto.getType().equals(NodeTypeEnum.CC.getKey())) {
            //指定用户
            List<NodeUserDto> userDtoTempList = props.getAssignedUser();
            //用户id
            List<Long> userIdList = userDtoTempList.stream().filter(w -> StrUtil.equals(w.getType(),NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toLong(w.getId())).collect(Collectors.toList());
            //部门id
            List<Long> deptIdList = userDtoTempList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toLong(w.getId())).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(deptIdList)) {

                IRemoteService iRemoteService = SpringUtil.getBean(IRemoteService.class);

                List<Long> data = iRemoteService.queryUserIdListByDepIdList(deptIdList).getData();

                if (CollUtil.isNotEmpty(data)) {
                    for (long datum : data) {
                        if (!userIdList.contains(datum)) {
                            userIdList.add(datum);
                        }
                    }
                }
            }
            {
                for (Long aLong : userIdList) {
                    userDto1List.add(CollUtil.newArrayList(buildUser(aLong)));
                }
            }

        }
        nodeVo.setUserDtoList(userDto1List);


        List<NodeVo> branchList = new ArrayList<>();

        if (StrUtil.equalsAny(type, NodeTypeEnum.CONDITIONS.getKey(), NodeTypeEnum.INCLUSIVES.getKey(), NodeTypeEnum.CONCURRENTS.getKey())) {
            //条件分支

            List<NodeDto> branchs = nodeDto.getBranchs();
            for (NodeDto branch : branchs) {
                NodeDto children = branch.getChildren();
                List<NodeVo> processNodeShowDtos = formatProcessNodeShow(children, completeNodeSet, continueNodeSet, processInstanceId, paramMap);

                NodeVo p = new NodeVo();
                p.setChildren(processNodeShowDtos);

                p.setPlaceholder(branch.getProps().getPlaceholder());
                branchList.add(p);
            }
        }
        nodeVo.setBranch(branchList);


        list.add(nodeVo);

        List<NodeVo> next = formatProcessNodeShow(nodeDto.getChildren(), completeNodeSet, continueNodeSet, processInstanceId, paramMap);
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
        Long userId = processInstanceRecord.getUserId();
        UserVo userVo = buildUser(userId);
        return userVo;
    }

    /**
     * 根据用户id
     *
     * @param userId
     * @return
     */
    private static UserVo buildUser(long userId) {

        IUserService userService = SpringUtil.getBean(IUserService.class);
        User user = userService.getById(userId);

        UserVo nodeUserDto = UserVo.builder().id(userId).name(user.getName())
                .avatar(user.getAvatarUrl())
                .build();
        return nodeUserDto;
    }

}
