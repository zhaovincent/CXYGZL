package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessGroup;
import com.cxygzl.biz.entity.ProcessStarter;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.vo.FormGroupVo;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : willian fu
 * @date : 2022/7/4
 */
@Slf4j
@Service
public class FormGroupServiceImpl implements FormGroupService {


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

    @Override
    public Object getFormGroups(Boolean hidden) {
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
                    .eq(hidden != null, Process::getIsHidden, hidden)
                    .orderByAsc(Process::getSort).orderByDesc(Process::getCreateTime).list();

            processList.forEach(process -> {



                formGroupVo.getItems().add(FormGroupVo.FlowVo.builder()
                        .flowId(process.getFlowId())
                                .rangeShow(process.getRangeShow())
                        .name(process.getName())
                        .logo(process.getLogo())
                        .remark(process.getRemark())
                        .isStop(process.getIsStop())
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
    public Object getStartFormGroups() {

        long userId = StpUtil.getLoginIdAsLong();

        User user = userService.getById(userId);

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
                    .eq(Process::getIsHidden, false)
                    .eq(Process::getIsStop, false)
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
                    boolean match = processStarters.stream().anyMatch(w -> w.getTypeId().longValue() == userId && w.getType().equals(NodeUserTypeEnum.USER.getKey()));
                    if (match) {
                        existMap.put(process.getId(), true);
                        continue;
                    }
                    Set<Long> deptIdSet = processStarters.stream().filter(w -> w.getType().equals(NodeUserTypeEnum.DEPT.getKey())).map(w -> w.getTypeId()).collect(Collectors.toSet());

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
                        .isStop(process.getIsStop())
                        .updated(process.getUpdateTime())
                        .build());
            });
        });
        return R.success(formGroupVos);
    }

    @Transactional
    @Override
    public Object formGroupsSort(List<Integer> groups) {
        int index = 1;
        for (Integer group : groups) {
            processGroupService.lambdaUpdate().set(ProcessGroup::getSort, index).eq(ProcessGroup::getId, group).update(new ProcessGroup());
            index++;
        }
        return R.success();
    }

    @Override
    public Object getFormById(String flowId) {
        ProcessVO processVO = getProcessVO(flowId);
        return R.success(processVO);
    }

    private ProcessVO getProcessVO(String flowId) {
        Process oaForms = processService.getByFlowId(flowId);
        String process = oaForms.getProcess();
        String formItems = oaForms.getFormItems();
        Node startNode = CommonUtil.toObj(process, Node.class);


        Map<String, String> formPerms = startNode.getFormPerms();
        List<FormItemVO> formItemVOList = JSON.parseArray(formItems, FormItemVO.class);

        for (FormItemVO formItemVO : formItemVOList) {
            String perm = MapUtil.getStr(formPerms, formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
            formItemVO.setPerm(perm);

            if(StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())){
                //明细
                Object value = formItemVO.getProps().getValue();
                List<FormItemVO> subList = Convert.toList(FormItemVO.class, value);
                for (FormItemVO itemVO : subList) {
                    String perm1 = MapUtil.getStr(formPerms, itemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
                    itemVO.setPerm(perm1);
                }


                formItemVO.getProps().setValue(subList);
                formItemVO.getProps();

            }

        }
        oaForms.setFormItems(CommonUtil.toJson(formItemVOList));


        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(startNode);

        ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
        processVO.setSelectUserNodeId(selectUserNodeId);

        return processVO;
    }

    @Override
    public Object updateFormGroupName(Long id, String name) {
        processGroupService.updateById(ProcessGroup.builder().id(id).groupName(name).build());
        return R.success("更新成功");
    }

    @Override
    public Object createFormGroup(ProcessGroup processGroup) {
        processGroupService.save(ProcessGroup.builder().sort(1).groupName(processGroup.getGroupName()).build());
        return R.success();
    }

    @Override
    public Object deleteFormGroup(long id) {
        processGroupService.removeById(id);
        return R.success();

    }


    @Override
    public Object formsSort(Long groupId, List<String> fromIds) {
        Process oaForms = Process.builder().groupId(groupId).build();
        for (int i = 0; i < fromIds.size(); i++) {
            oaForms.setFlowId(fromIds.get(i));
            oaForms.setSort(i);
            processService.updateByFlowId(oaForms);
        }
        return R.success("更新成功");
    }

    @Override
    public Object getFormGroupList() {

        List<ProcessGroup> processGroupList = processGroupService.lambdaQuery().orderByAsc(ProcessGroup::getSort).list();

        return R.success(processGroupList);
    }

    @Override
    public Object createFlow(Process process) {
        String processStr = process.getProcess();

        R<String> r = CoreHttpUtil.createFlow(JSON.parseObject(processStr), StpUtil.getLoginIdAsLong());
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }
        String flowId = r.getData();


        NodeUser nodeUser = CommonUtil.toArray(process.getAdmin(), NodeUser.class).get(0);

        if (StrUtil.isNotBlank(process.getFlowId())) {

            Process oldProcess = processService.getByFlowId(process.getFlowId());
            processService.hide(process.getFlowId());
            //修改所有的管理员
            processService.lambdaUpdate().set(Process::getAdminId, nodeUser.getId()).eq(Process::getUniqueId,
                    oldProcess.getUniqueId()).update(new Process());

        }

        Node startNode = CommonUtil.toObj(processStr, Node.class);


        List<NodeUser> nodeUserList = startNode.getNodeUserList();

        StringBuilder stringBuilder=new StringBuilder("");
        if(CollUtil.isNotEmpty(nodeUserList)){
            int index=0;

            for (NodeUser user : nodeUserList) {
                if(index>0){
                    stringBuilder.append(",");
                }
                stringBuilder.append(user.getName());
                index++;
                if(index>5){
                    break;
                }

            }
        }

        Process oaForms = Process.builder()
                .groupId(process.getGroupId())
                .flowId(flowId)
                .formItems(process.getFormItems())
                .name(process.getName())
                .admin(process.getAdmin())
                .logo(process.getLogo())
                .remark(process.getRemark())
                .process(processStr)
                .settings(process.getSettings())
                .sort(0)
                .adminId(Long.valueOf(nodeUser.getId()))
                .uniqueId(IdUtil.fastSimpleUUID())
                .isStop(false)
                .isHidden(false)
                .rangeShow(stringBuilder.toString())
                .build();
        processService.save(oaForms);

        //保存范围

        for (NodeUser nodeUserDto : nodeUserList) {
            ProcessStarter processStarter = new ProcessStarter();

            processStarter.setProcessId(oaForms.getId());
            processStarter.setTypeId(Long.valueOf(nodeUserDto.getId()));
            processStarter.setType(nodeUserDto.getType());
            processStarterService.save(processStarter);

        }


        return R.success();
    }

    @Override
    public Object updateForm(String flowId, String type, Long groupId) {
        Process process = Process.builder().flowId(flowId)
                .isStop("stop".equals(type))
                .isHidden("delete".equals(type))
                .groupId(groupId).build();

        processService.updateByFlowId(process);

        return R.success();
    }
}
