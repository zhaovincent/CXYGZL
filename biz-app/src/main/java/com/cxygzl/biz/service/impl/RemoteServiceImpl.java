package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.DataUtil;
import com.cxygzl.common.constants.TaskTypeEnum;
import com.cxygzl.common.dto.ProcessInstanceParamDto;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.TaskParamDto;
import com.cxygzl.common.dto.third.*;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RemoteServiceImpl implements IRemoteService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessInstanceExecutionService processExecutionService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Resource
    private IProcessInstanceCopyService processCopyService;
    @Resource
    private IProcessInstanceUserCopyService processInstanceUserCopyService;
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessGroupService processGroupService;

    @Resource
    private IProcessNodeDataService processNodeDataService;
    @Resource
    private IMessageService messageService;

    /**
     * 根据用户id查询角色id集合
     *
     * @param userId
     * @return
     */
    @Override
    public R<List<String>> queryRoleIdListByUserId(String userId) {
        List<String> list = ApiStrategyFactory.getStrategy().loadRoleIdListByUserId(userId);
        return R.success(list);
    }

    /**
     * 根据部门id获取部门列表
     *
     * @param deptIdList
     * @return
     */
    @Override
    public R<List<DeptDto>> queryDeptList(List<String> deptIdList) {
        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<DeptDto> collect = allDept.stream().filter(w -> deptIdList.contains(w.getId())).collect(Collectors.toList());
        return R.success(collect);
    }

    /**
     * 保存待办任务
     *
     * @param messageDto
     * @return
     */
    @Override
    public R saveMessage(MessageDto messageDto) {

        messageService.saveMessage(messageDto);
        ApiStrategyFactory.getStrategy().sendMsg(messageDto);
        return R.success();
    }

    /**
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     * @return
     */
    @Override
    public R<List<String>> queryUserIdListByRoleIdList(List<String> roleIdList) {

        List<String> userIdList =
                ApiStrategyFactory.getStrategy().loadUserIdListByRoleIdList(roleIdList.stream().map(w -> (w)).collect(Collectors.toList()));


        return R.success(userIdList);
    }

    /**
     * 保存抄送
     *
     * @param copyDto
     * @return
     */
    @Transactional
    @Override
    public R saveCC(ProcessInstanceCopyDto copyDto) {

        String processInstanceId = copyDto.getProcessInstanceId();

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        ProcessInstanceCopy processInstanceCopy = BeanUtil.copyProperties(copyDto, ProcessInstanceCopy.class);
        processInstanceCopy.setGroupId(Long.valueOf(processInstanceRecord.getGroupId()));
        processInstanceCopy.setGroupName(processInstanceRecord.getGroupName());
        processInstanceCopy.setProcessName(processInstanceRecord.getName());
        processInstanceCopy.setStartTime(processInstanceRecord.getCreateTime());


        processCopyService.save(processInstanceCopy);

        Long count = processInstanceUserCopyService.lambdaQuery()
                .eq(ProcessInstanceUserCopy::getUserId, copyDto.getUserId())
                .eq(ProcessInstanceUserCopy::getProcessInstanceId, copyDto.getProcessInstanceId())
                .count();

        log.info("抄送数量:{} {} {}", copyDto.getUserId(), copyDto.getProcessInstanceId(), count);
        if (count == 0) {
            ProcessInstanceUserCopy processInstanceUserCopy = BeanUtil.copyProperties(copyDto, ProcessInstanceUserCopy.class);
            processInstanceUserCopy.setGroupId(Long.valueOf(processInstanceRecord.getGroupId()));
            processInstanceUserCopy.setGroupName(processInstanceRecord.getGroupName());
            processInstanceUserCopy.setProcessName(processInstanceRecord.getName());


            processInstanceUserCopyService.save(processInstanceUserCopy);
        }


        return R.success();
    }

    /**
     * 检查是否是所有的父级
     *
     * @param checkParentDto
     * @return
     */
    @Override
    public R<Boolean> checkIsAllParent(CheckParentDto checkParentDto) {

        String parentId = checkParentDto.getParentId();
        List<String> deptIdList = checkParentDto.getDeptIdList();
        //查询子级包括自己
        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.cxygzl.common.dto.third.DeptDto> childrenDeptList = DataUtil.selectChildrenByDept(parentId, allDept);


        List<String> childrenDeptIdList = childrenDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());
        childrenDeptIdList.remove(parentId);

        List<String> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(childrenDeptIdList, String.class));

        return R.success(remainIdList.isEmpty());
    }

    /**
     * 根据部门id集合查询用户id集合
     *
     * @param depIdList
     * @return
     */
    @Override
    public R<List<String>> queryUserIdListByDepIdList(List<String> depIdList) {

        List<String> userIdList =
                ApiStrategyFactory.getStrategy().loadUserIdListByDeptIdList(depIdList.stream().map(w -> String.valueOf(w)).collect(Collectors.toList()));
        return R.success(userIdList);
    }

    /**
     * 检查是否是所有的子级
     *
     * @param checkChildDto
     * @return
     */
    @Override
    public R<Boolean> checkIsAllChild(CheckChildDto checkChildDto) {
        String childId = checkChildDto.getChildId();
        List<String> deptIdList = checkChildDto.getDeptIdList();
        //查询父级包括自己

        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.cxygzl.common.dto.third.DeptDto> parentDeptList = DataUtil.selectParentByDept(childId, allDept);

        List<String> parentDeptIdList = parentDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());
        parentDeptIdList.remove(childId);

        List<String> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(parentDeptIdList, String.class));

        return R.success(remainIdList.isEmpty());
    }

    /**
     * 获取用户的信息-包括扩展字段
     *
     * @param userId
     * @return
     */
    @Override
    public R<Map<String, Object>> queryUserAllInfo(String userId) {

        UserDto user = ApiStrategyFactory.getStrategy().getUser(String.valueOf(userId));


        Map<String, Object> map = BeanUtil.beanToMap(user, "id", "name", "deptId"
        );

        Map<String, String> fieldMap = ApiStrategyFactory.getStrategy().queryUserFieldData(String.valueOf(userId));

        map.putAll(fieldMap);

        return R.success(map);
    }

    /**
     * 查询用户属性配置
     *
     * @return
     */
    @Override
    public R<List<UserFieldDto>> queryUseField() {
        List<com.cxygzl.common.dto.third.UserFieldDto> userFieldList = ApiStrategyFactory.getStrategy().queryUserFieldList();
        return R.success(userFieldList);
    }

    /**
     * 根据当前用户查询包括自己部门在内的上级部门对象
     *
     * @param userId
     * @return
     */
    @Override
    public R<List<DeptDto>> queryParentDepListByUserId(String userId) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(String.valueOf(userId));
        String deptId = (user.getDeptId());


        return queryParentDepList(deptId);
    }

    /**
     * 查询上级部门
     *
     * @param deptId
     * @return
     */
    @Override
    public R<List<DeptDto>> queryParentDepList(String deptId) {

        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.cxygzl.common.dto.third.DeptDto> deptList = DataUtil.selectParentByDept(deptId, allDept);

        return R.success(deptList);
    }

    /**
     * 根据用户查询子级部门
     *
     * @param userId
     * @return
     */
    @Override
    public R<List<DeptDto>> queryChildDeptListByUserId(String userId) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(String.valueOf(userId));
        String deptId = (user.getDeptId());
        return queryChildDeptList(deptId);
    }

    /**
     * 获取子级部门集合
     *
     * @param deptId
     * @return
     */
    @Override
    public R<List<DeptDto>> queryChildDeptList(String deptId) {
        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.cxygzl.common.dto.third.DeptDto> deptList = DataUtil.selectChildrenByDept(deptId, allDept);

        return R.success(deptList);
    }

    /**
     * 开始节点事件
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public R startNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto) {
        return processNodeRecordService.start(recordParamDto);
    }

    /**
     * 记录父子执行id
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public R saveParentChildExecution(ProcessInstanceNodeRecordParamDto recordParamDto) {

        List<String> childExecutionId = recordParamDto.getChildExecutionId();
        if (CollUtil.isNotEmpty(childExecutionId)) {
            //子级

            for (String s : childExecutionId) {
                Long count = processExecutionService.lambdaQuery()
                        .eq(ProcessInstanceExecution::getExecutionId, recordParamDto.getExecutionId())
                        .eq(ProcessInstanceExecution::getChildExecutionId, s).count();
                if (count > 0) {
                    continue;
                }
                ProcessInstanceExecution entity = new ProcessInstanceExecution();
                entity.setChildExecutionId(s);
                entity.setExecutionId(recordParamDto.getExecutionId());
                processExecutionService.save(entity);
            }
        }

        return R.success();
    }

    /**
     * 流程创建了
     *
     * @param processInstanceRecordParamDto
     * @return
     */
    @Override
    public R startProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        ProcessInstanceRecord entity = BeanUtil.copyProperties(processInstanceRecordParamDto,
                ProcessInstanceRecord.class);


        Process process = processService.getByFlowId(processInstanceRecordParamDto.getFlowId());

        ProcessGroup processGroup = processGroupService.getById(process.getGroupId());

        entity.setName(process.getName());
        entity.setLogo(process.getLogo());
        entity.setUserId(processInstanceRecordParamDto.getUserId());
        entity.setFlowId(processInstanceRecordParamDto.getFlowId());
        entity.setProcessInstanceId(processInstanceRecordParamDto.getProcessInstanceId());
        entity.setProcessInstanceBizKey(processInstanceRecordParamDto.getProcessInstanceBizKey());
        entity.setProcessInstanceBizCode(processInstanceRecordParamDto.getProcessInstanceBizCode());
        entity.setGroupId(processGroup.getId());
        entity.setGroupName(processGroup.getGroupName());
        entity.setStatus(NodeStatusEnum.JXZ.getCode());
        String processStr = process.getProcess();
        Node node = JsonUtil.parseObject(processStr, Node.class);
        NodeUtil.addEndNode(node);
        entity.setProcess(JsonUtil.toJSONString(node));

        processInstanceRecordService.save(entity);


        //调用接口通知其他
        String formData = processInstanceRecordParamDto.getFormData();
        Map<String, Object> valueMap = JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
        });
        StartProcessDto processDto =
                StartProcessDto.builder()
                        .processInstanceId(processInstanceRecordParamDto.getProcessInstanceId())
                        .userId(processInstanceRecordParamDto.getUserId())
                        .flowId(process.getFlowId())
                        .formItemVOList(JsonUtil.parseArray(process.getFormItems(), FormItemVO.class))
                        .valueMap(valueMap).build();
        ApiStrategyFactory.getStrategy().startProcess(processDto);

        return R.success();
    }

    /**
     * 完成节点事件
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public R endNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto) {
        //处理任务
        List<ProcessInstanceNodeRecord> processNodeRecordList = processNodeRecordService.lambdaQuery()
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .eq(ProcessInstanceNodeRecord::getNodeId, recordParamDto.getNodeId()).list();
        if (!processNodeRecordList.isEmpty()) {
            ProcessInstanceNodeRecord processNodeRecord = null;
            if (processNodeRecordList.size() == 1) {
                processNodeRecord = processNodeRecordList.get(0);
            } else {
                processNodeRecord = processNodeRecordList.stream().filter(w -> StrUtil.equals(w.getExecutionId(),
                        recordParamDto.getExecutionId())).findAny().orElse(null);
            }

            List<ProcessInstanceExecution> processExecutionList = processExecutionService.lambdaQuery().eq(ProcessInstanceExecution::getExecutionId,
                    processNodeRecord.getExecutionId()).list();
            if (!processExecutionList.isEmpty()) {

                List<String> collect = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toList());

                {
                    //处理
                    List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery().eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                            .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                            .eq(ProcessInstanceAssignUserRecord::getNodeId, recordParamDto.getNodeId())
                            .in(ProcessInstanceAssignUserRecord::getExecutionId, collect).list();
                    if (CollUtil.isNotEmpty(list)) {
                        List<TaskParamDto> taskParamDtoList = new ArrayList<>();
                        for (ProcessInstanceAssignUserRecord processInstanceAssignUserRecord : list) {
                            TaskParamDto taskParamDto = new TaskParamDto();
                            taskParamDto.setProcessInstanceId(processInstanceAssignUserRecord.getProcessInstanceId());
                            taskParamDto.setUserId(processInstanceAssignUserRecord.getUserId());
                            taskParamDto.setTaskId(processInstanceAssignUserRecord.getTaskId());
                            taskParamDtoList.add(taskParamDto);
                        }

                        ApiStrategyFactory.getStrategy().handleTask(taskParamDtoList, TaskTypeEnum.CANCEL.getValue());
                    }


                }

                processNodeRecordAssignUserService.lambdaUpdate()
                        .set(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.YCX.getCode())
                        .set(ProcessInstanceAssignUserRecord::getTaskType, TaskTypeEnum.CANCEL.getValue())
                        .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                        .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                        .eq(ProcessInstanceAssignUserRecord::getNodeId, recordParamDto.getNodeId())
                        .in(ProcessInstanceAssignUserRecord::getExecutionId, collect)
                        .update(new ProcessInstanceAssignUserRecord());

            }


        }
        return processNodeRecordService.endNodeEvent(recordParamDto);
    }

    /**
     * 节点取消
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public R cancelNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto) {


        //处理任务
        ProcessInstanceNodeRecord processInstanceNodeRecord = processNodeRecordService.lambdaQuery()

                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .eq(ProcessInstanceNodeRecord::getNodeId, recordParamDto.getNodeId()).one();
        if (processInstanceNodeRecord != null) {

            List<ProcessInstanceExecution> processInstanceExecutionList = processExecutionService.lambdaQuery().eq(ProcessInstanceExecution::getExecutionId,
                    processInstanceNodeRecord.getExecutionId()).list();
            if (!processInstanceExecutionList.isEmpty()) {

                List<String> collect = processInstanceExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toList());

                processNodeRecordAssignUserService.lambdaUpdate()
                        .set(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.YCX.getCode())
                        .set(ProcessInstanceAssignUserRecord::getTaskType, TaskTypeEnum.CANCEL.getValue())
                        .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                        .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                        .eq(ProcessInstanceAssignUserRecord::getNodeId, recordParamDto.getNodeId())
                        .in(ProcessInstanceAssignUserRecord::getExecutionId, collect)
                        .update(new ProcessInstanceAssignUserRecord());
            }


        }

        processNodeRecordService.cancelNodeEvent(recordParamDto);

        return R.success();
    }

    /**
     * 开始设置执行人
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R startAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return processNodeRecordAssignUserService.addAssignUser(processInstanceAssignUserRecordParamDto);
    }

    /**
     * 任务结束事件
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R taskCompletedEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return processNodeRecordAssignUserService.taskCompletedEvent(processInstanceAssignUserRecordParamDto);
    }

    /**
     * 任务结束
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public R taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return processNodeRecordAssignUserService.taskEndEvent(processInstanceAssignUserRecordParamDto);
    }


    /**
     * 实例结束
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public R endProcess(ProcessInstanceParamDto processInstanceParamDto) {
        return processInstanceService.end(processInstanceParamDto);
    }

    /**
     * 查询流程管理员
     *
     * @param flowId
     * @return
     */
    @Override
    public R<String> queryProcessAdmin(String flowId) {
        Process process = processService.getByFlowId(flowId);
        return R.success(process.getAdminId());
    }

    /**
     * 查询流程设置
     *
     * @param flowId
     * @return
     */
    @Override
    public R<FlowSettingDto> queryProcessSetting(String flowId) {
        Process process = processService.getByFlowId(flowId);
        String settings = process.getSettings();
        if (StrUtil.isBlank(settings)) {
            return R.fail("该流程没有设置");
        }
        return R.success(JsonUtil.parseObject(settings, FlowSettingDto.class));
    }

    /**
     * 查询流程数据
     *
     * @param flowId
     * @return
     */
    @Override
    public R<ProcessDto> queryProcess(String flowId) {
        Process process = processService.getByFlowId(flowId);

        return R.success(BeanUtil.copyProperties(process, ProcessDto.class));
    }

    /**
     * 保存流程节点数据
     *
     * @param processNodeDataDto
     * @return
     */
    @Override
    public R saveNodeData(ProcessNodeDataDto processNodeDataDto) {
        return processNodeDataService.saveNodeData(processNodeDataDto);
    }

    /***
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    @Override
    public R<String> getNodeData(String flowId, String nodeId) {
        return processNodeDataService.getNodeData(flowId, nodeId);
    }
}
