package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessInstanceAssignUserRecord;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.third.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessInstanceExecutionService executionService;

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
                    if(b){
                        continue;
                    }
                    UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
                    userNameList.add(user.getName());
                }

                return R.fail(StrUtil.format("用户：{}的任务非进行中，不能减签",CollUtil.join(userNameList,",")));
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
}
