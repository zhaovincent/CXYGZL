package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.DataUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.*;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RemoteServiceImpl implements IRemoteService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessNodeRecordService processNodeRecordService;
    @Resource
    private IProcessExecutionService processExecutionService;
    @Resource
    private IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Resource
    private IProcessCopyService processCopyService;
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessGroupService processGroupService;

    @Resource
    private IProcessNodeDataService processNodeDataService;
    @Resource
    private IMessageService messageService;

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
    @Override
    public R saveCC(ProcessCopyDto copyDto) {

        String processInstanceId = copyDto.getProcessInstanceId();


        //如果抄送是第一个节点 会出现查询不到的情况

        ThreadUtil.execute(() -> {
            try {
                ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();

                int index = 10;
                while (index > 0 && processInstanceRecord == null) {
                    TimeUnit.SECONDS.sleep(5);
                    processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
                    index--;
                }

                ProcessCopy processCopy = BeanUtil.copyProperties(copyDto, ProcessCopy.class);
                processCopy.setGroupId(Long.valueOf(processInstanceRecord.getGroupId()));
                processCopy.setGroupName(processInstanceRecord.getGroupName());
                processCopy.setProcessName(processInstanceRecord.getName());
                processCopy.setStartTime(processInstanceRecord.getCreateTime());


                processCopyService.save(processCopy);
            } catch (Exception e) {
                log.error("Error:", e);
            }
        });


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

        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.cxygzl.common.dto.third.DeptDto> deptList = DataUtil.selectParentByDept(deptId, allDept);

        return R.success(deptList);
    }

    /**
     * 开始节点事件
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public R startNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
        return processNodeRecordService.start(recordParamDto);
    }

    /**
     * 记录父子执行id
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public R saveParentChildExecution(ProcessNodeRecordParamDto recordParamDto) {

        List<String> childExecutionId = recordParamDto.getChildExecutionId();
        if (CollUtil.isNotEmpty(childExecutionId)) {
            //子级

            for (String s : childExecutionId) {
                Long count = processExecutionService.lambdaQuery()
                        .eq(ProcessExecution::getExecutionId, recordParamDto.getExecutionId())
                        .eq(ProcessExecution::getChildExecutionId, s).count();
                if (count > 0) {
                    continue;
                }
                ProcessExecution entity = new ProcessExecution();
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
        entity.setGroupId(processGroup.getId());
        entity.setGroupName(processGroup.getGroupName());
        entity.setStatus(NodeStatusEnum.JXZ.getCode());
        String processStr = process.getProcess();
        Node node = JSON.parseObject(processStr, Node.class);
        NodeUtil.addEndNode(node);
        entity.setProcess(JSON.toJSONString(node));

        processInstanceRecordService.save(entity);



        //调用接口通知其他
        String formData = processInstanceRecordParamDto.getFormData();
        Map<String, Object> valueMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });
        ProcessDto processDto =
                ProcessDto.builder()
                        .processInstanceId(processInstanceRecordParamDto.getProcessInstanceId())
                        .mobile(processInstanceRecordParamDto.getUserId())
                        .flowId(process.getFlowId())
                        .formItemVOList(JSON.parseArray(process.getFormItems(), FormItemVO.class))
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
    public R endNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
        //处理任务
        ProcessNodeRecord processNodeRecord = processNodeRecordService.lambdaQuery()

                .eq(ProcessNodeRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                .eq(ProcessNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .eq(ProcessNodeRecord::getNodeId, recordParamDto.getNodeId()).one();
        if (processNodeRecord != null) {

            List<ProcessExecution> processExecutionList = processExecutionService.lambdaQuery().eq(ProcessExecution::getExecutionId,
                    processNodeRecord.getExecutionId()).list();
            if (!processExecutionList.isEmpty()) {

                List<String> collect = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toList());

                processNodeRecordAssignUserService.lambdaUpdate()
                        .set(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YCX.getCode())
                        .set(ProcessNodeRecordAssignUser::getTaskType, ProcessInstanceConstant.TaskType.CANCEL)
                        .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                        .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                        .eq(ProcessNodeRecordAssignUser::getNodeId, recordParamDto.getNodeId())
                        .in(ProcessNodeRecordAssignUser::getExecutionId, collect)
                        .update(new ProcessNodeRecordAssignUser());
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
    public R cancelNodeEvent(ProcessNodeRecordParamDto recordParamDto) {


        //处理任务
        ProcessNodeRecord processNodeRecord = processNodeRecordService.lambdaQuery()

                .eq(ProcessNodeRecord::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                .eq(ProcessNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .eq(ProcessNodeRecord::getNodeId, recordParamDto.getNodeId()).one();
        if (processNodeRecord != null) {

            List<ProcessExecution> processExecutionList = processExecutionService.lambdaQuery().eq(ProcessExecution::getExecutionId,
                    processNodeRecord.getExecutionId()).list();
            if (!processExecutionList.isEmpty()) {

                List<String> collect = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toList());

                processNodeRecordAssignUserService.lambdaUpdate()
                        .set(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YCX.getCode())
                        .set(ProcessNodeRecordAssignUser::getTaskType, ProcessInstanceConstant.TaskType.CANCEL)
                        .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, recordParamDto.getProcessInstanceId())
                        .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                        .eq(ProcessNodeRecordAssignUser::getNodeId, recordParamDto.getNodeId())
                        .in(ProcessNodeRecordAssignUser::getExecutionId, collect)
                        .update(new ProcessNodeRecordAssignUser());
            }


        }

        processNodeRecordService.cancelNodeEvent(recordParamDto);

        return R.success();
    }

    /**
     * 开始设置执行人
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return processNodeRecordAssignUserService.addAssignUser(processNodeRecordAssignUserParamDto);
    }

    /**
     * 任务结束事件
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        return processNodeRecordAssignUserService.taskEndEvent(processNodeRecordAssignUserParamDto);
    }

    /**
     * 任务取消了--驳回
     *
     * @param processNodeRecordAssignUserParamDto
     * @return
     */
    @Override
    public R taskCancelEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
        processNodeRecordAssignUserService.taskCancelEvent(processNodeRecordAssignUserParamDto);

        return R.success();
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
        return R.success(JSON.parseObject(settings, FlowSettingDto.class));
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
