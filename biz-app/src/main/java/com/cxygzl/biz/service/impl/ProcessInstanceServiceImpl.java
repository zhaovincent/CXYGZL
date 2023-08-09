package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.FormUtil;
import com.cxygzl.biz.utils.NodeFormatUtil;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.ProcessCopyVo;
import com.cxygzl.biz.vo.ProcessInstanceRecordVO;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.*;
import com.cxygzl.common.dto.third.UserDto;
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
    private IProcessNodeDataService processNodeDataService;

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

        String userId = StpUtil.getLoginIdAsString();


        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);


        processInstanceParamDto.setStartUserId(String.valueOf(userId));
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        Dict rootUser = Dict.create().set("id", userId).set("name", user.getName()).set("type", NodeUserTypeEnum.USER.getKey());
        paramMap.put(ProcessInstanceConstant.VariableKey.STARTER, CollUtil.newArrayList(rootUser));

        String post = CoreHttpUtil.startProcess(processInstanceParamDto);
        com.cxygzl.common.dto.R<String> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<String>>() {
        });
        if (!r.isOk()) {
            return com.cxygzl.common.dto.R.fail(r.getMsg());
        }
        String data = r.getData();


        return com.cxygzl.common.dto.R.success(data);
    }

    /**
     * 查询当前登录用户的待办任务
     *
     * @param pageVO
     * @return
     */
    @Override
    public R queryMineTask(PageDto pageVO) {

        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(pageVO, TaskQueryParamDto.class);
        taskQueryParamDto.setAssign(StpUtil.getLoginIdAsString());

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryAssignTask(taskQueryParamDto);


        PageResultDto<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return com.cxygzl.common.dto.R.success(pageResultDto);

        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<String> startUserIdSet =
                processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<UserDto> startUserList = new ArrayList<>();
        {
            for (String userIds : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(userIds);
                startUserList.add(user);
            }
        }
        //流程配置
        Set<String> flowIdSet = processInstanceRecordList.stream().map(w -> w.getFlowId()).collect(Collectors.toSet());
        List<Process> processList = processService.lambdaQuery().in(Process::getFlowId, flowIdSet).list();


        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());

                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(processInstanceRecord.getUserId())).findAny().orElse(null);

                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());


            }
            //是否是子流程发起任务
            Map<String, Object> paramMap = record.getParamMap();
            {
                Object subProcessStarterNode =
                        paramMap.get(ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE);
                Object rejectStarterNode = paramMap.get(ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE);
                record.setSubProcessStarterTask(Convert.toBool(subProcessStarterNode, false) && rejectStarterNode == null);


            }
            //处理表单数据
            Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findAny().orElse(null);
            if (process != null) {
                List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), record.getNodeId(), paramMap);

                record.setFormValueShowList(formValueShowList);
            }


        }


        return com.cxygzl.common.dto.R.success(pageResultDto);
    }

    private static void buildFormValueShow(Map<String, Object> paramMap, List<FormItemVO> formItemVOList,
                                           Map<String, String> formPermMap, List<Dict> formValueShowList) {
        for (FormItemVO formItemVO : formItemVOList) {
//            if (formValueMap.size() >= 3) {
//                break;
//            }
            String id = formItemVO.getId();
            String formItemVOName = formItemVO.getName();

            String perm = formPermMap.get(id);
            if (StrUtil.isBlank(perm) || ProcessInstanceConstant.FormPermClass.HIDE.equals(perm)) {
                continue;
            }
            Object o = paramMap.get(id);
            if (o == null || StrUtil.isBlankIfStr(o)) {
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", ""));
                continue;
            }
            //表单类型
            String type = formItemVO.getType();
            if (StrUtil.equals(type, FormTypeEnum.AREA.getType())) {
                AreaFormValue areaFormValue = BeanUtil.copyProperties(o, AreaFormValue.class);
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", areaFormValue.getName()));

            } else if (StrUtil.equalsAny(type, FormTypeEnum.SINGLE_SELECT.getType(), FormTypeEnum.MULTI_SELECT.getType())) {
                List<SelectValue> selectValueList = BeanUtil.copyToList(Convert.toList(o), SelectValue.class);
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", selectValueList.stream().map(w -> w.getValue()).collect(Collectors.joining(","))));

            } else if (StrUtil.equalsAny(type,
                    FormTypeEnum.SELECT_USER.getType(),
                    FormTypeEnum.SELECT_MULTI_USER.getType(),
                    FormTypeEnum.SELECT_MULTI_DEPT.getType(),
                    FormTypeEnum.SELECT_DEPT.getType()
            )) {
                List<NodeUser> nodeUserList = BeanUtil.copyToList(Convert.toList(o), NodeUser.class);
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", nodeUserList.stream().map(w -> w.getName()).collect(Collectors.joining(","))));

            } else if (StrUtil.equalsAny(type,
                    FormTypeEnum.UPLOAD_FILE.getType(),
                    FormTypeEnum.UPLOAD_IMAGE.getType()
            )) {
                List<UploadValue> uploadValueList = BeanUtil.copyToList(Convert.toList(o), UploadValue.class);
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", uploadValueList.stream().map(w -> w.getName()).collect(Collectors.joining(","))));

            } else if (StrUtil.equalsAny(type,
                    FormTypeEnum.LAYOUT.getType()
            )) {
                //明细列表
                Object formItemListSub = formItemVO.getProps().getValue();

                List<Object> valueList = Convert.toList(Object.class, o);
                for (Object o1 : valueList) {
                    buildFormValueShow(Convert.toMap(String.class, Object.class, o1), Convert.toList(FormItemVO.class, formItemListSub), formPermMap, formValueShowList);

                }

            } else {
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", Convert.toStr(o)));


            }

        }
    }

    /**
     * 查询已办任务
     *
     * @param pageVO
     * @return
     */
    @Override
    public R queryMineEndTask(PageDto pageVO) {
        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(pageVO, TaskQueryParamDto.class);
        taskQueryParamDto.setAssign(StpUtil.getLoginIdAsString());

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryCompletedTask(taskQueryParamDto);


        PageResultDto<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return com.cxygzl.common.dto.R.success(pageResultDto);

        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<String> startUserIdSet =
                processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<UserDto> startUserList = new ArrayList<>();
        {
            for (String userIds : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(userIds);
                startUserList.add(user);
            }
        }
        //流程配置
        Set<String> flowIdSet = processInstanceRecordList.stream().map(w -> w.getFlowId()).collect(Collectors.toSet());
        List<Process> processList = processService.lambdaQuery().in(Process::getFlowId, flowIdSet).list();

        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(processInstanceRecord.getUserId())).findAny().orElse(null);
                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
            }
            Map<String, Object> paramMap = new LinkedHashMap<>();
            {

                ProcessNodeRecordAssignUser processNodeRecordAssignUser = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessNodeRecordAssignUser::getTaskId, record.getTaskId())
                        .eq(ProcessNodeRecordAssignUser::getUserId, StpUtil.getLoginIdAsString())
                        .eq(ProcessNodeRecordAssignUser::getExecutionId, record.getExecutionId())
                        .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YJS.getCode())
                        .last("limit 1")
                        .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                        .one();

                if (processNodeRecordAssignUser != null) {
                    String data = processNodeRecordAssignUser.getData();
                    if (StrUtil.isNotBlank(data)) {
                        Map<String, Object> collect = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                        });
                        paramMap.putAll(collect);

                    }
                }

            }

            //处理表单数据
            Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findFirst().get();
            List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), record.getNodeId(), paramMap);

            record.setFormValueShowList(formValueShowList);
        }


        return R.success(pageResultDto);
    }

    /**
     * 流程结束
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R end(ProcessInstanceParamDto processInstanceParamDto) {
        processInstanceRecordService.lambdaUpdate()
                .set(ProcessInstanceRecord::getEndTime, new Date())
                .set(!processInstanceParamDto.getCancel(), ProcessInstanceRecord::getStatus,
                        NodeStatusEnum.YJS.getCode())
                .set(processInstanceParamDto.getCancel(), ProcessInstanceRecord::getStatus, NodeStatusEnum.YCX.getCode())
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceParamDto.getProcessInstanceId())
                .eq(ProcessInstanceRecord::getStatus,NodeStatusEnum.JXZ.getCode())
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
    public R queryMineStarted(PageDto pageDto) {

        String userId = StpUtil.getLoginIdAsString();

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getUserId, userId)
                .orderByDesc(ProcessInstanceRecord::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        List<ProcessInstanceRecord> records = instanceRecordPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return com.cxygzl.common.dto.R.success(instanceRecordPage);
        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();


        //流程配置
        Set<String> flowIdSet = processInstanceRecordList.stream().map(w -> w.getFlowId()).collect(Collectors.toSet());
        List<Process> processList = processService.lambdaQuery().in(Process::getFlowId, flowIdSet).list();

        List<ProcessInstanceRecordVO> processInstanceRecordVOList = BeanUtil.copyToList(records, ProcessInstanceRecordVO.class);

        for (ProcessInstanceRecordVO record : processInstanceRecordVOList) {
            String formData = record.getFormData();


            //处理表单数据
            Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findFirst().get();
            List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), ProcessInstanceConstant.VariableKey.STARTER, JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
            }));

            record.setFormValueShowList(formValueShowList);
            record.setFormData(null);
            record.setProcess(null);
        }
        Page page = BeanUtil.copyProperties(instanceRecordPage, Page.class);
        page.setRecords(processInstanceRecordVOList);


        return com.cxygzl.common.dto.R.success(page);
    }

    /**
     * 查询抄送给我的
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryMineCC(PageDto pageDto) {

        String userId = StpUtil.getLoginIdAsString();

        Page<ProcessCopy> page = processCopyService.lambdaQuery()
                .eq(ProcessCopy::getUserId, userId)
                .orderByDesc(ProcessCopy::getNodeTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        List<ProcessCopy> records = page.getRecords();

        List<ProcessCopyVo> processCopyVoList = BeanUtil.copyToList(records, ProcessCopyVo.class);

        if (CollUtil.isNotEmpty(records)) {

            //发起人
            Set<String> startUserIdSet = records.stream().map(w -> w.getStartUserId()).collect(Collectors.toSet());

            List<UserDto> startUserList = new ArrayList<>();
            for (String s : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(s);
                startUserList.add(user);
            }
            Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

            //流程实例记录
            List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceIdSet).list();

            //流程配置
            Set<String> flowIdSet = processInstanceRecordList.stream().map(w -> w.getFlowId()).collect(Collectors.toSet());
            List<Process> processList = processService.lambdaQuery().in(Process::getFlowId, flowIdSet).list();


            for (ProcessCopyVo record : processCopyVoList) {


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(record.getStartUserId())).findAny().orElse(null);
                record.setStartUserName(startUser.getName());


                Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findFirst().get();

                List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), ProcessInstanceConstant.VariableKey.STARTER, JSON.parseObject(record.getFormData(), new TypeReference<Map<String, Object>>() {
                }));

                record.setFormValueShowList(formValueShowList);
                record.setFormData(null);

            }
        }

        Page p = BeanUtil.copyProperties(page, Page.class);

        p.setRecords(processCopyVoList);

        return com.cxygzl.common.dto.R.success(p);
    }

    /**
     * 获取列表 显示的表单数据 姓名：张三 格式
     *
     * @param process
     * @param flowId
     * @param nodeId
     * @param paramMap
     * @return
     */
    private List<Dict> getFormValueShowList(Process process, String flowId, String nodeId, Map<String, Object> paramMap) {
        String formItems = process.getFormItems();
        List<FormItemVO> formItemVOList = JSON.parseArray(formItems, FormItemVO.class);
        String data = processNodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = JSON.parseObject(data, Node.class);
        Map<String, String> map = node.getFormPerms();

        List<Dict> formValueShowList = new ArrayList<>();


        buildFormValueShow(paramMap, formItemVOList, map, formValueShowList);

        List<Dict> list = formValueShowList.size() > 3 ? (formValueShowList.subList(0, 3)) : formValueShowList;
        return list;
    }

    /**
     * 显示流程实例图片
     *
     * @param procInsId
     * @return
     */
    @Override
    public R showImg(String procInsId) {
        String s = CoreHttpUtil.showImg(procInsId);
        com.cxygzl.common.dto.R<String> stringR = JSON.parseObject(s, new TypeReference<com.cxygzl.common.dto.R<String>>() {
        });
        String data = stringR.getData();
//
//        OutputStream out = response.getOutputStream();
//
//        Base64.decodeToStream(data, out, true);
        return com.cxygzl.common.dto.R.success(data);
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    @Override
    public R formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo) {
        String flowId = nodeFormatParamVo.getFlowId();
        String processInstanceId = nodeFormatParamVo.getProcessInstanceId();
        if (StrUtil.isAllBlank(flowId, processInstanceId)) {
            return com.cxygzl.common.dto.R.success(new ArrayList<>());
        }

        String process = null;
        if ( StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            flowId = processInstanceRecord.getFlowId();
            process = processInstanceRecord.getProcess();

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
                        .last("limit 1")
                        .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                        .one();

                String data = processNodeRecordAssignUser.getData();
                Map<String, Object> variableMap = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                });
                if (variableMap == null) {
                    variableMap = new HashMap<>();
                }
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            } else {
                Map<String, Object> variableMap = r.getData();
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            }

        }

        //完成的executionId
        Set<String> endUniqueId = new HashSet<>();
        //进行中的
        Set<String> beingUniqueId = new HashSet<>();
        Set<String> cancelUniqueId = new HashSet<>();

        if (StrUtil.isNotBlank(processInstanceId)) {
            List<ProcessNodeRecord> processNodeRecordList = processNodeRecordService.lambdaQuery()
                    .eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId)
                    .list();
            {
                Set<String> collect = processNodeRecordList.stream().filter(w -> w.getStatus().intValue() == NodeStatusEnum.YJS.getCode())
                        .map(w -> {
                            String nodeId = w.getNodeId();
                            if(StrUtil.startWith(nodeId,ProcessInstanceConstant.VariableKey.STARTER)){
                                nodeId=ProcessInstanceConstant.VariableKey.STARTER;
                            }
                            return StrUtil.format("{}@@{}@@{}", nodeId,w.getExecutionId(),w.getFlowUniqueId());
                        }).collect(Collectors.toSet());
                endUniqueId.addAll(collect);
            }
            {
                Set<String> collect = processNodeRecordList.stream().filter(w -> w.getStatus().intValue() == NodeStatusEnum.JXZ.getCode())
                        .map(w -> {
                            String nodeId = w.getNodeId();
                            if(StrUtil.startWith(nodeId,ProcessInstanceConstant.VariableKey.STARTER)){
                                nodeId=ProcessInstanceConstant.VariableKey.STARTER;
                            }
                            return StrUtil.format("{}@@{}@@{}", nodeId,w.getExecutionId(),w.getFlowUniqueId());
                        }).collect(Collectors.toSet());
                beingUniqueId.addAll(collect);
            }
            {
                Set<String> collect = processNodeRecordList.stream().filter(w -> w.getStatus().intValue() == NodeStatusEnum.YCX.getCode())
                        .map(w -> {
                            String nodeId = w.getNodeId();
                            if(StrUtil.startWith(nodeId,ProcessInstanceConstant.VariableKey.STARTER)){
                                nodeId=ProcessInstanceConstant.VariableKey.STARTER;
                            }
                            return StrUtil.format("{}@@{}@@{}", nodeId,w.getExecutionId(),w.getFlowUniqueId());
                        }).collect(Collectors.toSet());
                cancelUniqueId.addAll(collect);
            }
        }


        if (StrUtil.isBlank(process)) {
            Process oaForms = processService.getByFlowId(flowId);
            process = oaForms.getProcess();
        }
        Node nodeDto = JSON.parseObject(process, Node.class);

        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto, endUniqueId,
                beingUniqueId, cancelUniqueId, processInstanceId, paramMap);

        return com.cxygzl.common.dto.R.success(processNodeShowDtos);
    }

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R detail(String processInstanceId) {


        String userId = StpUtil.getLoginIdAsString();


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());
        if (oaForms == null) {
            return com.cxygzl.common.dto.R.fail("流程不存在");
        }


        //发起人变量数据
        String formData = processInstanceRecord.getFormData();
        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });
        //发起人表单权限
        String process = oaForms.getProcess();
        Node nodeDto = JSON.parseObject(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();


        List<FormItemVO> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : jsonObjectList) {
            String id = formItemVO.getId();
            String perm = formPerms1.get(id);

            formItemVO.setPerm(StrUtil.isBlankIfStr(perm) ? ProcessInstanceConstant.FormPermClass.READ :
                    (StrUtil.equals(perm, ProcessInstanceConstant.FormPermClass.HIDE) ?
                            perm : ProcessInstanceConstant.FormPermClass.READ
                    )
            );

            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, id, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        Object value1 = map.get(itemVO.getId());
                        FormUtil.handValue(itemVO, value1);


                        String permSub = formPerms1.get(itemVO.getId());

                        itemVO.setPerm(StrUtil.isBlankIfStr(permSub) ? ProcessInstanceConstant.FormPermClass.READ :
                                (StrUtil.equals(permSub, ProcessInstanceConstant.FormPermClass.HIDE) ?
                                        permSub : ProcessInstanceConstant.FormPermClass.READ
                                ));


                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);


            } else {
                Object value = variableMap.get(id);
                FormUtil.handValue(formItemVO, value);

            }


        }
        Dict set = Dict.create()
                .set("processInstanceId", processInstanceId)
                .set("process", oaForms.getProcess())


                .set("formItems", jsonObjectList);

        return com.cxygzl.common.dto.R.success(set);
    }
}
