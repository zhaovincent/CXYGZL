package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.NodeFormatUtil;
import com.cxygzl.biz.utils.R;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.ProcessCopyVo;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeFormPermDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实例进程服务
 */
@Service
@Slf4j
public class ProcessInstanceServiceImpl implements IProcessInstanceService {
    @Resource
    private IUserService userService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessCopyService processCopyService;

    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeRecordService processNodeRecordService;
    @Resource
    private IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;


    /**
     * 启动流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public Object startProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

        long userId = StpUtil.getLoginIdAsLong();

        User user = userService.getById(userId);

        processInstanceParamDto.setStartUserId(String.valueOf(userId));
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        Dict rootUser = Dict.create().set("id", userId).set("name", user.getName()).set("type", NodeUserTypeEnum.USER.getKey());
        paramMap.put("root", CollUtil.newArrayList(rootUser));

        String post = CoreHttpUtil.startProcess(processInstanceParamDto);
        com.cxygzl.common.dto.R<String> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<String>>() {
        });
        if (!r.isOk()) {
            return R.badRequest(r.getMsg());
        }
        String data = r.getData();


        return R.ok(data);
    }

    /**
     * 查询当前登录用户的待办任务
     *
     * @param pageVO
     * @return
     */
    @Override
    public Object queryMineTask(PageDto pageVO) {

        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(pageVO, TaskQueryParamDto.class);
        taskQueryParamDto.setAssign(StpUtil.getLoginIdAsString());

        String post = CoreHttpUtil.queryAssignTask(taskQueryParamDto);

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
        });
        PageResultDto<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return R.ok(pageResultDto);

        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<Long> startUserIdSet = processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<User> startUserList = userService.listByIds(startUserIdSet);

        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());

                User startUser = startUserList.stream().filter(w -> w.getId()
                        .longValue() == processInstanceRecord.getUserId()).findAny().orElse(null);

                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
            }
        }


        return R.ok(pageResultDto);
    }

    /**
     * 查询已办任务
     *
     * @param pageVO
     * @return
     */
    @Override
    public Object queryMineEndTask(PageDto pageVO) {
        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(pageVO, TaskQueryParamDto.class);
        taskQueryParamDto.setAssign(StpUtil.getLoginIdAsString());

        String post = CoreHttpUtil.queryCompletedTask(taskQueryParamDto);

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
        });
        PageResultDto<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return R.ok(pageResultDto);

        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<Long> startUserIdSet = processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<User> startUserList = userService.listByIds(startUserIdSet);

        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());

                User startUser = startUserList.stream().filter(w -> w.getId()
                        .longValue() == processInstanceRecord.getUserId()).findAny().orElse(null);

                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
            }
        }


        return R.ok(pageResultDto);
    }

    /**
     * 流程结束
     *
     * @param processsInstanceId
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R end(String processsInstanceId) {
        processInstanceRecordService.lambdaUpdate()
                .set(ProcessInstanceRecord::getEndTime, new Date())
                .set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .eq(ProcessInstanceRecord::getProcessInstanceId, processsInstanceId)
                .update(new ProcessInstanceRecord());
        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 查询我发起的
     *
     * @param pageDto
     * @return
     */
    @Override
    public Object queryMineStarted(PageDto pageDto) {

        long userId = StpUtil.getLoginIdAsLong();

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getUserId, userId)
                .orderByDesc(ProcessInstanceRecord::getUpdateTime)
                .page(new Page<>(pageDto.getPage(), pageDto.getCount()));

        return R.ok(instanceRecordPage);
    }

    /**
     * 查询抄送给我的
     *
     * @param pageDto
     * @return
     */
    @Override
    public Object queryMineCC(PageDto pageDto) {

        long userId = StpUtil.getLoginIdAsLong();

        Page<ProcessCopy> page = processCopyService.lambdaQuery()
                .eq(ProcessCopy::getUserId, userId)
                .orderByDesc(ProcessCopy::getNodeTime)
                .page(new Page<>(pageDto.getPage(), pageDto.getCount()));

        List<ProcessCopy> records = page.getRecords();

        List<ProcessCopyVo> processCopyVoList = BeanUtil.copyToList(records, ProcessCopyVo.class);

        if (CollUtil.isNotEmpty(records)) {

            //发起人
            Set<Long> startUserIdSet = records.stream().map(w -> w.getStartUserId()).collect(Collectors.toSet());

            List<User> startUserList = userService.listByIds(startUserIdSet);


            for (ProcessCopyVo record : processCopyVoList) {


                User startUser = startUserList.stream().filter(w -> w.getId()
                        .longValue() == record.getStartUserId()).findAny().orElse(null);
                record.setStartUserName(startUser.getName());
            }
        }

        Page p = BeanUtil.copyProperties(page, Page.class);

        p.setRecords(processCopyVoList);

        return R.ok(p);
    }

    /**
     * 显示流程实例图片
     *
     * @param procInsId
     * @return
     */
    @Override
    public Object showImg(String procInsId) {
        String s = CoreHttpUtil.showImg(procInsId);
        com.cxygzl.common.dto.R<String> stringR = JSON.parseObject(s, new TypeReference<com.cxygzl.common.dto.R<String>>() {
        });
        String data = stringR.getData();
//
//        OutputStream out = response.getOutputStream();
//
//        Base64.decodeToStream(data, out, true);
        return com.cxygzl.biz.utils.R.ok(data);
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    @Override
    public Object formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo) {
        String formId = nodeFormatParamVo.getFormId();
        String processInstanceId = nodeFormatParamVo.getProcessInstanceId();
        if (StrUtil.isAllBlank(formId, processInstanceId)) {
            return R.ok(new ArrayList<>());
        }

        if (StrUtil.isBlankIfStr(formId) && StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            formId = processInstanceRecord.getProcessId();


        }
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            String s = CoreHttpUtil.queryTaskVariables(nodeFormatParamVo.getTaskId(), null);
            com.cxygzl.common.dto.R<Map<String, Object>> r = JSON.parseObject(s,
                    new TypeReference<com.cxygzl.common.dto.R<Map<String, Object>>>() {
                    });
            if (!r.isOk()) {

                ProcessNodeRecordAssignUser processNodeRecordAssignUser = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessNodeRecordAssignUser::getTaskId, nodeFormatParamVo.getTaskId())
                        .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YJS.getCode())
                        .last("limit 1")
                        .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                        .one();

                String data = processNodeRecordAssignUser.getData();
                Map<String, Object> variableMap = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                });
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            }else{
                Map<String, Object> variableMap = r.getData();
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            }

        }


        Process oaForms = processService.getByFormId(formId);
        String process = oaForms.getProcess();
        NodeDto nodeDto = JSON.parseObject(process, NodeDto.class);

        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto, new HashSet<>(),
                new HashSet<>(), processInstanceId, paramMap);

        return R.ok(processNodeShowDtos);
    }

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public Object detail(String processInstanceId) {


        long userId = StpUtil.getLoginIdAsLong();


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        Process oaForms = processService.getByFormId(processInstanceRecord.getProcessId());
        if (oaForms == null) {
            return com.cxygzl.biz.utils.R.badRequest("流程不存在");
        }


        //发起人变量数据
        String formData = processInstanceRecord.getFormData();
        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });
        //发起人表单权限
        String process = oaForms.getProcess();
        NodeDto nodeDto = JSON.parseObject(process, NodeDto.class);
        List<NodeFormPermDto> formPerms = nodeDto.getProps().getFormPerms();


        List<JSONObject> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), JSONObject.class);
        for (JSONObject jsonObject : jsonObjectList) {
            String id = jsonObject.getString("id");

            NodeFormPermDto nodeFormPermDto = formPerms.stream().filter(w -> StrUtil.equals(w.getId(), id)).findAny().orElse(null);

            JSONObject props = jsonObject.getJSONObject("props");
            String perm =nodeFormPermDto==null?"R": nodeFormPermDto.getPerm();
            props.put("perm", nodeFormPermDto==null?"R": (StrUtil.equals("E",perm)?"R":perm));
            jsonObject.put("value", variableMap.get(id));
        }
        Dict set = Dict.create()
                .set("processInstanceId", processInstanceId)
                .set("process", oaForms.getProcess())


                .set("formItems", jsonObjectList);

        return com.cxygzl.biz.utils.R.ok(set);
    }
}
