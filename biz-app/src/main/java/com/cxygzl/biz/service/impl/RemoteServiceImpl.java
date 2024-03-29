package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.mapper.DeptMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.DataUtil;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.UserDto;
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
    private IUserService userService;
    @Resource
    private IUserFieldDataService userFieldDataService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Resource
    private IProcessInstanceCopyService processCopyService;
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessGroupService processGroupService;
    @Resource
    private IUserRoleService userRoleService;

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

        List<String> userIdList = ApiStrategyFactory.getStrategy().loadUserIdListByRoleIdList(roleIdList);


        return R.success(userIdList);
    }

    /**
     * 保存抄送
     *
     * @param copyDto
     * @return
     */
    @Override
    public R saveCC(ProcessInstanceCopyDto copyDto) {

        String processInstanceId = copyDto.getProcessInstanceId();


        //如果抄送是第一个节点 会出现查询不到的情况

        ThreadUtil.execute(() -> {
            try {
                ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();

                int index=10;
                while (index>0 && processInstanceRecord == null) {
                    TimeUnit.SECONDS.sleep(5);
                    processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
                    index--;
                }

                ProcessInstanceCopy processInstanceCopy = BeanUtil.copyProperties(copyDto, ProcessInstanceCopy.class);
                processInstanceCopy.setGroupId(Long.valueOf(processInstanceRecord.getGroupId()));
                processInstanceCopy.setGroupName(processInstanceRecord.getGroupName());
                processInstanceCopy.setProcessName(processInstanceRecord.getName());
                processInstanceCopy.setStartTime(processInstanceRecord.getCreateTime());



                processCopyService.save(processInstanceCopy);
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
        List<com.cxygzl.common.dto.third.DeptDto> childrenDeptList = DataUtil.selectChildrenByDept(parentId,allDept);


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

        List<String> userIdList = ApiStrategyFactory.getStrategy().loadUserIdListByDeptIdList(depIdList);
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

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        Map<String, Object> map = BeanUtil.beanToMap(user, "id", "name", "phone", "gender", "deptId", "entryDate"
                );
        List<UserFieldData> userFieldDataList = userFieldDataService.lambdaQuery().eq(UserFieldData::getUserId, userId).list();
        for (UserFieldData userFieldData : userFieldDataList) {
            map.put(userFieldData.getKey(), userFieldData.getData());
        }

        return R.success(map);
    }

    /**
     * 根据当前用户查询包括自己部门在内的上级部门对象
     *
     * @param userId
     * @return
     */
    @Override
    public R<List<DeptDto>> queryParentDepListByUserId(String userId) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);
        String deptId = user.getDeptId();

        List<com.cxygzl.common.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.cxygzl.common.dto.third.DeptDto> deptList = DataUtil.selectParentByDept(deptId,allDept);

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
     * 流程创建了
     *
     * @param processInstanceRecordParamDto
     * @return
     */
    @Override
    public R createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        ProcessInstanceRecord entity = BeanUtil.copyProperties(processInstanceRecordParamDto,
                ProcessInstanceRecord.class);


        Process oaForms = processService.getByFlowId(processInstanceRecordParamDto.getFlowId());

        ProcessGroup oaFormGroups = processGroupService.getById(oaForms.getGroupId());

        entity.setName(oaForms.getName());
        entity.setLogo(oaForms.getLogo());
        entity.setUserId(processInstanceRecordParamDto.getUserId());
        entity.setFlowId(processInstanceRecordParamDto.getFlowId());
        entity.setProcessInstanceId(processInstanceRecordParamDto.getProcessInstanceId());
        entity.setGroupId(oaFormGroups.getId());
        entity.setGroupName(oaFormGroups.getGroupName());
        entity.setStatus(NodeStatusEnum.JXZ.getCode());

        processInstanceRecordService.save(entity);

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
        return processNodeRecordService.complete(recordParamDto);
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
    public R taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return processNodeRecordAssignUserService.completeTaskEvent(processInstanceAssignUserRecordParamDto);
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
}
