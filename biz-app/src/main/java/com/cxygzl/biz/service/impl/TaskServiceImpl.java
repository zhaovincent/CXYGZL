package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceAssignUserRecord;
import com.cxygzl.biz.entity.ProcessInstanceExecution;
import com.cxygzl.biz.entity.ProcessInstanceNodeRecord;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.vo.NextNodeQueryVO;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceNodeRecordService processInstanceNodeRecordService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessInstanceExecutionService executionService;
    @Resource
    private IProcessInstanceAssignUserRecordService processInstanceAssignUserRecordService;

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
     * 添加执行人
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R addAssignee(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());

        List<String> targetUserIdList = taskParamDto.getTargetUserIdList();
        //判断当前用户是否已经存在了
        String taskId = taskParamDto.getTaskId();

        {
            ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, taskId)
                    .eq(ProcessInstanceAssignUserRecord::getUserId, StpUtil.getLoginIdAsString())
                    .one();

            List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceAssignUserRecord.getProcessInstanceId())
                    .eq(ProcessInstanceAssignUserRecord::getFlowUniqueId, processInstanceAssignUserRecord.getFlowUniqueId())
                    .eq(ProcessInstanceAssignUserRecord::getNodeId, processInstanceAssignUserRecord.getNodeId())
                    .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                    .in(ProcessInstanceAssignUserRecord::getUserId, targetUserIdList)
                    .list();
            if (CollUtil.isNotEmpty(list)) {
                Set<String> uidSet = list.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

                List<String> userNameList = new ArrayList<>();
                for (String s : uidSet) {
                    UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
                    userNameList.add(user.getName());
                }
                return R.fail(StrUtil.format("{}，已经是该任务执行人，不能被加签", CollUtil.join(userNameList, ",")));
            }

        }
        List<String> targetUserNameList = new ArrayList<>();
        for (String s : targetUserIdList) {
            UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
            targetUserNameList.add(user.getName());
        }
        taskParamDto.setTargetUserNameList(targetUserNameList);

        String post = CoreHttpUtil.addAssignee(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 减少执行人
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R delAssignee(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());

        List<String> targetUserIdList = taskParamDto.getTargetUserIdList();
        if (targetUserIdList.contains(StpUtil.getLoginIdAsString())) {
            return R.fail("不能删除当前用户");
        }
        //判断当前用户是否已经存在了
        String taskId = taskParamDto.getTaskId();

        {
            ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, taskId)
                    .eq(ProcessInstanceAssignUserRecord::getUserId, StpUtil.getLoginIdAsString())
                    .one();

            List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceAssignUserRecord.getProcessInstanceId())
                    .eq(ProcessInstanceAssignUserRecord::getFlowUniqueId, processInstanceAssignUserRecord.getFlowUniqueId())
                    .eq(ProcessInstanceAssignUserRecord::getNodeId, processInstanceAssignUserRecord.getNodeId())
                    .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                    .in(ProcessInstanceAssignUserRecord::getUserId, targetUserIdList)
                    .list();
            if (list.size() != targetUserIdList.size()) {
                List<String> userNameList = new ArrayList<>();

                for (String s : targetUserIdList) {
                    boolean b = list.stream().anyMatch(w -> StrUtil.equals(w.getUserId(), s));
                    if (b) {
                        continue;
                    }
                    UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
                    userNameList.add(user.getName());
                }

                return R.fail(StrUtil.format("用户：{}的任务非进行中，不能减签", CollUtil.join(userNameList, ",")));
            }

            List<String> executionIdList = list.stream().map(w -> w.getExecutionId()).collect(Collectors.toList());

            taskParamDto.setTargetExecutionIdList(executionIdList);
        }
        List<String> targetUserNameList = new ArrayList<>();
        for (String s : targetUserIdList) {
            UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
            targetUserNameList.add(user.getName());
        }
        taskParamDto.setTargetUserNameList(targetUserNameList);

        String post = CoreHttpUtil.delAssignee(taskParamDto);
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

    /**
     * 撤回
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R revoke(TaskParamDto taskParamDto) {
        //当前的任务id


        String executionId = taskParamDto.getExecutionId();

        String processInstanceId = taskParamDto.getProcessInstanceId();

        //判断流程是否进行中
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                .eq(ProcessInstanceRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .one();
        if (processInstanceRecord == null) {
            return R.fail("流程已结束，不能撤回了");
        }


        //已经完成的执行实例
        ProcessInstanceExecution processInstanceExecution = executionService.lambdaQuery()
                .eq(ProcessInstanceExecution::getChildExecutionId, executionId)
                .one();

        //已经完成的节点--用户已办任务节点
        ProcessInstanceNodeRecord processInstanceNodeRecord = processInstanceNodeRecordService.lambdaQuery()
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId)
                .eq(ProcessInstanceNodeRecord::getExecutionId, processInstanceExecution.getExecutionId())
                .one();

        //查询下级的节点
        NextNodeQueryVO nodeQueryVO = queryNextNode(processInstanceNodeRecord);
        List<ProcessInstanceNodeRecord> processInstanceNodeRecordList = nodeQueryVO.getProcessInstanceNodeRecordList();
        if (CollUtil.isEmpty(processInstanceNodeRecordList)) {
            return R.fail("未找到下级节点，不能撤回");
        }
//        if(processInstanceNodeRecordList.size()>1){
//            return R.fail("暂不支持多子级节点撤回");
//        }

        for (ProcessInstanceNodeRecord instanceNodeRecord : processInstanceNodeRecordList) {
            //如果不是用户任务 直接不能撤回
            Integer nodeType = instanceNodeRecord.getNodeType();
            if (nodeType != NodeTypeEnum.APPROVAL.getValue().intValue()) {
                return R.fail("下级节点非审批节点，不能撤回");
            }
            //判断是否进行中
            if (instanceNodeRecord.getStatus() != NodeStatusEnum.JXZ.getCode()) {
                return R.fail("审批节点已完成，不能撤回");
            }
        }

        //重新构建流程树
        Node currentProcessRootNode = com.alibaba.fastjson.JSON.parseObject(processInstanceRecord.getProcess(), Node.class);

        Node currentNode = nodeDataService.getNode(processInstanceNodeRecord.getFlowId(), processInstanceNodeRecord.getNodeId()).getData();
        if (!nodeQueryVO.getContainGateway()) {
            NodeUtil.handleChildrenAfterJump(currentProcessRootNode, processInstanceNodeRecordList.get(0).getNodeId(), currentNode);
            processInstanceRecordService.lambdaUpdate()
                    .set(ProcessInstanceRecord::getProcess, com.alibaba.fastjson.JSON.toJSONString(currentProcessRootNode))
                    .eq(ProcessInstanceRecord::getId, processInstanceRecord.getId())
                    .update(new ProcessInstanceRecord());
        } else {
            //经过网关
            NodeUtil.handleChildrenAfterJump(currentProcessRootNode, nodeQueryVO.getGatewayId(), currentNode);
            processInstanceRecordService.lambdaUpdate()
                    .set(ProcessInstanceRecord::getProcess, com.alibaba.fastjson.JSON.toJSONString(currentProcessRootNode))
                    .eq(ProcessInstanceRecord::getId, processInstanceRecord.getId())
                    .update(new ProcessInstanceRecord());
        }


        //查找正在执行的所有的任务id
        List<String> executionIdList = processInstanceNodeRecordList.stream().map(w -> w.getExecutionId()).collect(Collectors.toList());
        List<String> childrenExecutionIdList = executionService.lambdaQuery()
                .in(ProcessInstanceExecution::getExecutionId, executionIdList)
                .list().stream().map(ProcessInstanceExecution::getChildExecutionId).collect(Collectors.toList());

        List<ProcessInstanceAssignUserRecord> processInstanceAssignUserRecordList = processInstanceAssignUserRecordService.lambdaQuery()
                .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceId)
                .in(ProcessInstanceAssignUserRecord::getExecutionId, childrenExecutionIdList)
                .list();
        List<String> taskIdList = processInstanceAssignUserRecordList.stream().map(w -> w.getTaskId()).collect(Collectors.toList());

        List<String> nodeIdList = processInstanceNodeRecordList.stream().map(w -> w.getNodeId()).collect(Collectors.toList());

        taskParamDto.setTargetNodeId(processInstanceNodeRecord.getNodeId());
        taskParamDto.setNodeIdList(nodeIdList);
        taskParamDto.setTaskIdList(taskIdList);
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        R r = CoreHttpUtil.revoke(taskParamDto);

        if (!r.isOk()) {
            throw new BusinessException(r.getMsg());
        }

        return r;
    }

    /**
     * 获取下一个节点，不包含分支
     *
     * @param processInstanceNodeRecord
     * @return
     */
    private NextNodeQueryVO queryNextNode(ProcessInstanceNodeRecord processInstanceNodeRecord) {

        NextNodeQueryVO nodeQueryVO = new NextNodeQueryVO();
        nodeQueryVO.setContainGateway(false);

        List<ProcessInstanceNodeRecord> processInstanceNodeRecordList = new ArrayList<>();

        List<ProcessInstanceNodeRecord> list = processInstanceNodeRecordService.lambdaQuery()
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceNodeRecord.getProcessInstanceId())
                .eq(ProcessInstanceNodeRecord::getParentNodeId, processInstanceNodeRecord.getNodeId())
                .ge(ProcessInstanceNodeRecord::getStartTime, processInstanceNodeRecord.getStartTime())
                .list();
        if (CollUtil.isEmpty(list)) {
            nodeQueryVO.setProcessInstanceNodeRecordList(processInstanceNodeRecordList);
            return nodeQueryVO;
        }
        for (ProcessInstanceNodeRecord instanceNodeRecord : list) {
            Integer nodeType = instanceNodeRecord.getNodeType();
            if (NodeTypeEnum.getByValue(nodeType).getBranch()) {
                //分支
                nodeQueryVO.setContainGateway(true);
                nodeQueryVO.setGatewayId(list.get(0).getNodeId());

                NextNodeQueryVO nodeQueryVO1 = queryNextNode(instanceNodeRecord);
                processInstanceNodeRecordList.addAll(nodeQueryVO1.getProcessInstanceNodeRecordList());
                if (nodeQueryVO1.getContainGateway()) {
                    nodeQueryVO.setContainGateway(true);
                }

            }else{
                processInstanceNodeRecordList.add(instanceNodeRecord);

            }
        }

        nodeQueryVO.setProcessInstanceNodeRecordList(processInstanceNodeRecordList);
        return nodeQueryVO;
    }
}
