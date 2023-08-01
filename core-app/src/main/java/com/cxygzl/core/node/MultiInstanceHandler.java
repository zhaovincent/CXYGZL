package com.cxygzl.core.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.flow.SameAsStarter;
import com.cxygzl.common.dto.third.DeptDto;
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
import java.util.Map;
import java.util.stream.Collectors;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_OK_NUM;

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
        Object rootUserObj = execution.getVariable(ProcessInstanceConstant.VariableKey.STARTER);
        NodeUser rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj), NodeUser.class).get(0);
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
                    R<String> longR = CoreHttpUtil.queryProcessAdmin(flowId);
                    String adminId = longR.getData();
                    int index = userIdList.indexOf(rootUserId);
                    userIdList.remove(rootUserId);
                    userIdList.add(index, adminId);
                } else if (StrUtil.equals(ProcessInstanceConstant.UserTaskSameAsStarterHandler.TO_DEPT_LEADER, handler)) {
                    int index = userIdList.indexOf(rootUserId);
                    userIdList.remove(rootUserId);


                    {
                        //去获取主管

                        R<List<com.cxygzl.common.dto.third.DeptDto>> r = CoreHttpUtil.queryParentDepListByUserId(rootUserId);

                        List<com.cxygzl.common.dto.third.DeptDto> deptDtoList = r.getData();
                        if (CollUtil.isNotEmpty(deptDtoList)) {
                            if (deptDtoList.size() >= 1) {
                                DeptDto deptDto = deptDtoList.get(0);
                                String leaderUserId = deptDto.getLeaderUserId();
                                if (StrUtil.isNotBlank(leaderUserId)) {
                                    userIdList.add(index, leaderUserId);
                                }
                            }
                        }

                    }
                }
            }

            assignList.addAll(userIdList);

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


        Object reject = execution.getVariable(ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE);
        Object subProcess = execution.getVariable(ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE);
        if (reject == null && subProcess == null) {
            return assignList;
        }


        //发起人
        Object rootUserObj = execution.getVariable(ProcessInstanceConstant.VariableKey.STARTER);
        NodeUser rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj), NodeUser.class).get(0);
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
        String processDefinitionKey = entity.getProcessDefinitionKey();



        String nodeId = execution.getCurrentActivityId();

        Node node = NodeDataStoreFactory.getInstance().getNode(processDefinitionKey, nodeId);

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


        Object result = execution.getVariable(ProcessInstanceConstant.VariableKey.APPROVE_RESULT);

        log.debug("当前节点审批结果：{}", result);
        Boolean approve = Convert.toBool(result,true);
        if (approve) {

            Object okNum = execution.getVariableLocal(APPROVE_OK_NUM);
            Integer num = Convert.toInt(okNum, 0);
            num++;

            if (num <= nrOfCompletedInstances) {
                ((ExecutionEntityImpl) execution).setVariableLocal(APPROVE_OK_NUM, num);

            }

        }

        if (
                multipleMode.intValue() == ProcessInstanceConstant.MULTIPLE_MODE_ONE
        ) {
            //或签
            if (approve) {
                return true;
            }
            if (nrOfCompletedInstances == nrOfInstances) {
                return true;
            }

            return false;
        }
        if (
                multipleMode.intValue() == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT
        ) {
            //顺签
            if (!approve) {
                return true;
            }
            if (nrOfCompletedInstances == nrOfInstances) {
                return true;
            }

            return false;
        }


        if (
                multipleMode.intValue() == ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME

        ) {

            Object okNum = execution.getVariableLocal(APPROVE_OK_NUM);
            Integer anInt = Convert.toInt(okNum, 0);
            log.info("节点:{}  成功的数量:{}", execution.getId(), anInt);

            //会签
            if (Convert.toBigDecimal(anInt * 100).compareTo(Convert.toBigDecimal(nrOfInstances).multiply(modePercentage)) >= 0) {
                return true;
            } else {
                //如果剩余的数量不可能达到比例 也结束了
                if (Convert.toBigDecimal((nrOfInstances - nrOfCompletedInstances + anInt) * 100).compareTo(Convert.toBigDecimal(nrOfInstances).multiply(modePercentage)) < 0) {



                    return true;
                }

                if (nrOfCompletedInstances == nrOfInstances) {
                    //未满足条件

                    return true;
                }


                return false;
            }

        }


        return false;
    }
}
