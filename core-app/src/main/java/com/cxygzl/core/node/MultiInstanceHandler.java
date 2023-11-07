package com.cxygzl.core.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.ApproveResultEnum;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.flow.SameAsStarter;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.FlowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_NODE_RESULT;
import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_RESULT;

/**
 * 多实例任务处理
 */
@Component("multiInstanceHandler")
@Slf4j
public class MultiInstanceHandler {

    /**
     * 审批人节点
     * 处理执行人
     *
     * @param execution
     * @return
     */
    public List<String> resolveAssignee(DelegateExecution execution) {
        //执行人集合
        Set<String> assignSet = new LinkedHashSet<>();

        IDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        String flowId = entity.getProcessDefinitionKey();
        String nodeId = entity.getActivityId();

        log.debug("nodeId={} nodeName={}", nodeId, entity.getActivityName());

        //发起人
        Object rootUserObj = execution.getVariable(ProcessInstanceConstant.VariableKey.STARTER);
        NodeUser rootUser = JsonUtil.parseArray(JsonUtil.toJSONString(rootUserObj), NodeUser.class).get(0);
        String rootUserId = rootUser.getId();

        //节点数据
        Node node = nodeDataStoreHandler.getNode(flowId, nodeId);
        if (node != null) {

            Map<String, Object> variables = execution.getVariables();

            Integer assignedType = node.getAssignedType();

            List<String> userIdList = AssignUserStrategyFactory.getStrategy(assignedType).handle(node, rootUser, variables);

            //处理发起人和审批人一致的问题
            SameAsStarter sameAsStarter = node.getSameAsStarter();
            if (sameAsStarter != null && userIdList.contains(rootUserId)) {
                String handler = sameAsStarter.getHandler();
                if (StrUtil.equals(ProcessInstanceConstant.UserTaskSameAsStarterHandler.TO_PASS, handler)) {
                    userIdList.remove(rootUserId);
                } else if (StrUtil.equals(ProcessInstanceConstant.UserTaskSameAsStarterHandler.TO_ADMIN, handler)) {
                    R<String> longR = BizHttpUtil.queryProcessAdmin(flowId);
                    String adminId = longR.getData();
                    int index = userIdList.indexOf(rootUserId);
                    userIdList.remove(rootUserId);
                    userIdList.add(index, adminId);
                } else if (StrUtil.equals(ProcessInstanceConstant.UserTaskSameAsStarterHandler.TO_DEPT_LEADER, handler)) {
                    userIdList.remove(rootUserId);


                    {
                        //去获取主管


                        R<List<com.cxygzl.common.dto.third.DeptDto>> r = BizHttpUtil.queryParentDepListByUserId(rootUserId);

                        List<com.cxygzl.common.dto.third.DeptDto> deptDtoList = r.getData();
                        if (CollUtil.isNotEmpty(deptDtoList)) {
                            if (deptDtoList.size() >= 1) {
                                DeptDto deptDto = deptDtoList.get(0);
                                List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                                if (CollUtil.isNotEmpty(leaderUserIdList)) {
                                    userIdList.addAll(leaderUserIdList);
                                }
                            }
                        }

                    }
                }
            }
            //处理是否去重的问题
            FlowSettingDto flowSettingDto = BizHttpUtil.queryProcessSetting(flowId).getData();
            FlowSettingDto.Distinct distinct = flowSettingDto.getDistinct();
            if (distinct != null && distinct.getEnable()) {
                List<String> existUserIdList=new ArrayList<>();
                for (String s : userIdList) {
                    List<TaskDto> taskDtos = FlowableUtils.queryApproveDataAtUnFinishedProcess(entity.getProcessInstanceId(), s);
                    if(!taskDtos.isEmpty()){
                        //存在已经审批过的了
                        if(distinct.getValue().intValue()==ProcessInstanceConstant.ProcessSettingDistinctValueClass.ALL){
                            existUserIdList.add(s);

                        }
                        if(distinct.getValue().intValue()==ProcessInstanceConstant.ProcessSettingDistinctValueClass.CONTINUED){
                            //判断上一个节点是否是审批节点 并且 也是同一个人

                            Node rootNode = NodeDataStoreFactory.getInstance().getNode(flowId, ProcessInstanceConstant.VariableKey.STARTER);

                            List<Node> parentNodeUntilRoot = NodeUtil.getParentNodeUntilRoot(rootNode, nodeId);
                            log.info("上级所有的节点：{}",parentNodeUntilRoot.stream().map(w->w.getId()).collect(Collectors.toList()));

                            if(CollUtil.isNotEmpty(parentNodeUntilRoot)){
                                Node node1 = parentNodeUntilRoot.get(0);
                                if(node1.getType().intValue()== NodeTypeEnum.APPROVAL.getValue()){
                                    long count = taskDtos.stream().filter(w -> w.getNodeId().equals(node1.getId()))
                                            .filter(w -> w.getAssign().equals(s)).count();
                                    if(count>0){
                                        existUserIdList.add(s);
                                    }
                                }
                            }

                        }
                    }

                }
                userIdList.removeAll(existUserIdList);
            }

            assignSet.addAll(userIdList);

        } else {
            //默认值
            String format = StrUtil.format("{}_assignee_default_list", nodeId);
            Object variable = execution.getVariable(format);
            List<NodeUser> nodeUserDtos = JsonUtil.parseArray(JsonUtil.toJSONString(variable), NodeUser.class);
            if (CollUtil.isNotEmpty(nodeUserDtos)) {
                List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
                assignSet.addAll(collect);
            }

        }

        if (CollUtil.isEmpty(assignSet)) {
            assignSet.add(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN);
        }

        return new ArrayList<>(assignSet);

    }

    /**
     * 处理多实例任务
     *
     * @param execution
     * @return
     */
    public List resolveCallActivity(DelegateExecution execution, String nodeId) {
        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        String flowId = entity.getProcessDefinitionKey();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);
        Integer multipleMode = node.getMultipleMode();


        Object variable = execution.getVariable(node.getMultipleModeValue().toString());
        if (variable == null) {
            return new ArrayList<>();
        }

        //数字表单

        if (ProcessInstanceConstant.SubProcessMultipleMode.FORM_NUMBER == multipleMode.intValue()) {
            int num = Convert.toInt(variable);
            List<String> list = new ArrayList<>();

            for (int k = 0; k < num; k++) {
                list.add("");
            }
            return list;
        }
        //多表单
        if (ProcessInstanceConstant.SubProcessMultipleMode.FORM_MULTIPLE == multipleMode.intValue()) {
            List<?> list = Convert.toList(
                    variable);

            return list;
        }
        return new ArrayList();

    }

    /**
     * 处理发起人用户任务
     * 默认情况下自动完成
     *
     * @param execution
     * @return
     */
    public List<String> resolveStarAssignee(DelegateExecution execution) {

        List<String> assignList = new ArrayList<>();


        //发起人
        Object rootUserObj = execution.getVariable(ProcessInstanceConstant.VariableKey.STARTER);
        NodeUser rootUser = JsonUtil.parseArray(JsonUtil.toJSONString(rootUserObj), NodeUser.class).get(0);
        assignList.add(rootUser.getId());

        return assignList;

    }


    /**
     * 会签或者或签完成条件检查
     *
     * @param execution
     */
    public boolean completionCondition(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String flowId = entity.getProcessDefinitionKey();

        UserTask flowNode = (UserTask) FlowableUtils.getFlowNode(execution.getProcessInstanceId(), entity.getActivityId());
        String nodeId = FlowableUtils.getNodeIdFromExtension(flowNode);


        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);

        Integer multipleMode = node.getMultipleMode();

        //实例总数
        int nrOfInstances = (int) execution.getVariable("nrOfInstances");
        //完成的实例数
        int nrOfCompletedInstances = (int) execution.getVariable("nrOfCompletedInstances");


        //完成比例
        BigDecimal modePercentage = BigDecimal.valueOf(100);
        if (node.getCompleteRate() != null) {
            modePercentage = node.getCompleteRate();
        }
        log.debug("当前节点完成实例数：{}  总实例数:{} 需要完成比例:{}", nrOfCompletedInstances, nrOfInstances, modePercentage);


        int okNum = 0;
        int failNum = 0;

        List<? extends DelegateExecution> executionList = execution.getExecutions();
        for (DelegateExecution delegateExecution : executionList) {
            Boolean variableLocal = delegateExecution.getVariableLocal(APPROVE_RESULT, Boolean.class);
            if (variableLocal != null && variableLocal) {
                okNum++;
            }
            if (variableLocal != null && !variableLocal) {
                failNum++;
            }
        }

        log.debug("okNum:{} failNum:{}", okNum, failNum);


        if (
                multipleMode.intValue() == ProcessInstanceConstant.MULTIPLE_MODE_ONE
        ) {
            //或签
            if (okNum > 0) {
                entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                        ApproveResultEnum.PASS.getValue());

                return true;
            }
            if (nrOfCompletedInstances == nrOfInstances) {
                entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                        ApproveResultEnum.REFUSE.getValue());

                return true;
            }

            return false;
        }
        if (
                multipleMode.intValue() == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT
        ) {
            //顺签
            if (failNum > 0) {
                entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                        ApproveResultEnum.REFUSE.getValue());

                return true;
            }
            if (nrOfCompletedInstances == nrOfInstances) {
                entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                        ApproveResultEnum.PASS.getValue());

                return true;
            }

            return false;
        }


        if (
                multipleMode.intValue() == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SAME

        ) {


            //会签
            if (Convert.toBigDecimal(okNum * 100).compareTo(Convert.toBigDecimal(nrOfInstances).multiply(modePercentage)) >= 0) {
                entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                        ApproveResultEnum.PASS.getValue());
                return true;
            } else {
                //如果剩余的数量不可能达到比例 也结束了
                if (Convert.toBigDecimal((nrOfInstances - nrOfCompletedInstances + okNum) * 100).compareTo(Convert.toBigDecimal(nrOfInstances).multiply(modePercentage)) < 0) {

                    entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                            ApproveResultEnum.REFUSE.getValue());

                    return true;
                }

                if (nrOfCompletedInstances == nrOfInstances) {
                    //未满足条件
                    entity.setVariable(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                            ApproveResultEnum.REFUSE.getValue());


                    return true;
                }


                return false;
            }

        }


        return false;
    }
}
