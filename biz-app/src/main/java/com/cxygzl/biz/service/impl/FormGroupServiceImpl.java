package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessGroup;
import com.cxygzl.biz.entity.ProcessStarter;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.R;
import com.cxygzl.biz.vo.FormGroupVo;
import com.cxygzl.biz.vo.ProcessSettingVo;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeFormMappingDto;
import com.cxygzl.common.dto.process.NodePropDto;
import com.cxygzl.common.dto.process.NodeUserDto;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
                    .orderByAsc(Process::getSort).list();

            processList.forEach(process -> {
                formGroupVo.getItems().add(FormGroupVo.Form.builder()
                        .formId(process.getFormId())
                        .formName(process.getFormName())
                        .logo(process.getLogo())
                        .remark(process.getRemark())
                        .isStop(process.getIsStop())
                        .updated(process.getUpdateTime())
                        .build());
            });
        });
        return R.ok(formGroupVos);
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

                    existMap.put(process.getId(), deptIdSet.contains(user.getDepId()));


                }

            }


            processList.forEach(process -> {

                if(!existMap.get(process.getId())){
                    return;
                }

                formGroupVo.getItems().add(FormGroupVo.Form.builder()
                        .formId(process.getFormId())
                        .formName(process.getFormName())
                        .logo(process.getLogo())
                        .remark(process.getRemark())
                        .isStop(process.getIsStop())
                        .updated(process.getUpdateTime())
                        .build());
            });
        });
        return R.ok(formGroupVos);
    }

    @Transactional
    @Override
    public Object formGroupsSort(List<Integer> groups) {
        int index=1;
        for (Integer group : groups) {
            processGroupService.lambdaUpdate().set(ProcessGroup::getSort,index).eq(ProcessGroup::getId,group).update(new ProcessGroup());
            index++;
        }
        return R.ok("排序成功");
    }

    @Override
    public Object getFormById(String formId) {
        ProcessVO processVO = getProcessVO(formId);
        return R.ok(processVO);
    }

    private ProcessVO getProcessVO(String formId) {
        Process oaForms = processService.getByFormId(formId);
        String process = oaForms.getProcess();
        NodeDto nodeDto = JSON.parseObject(process, NodeDto.class);

        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(nodeDto);

        ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
        processVO.setSelectUserNodeId(selectUserNodeId);

        return processVO;
    }



    @Override
    public Object updateFormGroupName(Long id, String name) {
        processGroupService.updateById(ProcessGroup.builder().id(id).groupName(name).build());
        return R.ok("更新成功");
    }

    @Override
    public Object createFormGroup(String name) {
        processGroupService.save(ProcessGroup.builder().sort(1).groupName(name).build());
        return R.ok("新增成功");
    }

    @Override
    public Object deleteFormGroup(Integer id) {
        processGroupService.removeById(id);
        return R.ok("删除成功");

    }

    @Override
    public Object updateForm(String formId, String type, Long groupId) {
        Process process = Process.builder().formId(formId)
                .isStop("stop".equals(type))
                .isHidden("delete".equals(type))
                .groupId(groupId).build();

        processService.updateByFormId(process);

        return R.ok("操作成功");
    }

    @Transactional
    @Override
    public Object updateFormDetail(Process forms) {

        String process = forms.getProcess();
        String settings = forms.getSettings();

        NodeDto nodeDto = JSON.parseObject(process, NodeDto.class);

        JSONObject processJSON = JSON.parseObject(process);
        JSONObject settingJSON = JSON.parseObject(settings);
        JSONObject props = processJSON.getJSONObject("props");
        props.putAll(settingJSON);

        String post = CoreHttpUtil.createProcess(processJSON,StpUtil.getLoginIdAsLong());
        com.cxygzl.common.dto.R<String> r = JSON.parseObject(post, com.cxygzl.common.dto.R.class);
        if (!r.isOk()) {
            return R.badRequest(r.getMsg());
        }


        String oldFormId = forms.getFormId();

        Process oldProcess = processService.getByFormId(oldFormId);

        ProcessSettingVo processSettingVo = JSON.parseObject(settings, ProcessSettingVo.class);
        List<NodeUserDto> adminList = processSettingVo.getAdmin();
        NodeUserDto nodeUserDtoAdmin = adminList.get(0);


        String processId = r.getData();

        forms.setFormId(processId);
        forms.setIsStop(false);
        forms.setFormId(processId);
        forms.setIsHidden(false);
        forms.setSort(oldProcess.getSort());
        forms.setAdminId(nodeUserDtoAdmin.getId());
        forms.setUniqueId(oldProcess.getUniqueId());
        {
            processService.hide(oldFormId);
            processService.save(forms);
        }

        //保存范围
        List<NodeUserDto> assignedUser = nodeDto.getProps().getAssignedUser();
        for (NodeUserDto nodeUserDto : assignedUser) {
            ProcessStarter processStarter = new ProcessStarter();

            processStarter.setProcessId(forms.getId());
            processStarter.setTypeId(nodeUserDto.getId());
            processStarter.setType(nodeUserDto.getType());
            processStarterService.save(processStarter);

        }

        //修改所有的管理员
        processService.lambdaUpdate().set(Process::getAdminId,nodeUserDtoAdmin.getId()).eq(Process::getUniqueId,oldProcess.getUniqueId()).update(new Process());


        return R.ok("操作成功");
    }

    @Override
    public Object formsSort(Long groupId, List<String> fromIds) {
        Process oaForms = Process.builder().groupId(groupId).build();
        for (int i = 0; i < fromIds.size(); i++) {
            oaForms.setFormId(fromIds.get(i));
            oaForms.setSort(i);
            processService.updateByFormId(oaForms);
        }
        return R.ok("更新成功");
    }

    @Override
    public Object getFormGroupList() {

        List<ProcessGroup> processGroupList = processGroupService.lambdaQuery().orderByAsc(ProcessGroup::getSort).list();

        return processGroupList;
    }

    @Override
    public Object createForm(Process form) {
        String process = form.getProcess();

        String post = CoreHttpUtil.createProcess(JSON.parseObject(process),StpUtil.getLoginIdAsLong());
        com.cxygzl.common.dto.R<String> r = JSON.parseObject(post, com.cxygzl.common.dto.R.class);
        if (!r.isOk()) {
            return R.badRequest(r.getMsg());
        }
        String processId = r.getData();


        ProcessSettingVo processSettingVo = JSON.parseObject(form.getSettings(), ProcessSettingVo.class);
        List<NodeUserDto> adminList = processSettingVo.getAdmin();
        NodeUserDto nodeUserDtoAdmin = adminList.get(0);



        Process oaForms = Process.builder()
                .groupId(form.getGroupId())
                .formId(processId)
                .formItems(form.getFormItems())
                .formName(form.getFormName())
                .logo(form.getLogo())
                .process(process)
                .settings(form.getSettings())
                .sort(0)
                .adminId(nodeUserDtoAdmin.getId())
                .uniqueId(IdUtil.fastSimpleUUID())
                .isStop(false)
                .isHidden(false)
                .build();
        processService.save(oaForms);

        return R.ok("创建表单成功");
    }
}
