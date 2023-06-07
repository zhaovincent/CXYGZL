package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.mapper.DeptMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.common.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private IProcessNodeRecordService processNodeRecordService;
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

                int index=10;
                while (index>0 && processInstanceRecord == null) {
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

        Long parentId = checkParentDto.getParentId();
        List<Long> deptIdList = checkParentDto.getDeptIdList();
        //查询子级包括自己
        List<Dept> childrenDeptList = deptMapper.selectChildrenByDept(parentId);
        List<Long> childrenDeptIdList = childrenDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());
        childrenDeptIdList.remove(parentId);

        List<Long> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(childrenDeptIdList, Long.class));

        return R.success(remainIdList.isEmpty());
    }

    /**
     * 根据部门id集合查询用户id集合
     *
     * @param depIdList
     * @return
     */
    @Override
    public R<List<Long>> queryUserIdListByDepIdList(List<Long> depIdList) {
        if (CollUtil.isEmpty(depIdList)) {
            return R.success(new ArrayList<>());
        }
        List<User> userList = userService.lambdaQuery().in(User::getDepId, depIdList).list();
        return R.success(userList.stream().map(w -> w.getId()).collect(Collectors.toList()));
    }

    /**
     * 检查是否是所有的子级
     *
     * @param checkChildDto
     * @return
     */
    @Override
    public R<Boolean> checkIsAllChild(CheckChildDto checkChildDto) {
        Long childId = checkChildDto.getChildId();
        List<Long> deptIdList = checkChildDto.getDeptIdList();
        //查询父级包括自己
        List<Dept> parentDeptList = deptMapper.selectParentByDept(childId);
        List<Long> parentDeptIdList = parentDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());
        parentDeptIdList.remove(childId);

        List<Long> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(parentDeptIdList, Long.class));

        return R.success(remainIdList.isEmpty());
    }

    /**
     * 获取用户的信息-包括扩展字段
     *
     * @param userId
     * @return
     */
    @Override
    public R<Map<String, Object>> queryUserAllInfo(long userId) {
        User user = userService.getById(userId);

        Map<String, Object> map = BeanUtil.beanToMap(user, "id", "name", "phone", "gender", "depId", "entryDate", "leaveDate");


        return R.success(map);
    }

    /**
     * 根据当前用户查询包括自己部门在内的上级部门对象
     *
     * @param userId
     * @return
     */
    @Override
    public R<List<DeptDto>> queryParentDepListByUserId(long userId) {
        User user = userService.getById(userId);
        Long depId = user.getDepId();

        List<Dept> deptList = deptMapper.selectParentByDept(depId);
        List<DeptDto> dtoList = BeanUtil.copyToList(deptList, DeptDto.class);

        return R.success(dtoList);
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
     * 流程创建了
     *
     * @param processInstanceRecordParamDto
     * @return
     */
    @Override
    public R createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        ProcessInstanceRecord entity = BeanUtil.copyProperties(processInstanceRecordParamDto,
                ProcessInstanceRecord.class);


        Process oaForms = processService.getByFormId(processInstanceRecordParamDto.getProcessId());

        ProcessGroup oaFormGroups = processGroupService.getById(oaForms.getGroupId());

        entity.setName(oaForms.getFormName());
        entity.setLogo(oaForms.getLogo());
        entity.setUserId(processInstanceRecordParamDto.getUserId());
        entity.setProcessId(processInstanceRecordParamDto.getProcessId());
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
    public R endNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
        return processNodeRecordService.complete(recordParamDto);
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
        return processNodeRecordAssignUserService.completeTaskEvent(processNodeRecordAssignUserParamDto);
    }

    /**
     * 实例结束
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R endProcess(String processInstanceId) {
        return processInstanceService.end(processInstanceId);
    }

    /**
     * 查询流程管理员
     *
     * @param processId
     * @return
     */
    @Override
    public R<Long> queryProcessAdmin(String processId) {
        Process process = processService.getByFormId(processId);
        return R.success(process.getAdminId());
    }
}
