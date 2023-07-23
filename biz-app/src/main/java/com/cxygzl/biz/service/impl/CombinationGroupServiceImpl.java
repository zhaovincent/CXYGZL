package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessGroup;
import com.cxygzl.biz.entity.ProcessStarter;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.vo.FormGroupVo;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CombinationGroupServiceImpl implements ICombinationGroupService {
    @Resource
    private IProcessGroupService processGroupService;

    @Resource
    private IUserService userService;

    @Resource
    private IProcessNodeDataService nodeDataService;

    @Resource
    private IProcessService processService;

    @Resource
    private IProcessStarterService processStarterService;
    /**
     * 查询表单组包含流程
     *
     * @param hidden
     * @return 表单组数据
     */
    @Override
    public R listGroupWithProcess(Boolean hidden) {

        List<FormGroupVo> formGroupVos = new LinkedList<>();

        List<ProcessGroup> processGroupList = processGroupService.lambdaQuery().orderByAsc(ProcessGroup::getSort).list();

        processGroupList.forEach(group -> {
            FormGroupVo formGroupVo = FormGroupVo.builder()
                    .id(group.getId())
                    .name(group.getGroupName())
                    .items(new LinkedList<>())
                    .build();
            formGroupVos.add(formGroupVo);

            List<Process> processList = processService.lambdaQuery()
                    .eq(Process::getGroupId, group.getId())
                    .eq(hidden != null, Process::getHidden, hidden)
                    .orderByAsc(Process::getSort).orderByDesc(Process::getCreateTime).list();

            processList.forEach(process -> {



                formGroupVo.getItems().add(FormGroupVo.FlowVo.builder()
                        .flowId(process.getFlowId())
                        .rangeShow(process.getRangeShow())
                        .name(process.getName())
                        .logo(process.getLogo())
                        .remark(process.getRemark())
                        .stop(process.getStop())
                        .updated(process.getUpdateTime())
                        .build());
            });
        });
        return R.success(formGroupVos);
    }

    /**
     * 查询所有我可以发起的表单组
     *
     * @return
     */
    @Override
    public R listCurrentUserStartGroup() {


        String userId = StpUtil.getLoginIdAsString();

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        List<FormGroupVo> formGroupVos = new LinkedList<>();

        List<ProcessGroup> processGroupList = processGroupService.lambdaQuery().orderByAsc(ProcessGroup::getSort).list();

        processGroupList.forEach(group -> {
            FormGroupVo formGroupVo = FormGroupVo.builder()
                    .id(group.getId())
                    .name(group.getGroupName())
                    .items(new LinkedList<>())
                    .build();
            formGroupVos.add(formGroupVo);

            List<Process> processList = processService.lambdaQuery()
                    .eq(Process::getGroupId, group.getId())
                    .eq(Process::getHidden, false)
                    .eq(Process::getStop, false)
                    .orderByAsc(Process::getSort).list();

            Map<Long, Boolean> existMap = new HashMap<>();

            if (!processList.isEmpty()) {
                List<Long> idList = processList.stream().map(w -> w.getId()).collect(Collectors.toList());
                //查询发起人集合
                List<ProcessStarter> processStarterList = processStarterService.lambdaQuery().in(ProcessStarter::getProcessId, idList).list();
                Map<Long, List<ProcessStarter>> groupmap = processStarterList.stream().collect(Collectors.groupingBy(ProcessStarter::getProcessId));

                for (Process process : processList) {
                    List<ProcessStarter> processStarters = groupmap.get(process.getId());
                    if (processStarters == null) {
                        existMap.put(process.getId(), true);
                        continue;
                    }
                    boolean match =
                            processStarters.stream().anyMatch(w -> w.getTypeId().equals(userId) && w.getType().equals(NodeUserTypeEnum.USER.getKey()));
                    if (match) {
                        existMap.put(process.getId(), true);
                        continue;
                    }
                    Set<String> deptIdSet =
                            processStarters.stream().filter(w -> w.getType().equals(NodeUserTypeEnum.DEPT.getKey())).map(w -> w.getTypeId()).collect(Collectors.toSet());

                    existMap.put(process.getId(), deptIdSet.contains(user.getDeptId()));


                }

            }


            processList.forEach(process -> {

                if (!existMap.get(process.getId())) {
                    return;
                }

                formGroupVo.getItems().add(FormGroupVo.FlowVo.builder()
                        .flowId(process.getFlowId())
                        .name(process.getName())
                        .logo(process.getLogo())
                        .remark(process.getRemark())
                        .stop(process.getStop())
                        .updated(process.getUpdateTime())
                        .build());
            });
        });
        return R.success(formGroupVos);
    }
}
