package com.cxygzl.core.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.DeptDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("multiInstanceHandler")
@Slf4j
public class MultiInstanceHandler {
    @Resource
    private RuntimeService runtimeService;

    /**
     * 处理执行人
     *
     * @param execution
     * @return
     */
    public List<String> resolveAssignee(DelegateExecution execution) {
        //执行人集合
        List<String> assignList = new ArrayList<>();

        INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        String flowId = entity.getProcessDefinitionKey();
        String nodeId = entity.getActivityId();

        log.debug("nodeId={} nodeName={}", nodeId, entity.getActivityName());

        //发起人
        Object rootUserObj = execution.getVariable("root");
        NodeUser rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj), NodeUser.class).get(0);

        //节点数据
        Node node =  nodeDataStoreHandler.getNode(flowId, nodeId);
        if (node != null) {


            Integer assignedType = node.getAssignedType();


            if (assignedType == ProcessInstanceConstant.AssignedTypeClass.USER) {
                //指定人员
                List<NodeUser> userDtoList = node.getNodeUserList();
                //用户id
                List<String> userIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());
                //部门id
                List<String> deptIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

                if (CollUtil.isNotEmpty(deptIdList)) {

                    R<List<String>> r= CoreHttpUtil.queryUserIdListByDepIdList(deptIdList);

                    List<String> data = r.getData();
                    if (CollUtil.isNotEmpty(data)) {
                        for (String datum : data) {
                            if (!userIdList.contains(datum)) {
                                userIdList.add(datum);
                            }
                        }
                    }
                }

                assignList.addAll(userIdList);

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {

                //发起人自己


                List<String> userIdList = CollUtil.newArrayList(String.valueOf(rootUser.getId()));
                assignList.addAll(userIdList);

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FORM_USER) {

                //表单值

                Object variable = execution.getVariable(node.getFormUserId());
                if (variable == null) {

                } else if (StrUtil.isBlankIfStr(variable)) {

                } else {

                    String jsonString = JSON.toJSONString(variable);
                    List<NodeUser> nodeUserDtoList = CommonUtil.toArray(jsonString, NodeUser.class);

                    List<String> userIdList = nodeUserDtoList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

                    assignList.addAll(userIdList);

                }

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.LEADER) {

                //指定主管审批
                //第几级主管审批
                Integer level = node.getDeptLeaderLevel();

                //去获取主管

                R<List<DeptDto>> r = CoreHttpUtil.queryParentDepListByUserId(Long.parseLong(rootUser.getId()));

                List<DeptDto> deptDtoList = r.getData();
                if (CollUtil.isNotEmpty(deptDtoList)) {
                    if (deptDtoList.size() >= level) {
                        DeptDto deptDto = deptDtoList.get(level - 1);

                        assignList.add(String.valueOf(deptDto.getLeaderUserId()));
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.LEADER_TOP) {


                //去获取主管

                R<List<DeptDto>> r  = CoreHttpUtil.queryParentDepListByUserId(Long.parseLong(rootUser.getId()));

                List<DeptDto> deptDtoList = r.getData();

                //上级主管依次审批

                //第几级主管审批截止
                Integer level = node.getDeptLeaderLevel();


                if (CollUtil.isNotEmpty(deptDtoList)) {
                    int index = 1;
                    for (DeptDto deptDto : deptDtoList) {
                        if (level != null && level < index) {
                            break;
                        }
                        assignList.add(String.valueOf(deptDto.getLeaderUserId()));
                        index++;
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT) {

                //发起人自选
                Object variable = execution.getVariable(StrUtil.format("{}_assignee_select", nodeId));
                log.info("{}-发起人自选参数:{}", node.getName(), variable);
                List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);

                List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

                assignList.addAll(collect);

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.ROLE) {

                //角色

                List<NodeUser> nodeUserList = node.getNodeUserList();

                List<String> roleIdList = nodeUserList.stream().map(w -> w.getId()).collect(Collectors.toList());


                R<List<String>> r = CoreHttpUtil.queryUserIdListByRoleIdList(roleIdList);

                List<String> data = r.getData();


                assignList.addAll(data);

            }

        } else {
            //默认值
            String format = StrUtil.format("{}_assignee_default_list", nodeId);
            Object variable = execution.getVariable(format);
            List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);
            if (CollUtil.isNotEmpty(nodeUserDtos)) {
                List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
                assignList.addAll(collect);
            }

        }

        if (CollUtil.isEmpty(assignList)) {
            assignList.add(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN);
        }

        return assignList;

    }



    /**
     * 会签或者或签完成条件检查
     *
     * @param execution
     */
    public boolean completionCondition(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String processDefinitionKey = entity.getProcessDefinitionKey();

        String nodeId = execution.getCurrentActivityId();

        Node node = NodeDataStoreFactory.getInstance().getNode(processDefinitionKey, nodeId);

        Integer multipleMode = node.getMultipleMode();

        BigDecimal modePercentage=BigDecimal.valueOf(100);


        Object variable = execution.getVariable(StrUtil.format("{}_approve_condition", nodeId));
        log.debug("当前节点审批结果：{}", variable);
        Boolean approve = Convert.toBool(variable);
//        if (StrUtil.equalsAny(mode, "AND", "NEXT")) {
        if (
                multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME||
                multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT

        ) {
            //会签或者顺序签署
            if (!approve) {
                return true;
            }
        }
        if (

                multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_ONE

        ) {
            //或签
            if (approve) {
                return true;
            }
        }

        //实例总数
        int nrOfInstances = (int) execution.getVariable("nrOfInstances");
        //完成的实例数
        int nrOfCompletedInstances = (int) execution.getVariable("nrOfCompletedInstances");
        log.debug("当前节点完成实例数：{}  总实例数:{} 需要完成比例:{}", nrOfCompletedInstances, nrOfInstances, modePercentage);
        if ( multipleMode.intValue()==ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME) {
            //会签判断完成比例
            if (modePercentage != null) {
                if (Convert.toBigDecimal(nrOfCompletedInstances * 100).compareTo(Convert.toBigDecimal(nrOfCompletedInstances).multiply(modePercentage)) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (nrOfCompletedInstances == nrOfInstances) {
            return true;
        }
        return false;
    }
}
