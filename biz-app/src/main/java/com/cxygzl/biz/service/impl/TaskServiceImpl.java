package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
import com.cxygzl.common.constants.ApproveDescTypeEnum;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.OperTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.SimpleApproveDescDto;
import com.cxygzl.common.dto.TaskDto;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.UploadValue;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    private IRemoteService remoteService;
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
    @Resource
    private IProcessInstanceOperRecordService processInstanceOperRecordService;

    /**
     * 提交评论
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public R submitComment(TaskParamDto taskParamDto) {

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, taskParamDto.getProcessInstanceId()).one();
        String process = processInstanceRecord.getProcess();
        Node node = JsonUtil.parseObject(process, Node.class);
        //找到根路径的最后一个没有执行的节点
        SimpleApproveDescDto simpleApproveDescDto = new SimpleApproveDescDto();
        simpleApproveDescDto.setDate(new Date());

        Dict set = Dict.create().set("content", taskParamDto.getApproveDesc()).set("title", "添加了评论").set("userId", StpUtil.getLoginIdAsString()).set("sys", false);
        simpleApproveDescDto.setMessage(JsonUtil.toJSONString(set));
        simpleApproveDescDto.setMsgId(IdUtil.fastUUID());
        simpleApproveDescDto.setUserId(StpUtil.getLoginIdAsString());
        simpleApproveDescDto.setType(ApproveDescTypeEnum.COMMENT.getType());
        simpleApproveDescDto.setSys(false);
        List<UploadValue> approveFileList = taskParamDto.getApproveFileList();
        simpleApproveDescDto.setApproveFileList(approveFileList);

        List<UploadValue> approveImageList = taskParamDto.getApproveImageList();
        simpleApproveDescDto.setApproveImageList(approveImageList);

        com.cxygzl.biz.utils.NodeUtil.addCommentNode(node, StpUtil.getLoginIdAsString(), simpleApproveDescDto);
        processInstanceRecord.setProcess(JsonUtil.toJSONString(node));
        processInstanceRecordService.updateById(processInstanceRecord);

        return R.success();
    }

    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R completeTask(TaskParamDto taskParamDto) {
        String userId = StpUtil.getLoginIdAsString();
        taskParamDto.setUserId(String.valueOf(userId));


        com.cxygzl.common.dto.R r = CoreHttpUtil.completeTask(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }

        if (taskParamDto.getApproveResult()) {
            processInstanceOperRecordService.saveRecord(userId, taskParamDto, OperTypeEnum.PASS.getValue(), "提交任务");
        } else {
            processInstanceOperRecordService.saveRecord(userId, taskParamDto, OperTypeEnum.REFUSE.getValue(), "提交任务");
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

        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.FRONT_JOIN.getValue(), "委派任务");


        return R.success();
    }

    /**
     * 加签完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R resolveTask(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.resolveTask(taskParamDto);
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }

        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.RESOLVE.getValue(), "完成任务");


        return R.success();
    }

    /**
     * 设置执行人
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R setAssignee(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        UserDto user = ApiStrategyFactory.getStrategy().getUser(taskParamDto.getTargetUserId());
        taskParamDto.setTargetUserName(user.getName());
        String post = CoreHttpUtil.setAssignee(taskParamDto);
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.BACK_JOIN.getValue(), "转办任务");
        return R.success();
    }

    /**
     * 添加执行人
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
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
                    .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime).list().get(0);
            //查询未执行的人员--待办人员
            R<List<TaskDto>> listR = CoreHttpUtil.queryTaskAssignee(processInstanceAssignUserRecord.getNodeId(),
                    processInstanceAssignUserRecord.getProcessInstanceId());
            if (!listR.isOk()) {
                return R.fail(listR.getMsg());
            }
            List<TaskDto> taskDtoList = listR.getData();
            //过滤一下要减签的
            List<TaskDto> collect =
                    taskDtoList.stream().filter(w -> targetUserIdList.contains(w.getAssign())).collect(Collectors.toList());


            if (CollUtil.isNotEmpty(collect)) {
                Set<String> uidSet = collect.stream().map(w -> w.getAssign()).collect(Collectors.toSet());

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
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.ADD_ASSIGNEE.getValue(), "添加审批人");
        return R.success();
    }

    /**
     * 减少执行人
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
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
                    .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime).list().get(0);

            //查询未执行的人员--待办人员
            R<List<TaskDto>> listR = CoreHttpUtil.queryTaskAssignee(processInstanceAssignUserRecord.getNodeId(),
                    processInstanceAssignUserRecord.getProcessInstanceId());
            if (!listR.isOk()) {
                return R.fail(listR.getMsg());
            }
            List<TaskDto> taskDtoList = listR.getData();
            //过滤一下要减签的
            List<TaskDto> collect =
                    taskDtoList.stream().filter(w -> targetUserIdList.contains(w.getAssign())).collect(Collectors.toList());


            if (collect.size() != targetUserIdList.size()) {
                List<String> userNameList = new ArrayList<>();

                for (String s : targetUserIdList) {
                    boolean b = collect.stream().anyMatch(w -> StrUtil.equals(w.getAssign(), s));
                    if (b) {
                        continue;
                    }
                    UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
                    userNameList.add(user.getName());
                }

                return R.fail(StrUtil.format("用户：{}的任务非进行中，不能减签", CollUtil.join(userNameList, ",")));
            }

            List<String> executionIdList = collect.stream().map(w -> w.getExecutionId()).collect(Collectors.toList());

            taskParamDto.setTargetExecutionIdList(executionIdList);
        }
        List<String> targetUserNameList = new ArrayList<>();
        for (String s : targetUserIdList) {
            UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
            targetUserNameList.add(user.getName());
        }
        taskParamDto.setTargetUserNameList(targetUserNameList);

        String post = CoreHttpUtil.delAssignee(taskParamDto);
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }

        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.DEL_ASSIGNEE.getValue(), "减少审批人");

        return R.success();
    }


    /**
     * 退回
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R back(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.back(taskParamDto);
        com.cxygzl.common.dto.R r = JsonUtil.parseObject(post, new JsonUtil.TypeReference<R>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.REJECT.getValue(), "驳回任务");
        return R.success();
    }


    /**
     * 撤回
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
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
        Node currentProcessRootNode = JsonUtil.parseObject(processInstanceRecord.getProcess(), Node.class);

        Node currentNode = nodeDataService.getNode(processInstanceNodeRecord.getFlowId(), processInstanceNodeRecord.getNodeId()).getData();
        if (!nodeQueryVO.getContainGateway()) {
            NodeUtil.handleChildrenAfterJump(currentProcessRootNode, processInstanceNodeRecordList.get(0).getNodeId(), currentNode);
            processInstanceRecordService.lambdaUpdate()
                    .set(ProcessInstanceRecord::getProcess, JsonUtil.toJSONString(currentProcessRootNode))
                    .eq(ProcessInstanceRecord::getId, processInstanceRecord.getId())
                    .update(new ProcessInstanceRecord());
        } else {
            //经过网关
            NodeUtil.handleChildrenAfterJump(currentProcessRootNode, nodeQueryVO.getGatewayId(), currentNode);
            processInstanceRecordService.lambdaUpdate()
                    .set(ProcessInstanceRecord::getProcess, JsonUtil.toJSONString(currentProcessRootNode))
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

        processInstanceOperRecordService.saveRecord(StpUtil.getLoginIdAsString(), taskParamDto,
                OperTypeEnum.REVOKE.getValue(), "撤回任务");
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

            } else {
                processInstanceNodeRecordList.add(instanceNodeRecord);

            }
        }

        nodeQueryVO.setProcessInstanceNodeRecordList(processInstanceNodeRecordList);
        return nodeQueryVO;
    }
}
