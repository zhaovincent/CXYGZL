package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.FormUtil;
import com.cxygzl.biz.vo.TaskDetailViewVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeRecordService processNodeRecordService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessExecutionService executionService;

    /**
     * 查询任务
     *
     * @param taskId
     * @param view
     * @return
     */
    @Override
    public R queryTask(String taskId, boolean view) {


        String userId = StpUtil.getLoginIdAsString();


        com.cxygzl.common.dto.R<TaskResultDto> r = CoreHttpUtil.queryTask(taskId, userId);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }

        TaskResultDto taskResultDto = r.getData();

        String flowId = taskResultDto.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);
        if (oaForms == null) {
            return R.fail("流程不存在");
        }


        List<ProcessExecution> processExecutionList = executionService.lambdaQuery()
                .eq(ProcessExecution::getExecutionId, taskResultDto.getExecutionId())
                .or()
                .eq(ProcessExecution::getChildExecutionId, taskResultDto.getExecutionId())
                .list();

        Set<String> executionIdSet = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toSet());
        processExecutionList.forEach(rr -> executionIdSet.add(rr.getExecutionId()));


        //变量
        Map<String, Object> paramMap = taskResultDto.getVariableAll();
        Boolean taskExist = taskResultDto.getCurrentTask();
        if (!taskExist) {
            //任务已完成了
            List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessNodeRecordAssignUser::getTaskId, taskId)
                    .eq(ProcessNodeRecordAssignUser::getUserId, userId)
                    .eq(ProcessNodeRecordAssignUser::getFlowUniqueId, taskResultDto.getFlowUniqueId())
                    .in(ProcessNodeRecordAssignUser::getExecutionId, executionIdSet)
                    .orderByDesc(ProcessNodeRecordAssignUser::getUpdateTime)
                    .list();

            String data = processNodeRecordAssignUserList.get(0).getData();
            if (StrUtil.isNotBlank(data)) {
                Map<String, Object> collect = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                });
                paramMap.putAll(collect);

            }
        } else {

        }


        //当前节点数据
        String nodeId = taskResultDto.getNodeId();
        if (StrUtil.startWith(nodeId, ProcessInstanceConstant.VariableKey.STARTER)) {
            nodeId = ProcessInstanceConstant.VariableKey.STARTER;
        }
        String nodeDataJson =
                nodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = CommonUtil.toObj(nodeDataJson, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        List<FormItemVO> formItemVOList = CommonUtil.toArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {


            String id = formItemVO.getId();

            String perm = formPerms.get(id);


            if (StrUtil.isNotBlank(perm)) {

                formItemVO.setPerm((view || !taskExist) ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(perm) ?
                        ProcessInstanceConstant.FormPermClass.READ : perm) : perm);

            } else {
                formItemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
            }

            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(paramMap, id, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        Object value1 = map.get(itemVO.getId());

                        FormUtil.handValue(itemVO, value1);


                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm((view || !taskExist) ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub) ?
                                    ProcessInstanceConstant.FormPermClass.READ : permSub)
                                    : permSub
                            );


                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);
                {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {

                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {


                            itemVO.setPerm(permSub);

                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    formItemVO.getProps().setOriForm(subItemList);

                }

            } else {

                Object value = paramMap.get(id);
                FormUtil.handValue(formItemVO, value);


            }

        }


        //是否是子流程发起任务
        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(node);

        //发起人
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, taskResultDto.getProcessInstanceId()).one();
        String starterUserId = processInstanceRecord.getUserId();

        UserDto starterUser = ApiStrategyFactory.getStrategy().getUser(starterUserId);

        Object subProcessStarterNode =
                paramMap.get(ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE);
        Object rejectStarterNode = paramMap.get(ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE);


        TaskDetailViewVO taskDetailViewVO = TaskDetailViewVO.builder()
                .processInstanceId(taskResultDto.getProcessInstanceId())
                .node(nodeDataJson)
                .nodeId(nodeId)
                .taskExist(taskExist)
                .processName(oaForms.getName())
                .process(oaForms.getProcess())
                .starterAvatarUrl(starterUser.getAvatarUrl())
                .starterName(starterUser.getName())
                .startTime(processInstanceRecord.getCreateTime())
                .nodeName(node.getNodeName())
                .flowId(flowId)
                .selectUserNodeId(selectUserNodeId)
                .formItems(formItemVOList)
                .processInstanceStatus(processInstanceRecord.getStatus())
                .processInstanceResult(processInstanceRecord.getResult())
                .frontJoinTask(taskResultDto.getFrontJoinTask())
                .subProcessStarterTask(Convert.toBool(subProcessStarterNode, false) && rejectStarterNode == null)
                .build();


        return R.success(taskDetailViewVO);
    }

    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R completeTask(TaskParamDto taskParamDto) {
        String userId = StpUtil.getLoginIdAsString();
        taskParamDto.setUserId(String.valueOf(userId));


        com.cxygzl.common.dto.R r = CoreHttpUtil.completeTask(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 前加签
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R delegateTask(TaskParamDto taskParamDto) {


        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        UserDto user = ApiStrategyFactory.getStrategy().getUser(taskParamDto.getTargetUserId());
        taskParamDto.setTargetUserName(user.getName());

        com.cxygzl.common.dto.R r = CoreHttpUtil.delegateTask(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }
        //成功了 处理节点


        return R.success();
    }

    /**
     * 加签完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R resolveTask(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.resolveTask(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 设置执行人
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R setAssignee(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        UserDto user = ApiStrategyFactory.getStrategy().getUser(taskParamDto.getTargetUserId());
        taskParamDto.setTargetUserName(user.getName());
        String post = CoreHttpUtil.setAssignee(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 结束流程
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R stopProcessInstance(TaskParamDto taskParamDto) {

        String processInstanceId = taskParamDto.getProcessInstanceId();

        List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceId);
        CollUtil.reverse(allStopProcessInstanceIdList);
        allStopProcessInstanceIdList.add(processInstanceId);

        taskParamDto.setProcessInstanceIdList(allStopProcessInstanceIdList);
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        com.cxygzl.common.dto.R r = CoreHttpUtil.stopProcessInstance(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 退回
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R back(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.back(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    private List<String> getAllStopProcessInstanceIdList(String processInstanceId) {
        List<ProcessInstanceRecord> list = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getParentProcessInstanceId, processInstanceId).list();

        List<String> collect = list.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toList());

        for (ProcessInstanceRecord processInstanceRecord : list) {
            List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceRecord.getProcessInstanceId());

            collect.addAll(allStopProcessInstanceIdList);

        }
        return collect;
    }
}
