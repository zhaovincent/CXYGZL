package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.form.FormStrategyFactory;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.*;
import com.cxygzl.biz.vo.*;
import com.cxygzl.biz.vo.node.NodeImageVO;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.common.constants.*;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private IFileService fileService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessInstanceCopyService processCopyService;

    @Resource
    private IProcessInstanceOperRecordService processInstanceOperRecordService;

    @Resource
    private IProcessNodeDataService processNodeDataService;

    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AnylineService anylineService;
    @Resource
    @Lazy
    private IRemoteService remoteService;

    /**
     * 消息通知事件
     *
     * @param messageDto
     * @return
     */
    @Override
    public R notifyMsgEvent(NotifyMessageDto messageDto) {
        return CoreHttpUtil.notifyMsgEvent(messageDto);
    }

    /**
     * 启动流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public R startProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

        String userId = StpUtil.getLoginIdAsString();


        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);


        processInstanceParamDto.setStartUserId(String.valueOf(userId));
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        NodeUser rootUser = NodeUser.builder().id(userId).name(user.getName()).type(NodeUserTypeEnum.USER.getKey()).build();
        paramMap.put(ProcessInstanceConstant.VariableKey.STARTER, CollUtil.newArrayList(rootUser));

        //业务key
        Process process = processService.getByFlowId(processInstanceParamDto.getFlowId());
        processInstanceParamDto.setBizKey(process.getUniqueId());


        com.cxygzl.common.dto.R<String> r = CoreHttpUtil.startProcess(processInstanceParamDto);

        if (!r.isOk()) {
            return com.cxygzl.common.dto.R.fail(r.getMsg());
        }
        String data = r.getData();


        processInstanceOperRecordService.saveStartProcessRecord(userId, data, processInstanceParamDto.getFlowId());


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

        com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryTodoTask(taskQueryParamDto);


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
                record.setProcessInstanceBizCode(processInstanceRecord.getProcessInstanceBizCode());



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

            if (StrUtil.equalsAny(type,
                    FormTypeEnum.LAYOUT.getType()
            )) {
                Object formItemListSub = formItemVO.getProps().getValue();

                List<Object> valueList = Convert.toList(Object.class, o);
                for (Object o1 : valueList) {
                    buildFormValueShow(Convert.toMap(String.class, Object.class, o1), Convert.toList(FormItemVO.class, formItemListSub), formPermMap, formValueShowList);

                }
            }else{
                if(o==null||StrUtil.isBlankIfStr(o)){
                    formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", ""));
                    return;

                }
                String label = FormStrategyFactory.getStrategy(type).getProcessInstanceExcelShow(JsonUtil.toJSONString(o));
                formValueShowList.add(Dict.create().set("key", formItemVOName).set("label", label));

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
    public R queryMineEndTask(ProcessDataQueryVO pageVO) {
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
                record.setProcessInstanceResult(processInstanceRecord.getResult());
                record.setProcessInstanceBizCode(processInstanceRecord.getProcessInstanceBizCode());
            }
            Map<String, Object> paramMap = new LinkedHashMap<>();
            {

                List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessInstanceAssignUserRecord::getTaskId, record.getTaskId())
                        .eq(ProcessInstanceAssignUserRecord::getUserId, StpUtil.getLoginIdAsString())
                        .eq(ProcessInstanceAssignUserRecord::getExecutionId, record.getExecutionId())
                        .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.YJS.getCode())

                        .orderByDesc(ProcessInstanceAssignUserRecord::getEndTime)
                        .list();

                if (CollUtil.isNotEmpty(list)) {
                    String data = list.get(0).getData();
                    if (StrUtil.isNotBlank(data)) {
                        Map<String, Object> collect = JsonUtil.parseObject(data, new JsonUtil.TypeReference<Map<String, Object>>() {
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
     * 查询已办任务的流程实例
     *
     * @param pageVO
     * @return
     */
    @Override
    public R queryMineDoneProcessInstance(ProcessDataQueryVO pageVO) {

        List<String> flowIdList = pageVO.getFlowIdList();
        if (CollUtil.isNotEmpty(flowIdList)) {
            flowIdList = processService.getAllRelatedFlowId(flowIdList).getData();
        }

        ProcessQueryParamDto processQueryParamDto = BeanUtil.copyProperties(pageVO, ProcessQueryParamDto.class);
        processQueryParamDto.setAssign(StpUtil.getLoginIdAsString());
        processQueryParamDto.setFlowIdList(flowIdList);
        R<PageResultDto<ProcessInstanceDto>> r = CoreHttpUtil.queryCompletedProcessInstance(processQueryParamDto);

        PageResultDto<ProcessInstanceDto> pageResultDto = r.getData();
        List<ProcessInstanceDto> records = pageResultDto.getRecords();
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


        for (ProcessInstanceDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(processInstanceRecord.getUserId())).findAny().orElse(null);
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setStartUserName(startUser.getName());
                record.setProcessInstanceResult(processInstanceRecord.getResult());
                record.setProcessInstanceStatus(processInstanceRecord.getStatus());
            }

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
                .set(processInstanceParamDto.getCancel(), ProcessInstanceRecord::getResult, ApproveResultEnum.CANCEL.getValue())
                .set(!processInstanceParamDto.getCancel(), ProcessInstanceRecord::getResult, processInstanceParamDto.getResult())
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceParamDto.getProcessInstanceId())
                .eq(ProcessInstanceRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .update(new ProcessInstanceRecord());

        //通知第三方
        if (processInstanceParamDto.getCancel()) {
            ApiStrategyFactory.getStrategy().stopProcessInstance(BeanUtil.copyProperties(processInstanceParamDto, com.cxygzl.common.dto.third.ProcessInstanceParamDto.class));
        } else {
            ApiStrategyFactory.getStrategy().completeProcessInstance(BeanUtil.copyProperties(processInstanceParamDto, com.cxygzl.common.dto.third.ProcessInstanceParamDto.class));
        }


        //保存数据到数据库
        try {
            String flowId = processInstanceParamDto.getFlowId();
            Process process = processService.getByFlowId(flowId);
            String settings = process.getSettings();
            FlowSettingDto flowSettingDto = JsonUtil.parseObject(settings, FlowSettingDto.class);
            if (flowSettingDto.getDbRecord() != null && flowSettingDto.getDbRecord().getEnable()) {

                String formItems = process.getFormItems();
                List<FormItemVO> formItemVOList = JsonUtil.parseArray(formItems, FormItemVO.class);
                DataRow dataRow = FormStrategyFactory.buildInsertSql(formItemVOList, flowId, processInstanceParamDto.getProcessInstanceId(),
                        processInstanceParamDto.getParamMap());

                anylineService.insert(StrUtil.format("tb_{}", process.getUniqueId()), dataRow);
            }
        } catch (Exception e) {
            log.error("Error", e);
        }


        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 查询我发起的
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryMineStarted(ProcessDataQueryVO pageDto) {

        String userId = StpUtil.getLoginIdAsString();

        //查询所有的流程id
        List<String> allFlowIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(pageDto.getFlowIdList())) {
            List<String> data = processService.getAllRelatedFlowId(pageDto.getFlowIdList()).getData();
            allFlowIdList.addAll(data);
        }

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getUserId, userId)
                .in(CollUtil.isNotEmpty(allFlowIdList), ProcessInstanceRecord::getFlowId,
                        allFlowIdList)
                .orderByDesc(ProcessInstanceRecord::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        List<ProcessInstanceRecord> records = instanceRecordPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return com.cxygzl.common.dto.R.success(instanceRecordPage);
        }


        //流程配置
        Set<String> flowIdSet = records.stream().map(w -> w.getFlowId()).collect(Collectors.toSet());
        List<Process> processList = processService.lambdaQuery().in(Process::getFlowId, flowIdSet).list();

        List<ProcessInstanceRecordVO> processInstanceRecordVOList = BeanUtil.copyToList(records, ProcessInstanceRecordVO.class);

        UserDto userDto = ApiStrategyFactory.getStrategy().getUser(userId);

        for (ProcessInstanceRecordVO record : processInstanceRecordVOList) {
            String formData = record.getFormData();


            //处理表单数据
            Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findFirst().get();
            List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), ProcessInstanceConstant.VariableKey.STARTER, JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
            }));

            record.setFormValueShowList(formValueShowList);
            record.setFormData(null);
            record.setProcess(null);
            record.setRootUserAvatarUrl(userDto.getAvatarUrl());
            record.setRootUserName(userDto.getName());
        }
        Page page = BeanUtil.copyProperties(instanceRecordPage, Page.class);
        page.setRecords(processInstanceRecordVOList);


        return com.cxygzl.common.dto.R.success(page);
    }

    /**
     * 查询流程实例
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryList(ProcessDataQueryVO pageDto) {


        //查询所有的流程id
        List<String> allFlowIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(pageDto.getFlowIdList())) {
            List<String> data = processService.getAllRelatedFlowId(pageDto.getFlowIdList()).getData();
            allFlowIdList.addAll(data);
        }

        List<NodeUser> starterList = pageDto.getStarterList();
        List<String> startTime = pageDto.getStartTime();
        List<String> finishTime = pageDto.getFinishTime();
        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .in(CollUtil.isNotEmpty(allFlowIdList), ProcessInstanceRecord::getFlowId,
                        allFlowIdList)
                .eq(pageDto.getStatus()!=null,ProcessInstanceRecord::getStatus,pageDto.getStatus())
                .ge(CollUtil.isNotEmpty(startTime)&& startTime.size()>=2,ProcessInstanceRecord::getCreateTime,
                        (CollUtil.isNotEmpty(startTime)&& startTime.size()>=2)?(DateUtil.parseDate(startTime.get(0))):null
                        )
                .le(CollUtil.isNotEmpty(startTime)&& startTime.size()>=2,ProcessInstanceRecord::getCreateTime,
                        (CollUtil.isNotEmpty(startTime)&& startTime.size()>=2)?(DateUtil.endOfDay(DateUtil.parseDate(startTime.get(1)))):null
                        )
                .ge(CollUtil.isNotEmpty(finishTime)&& finishTime.size()>=2,ProcessInstanceRecord::getEndTime,
                        (CollUtil.isNotEmpty(finishTime)&& finishTime.size()>=2)?(DateUtil.parseDate(finishTime.get(0))):null
                        )
                .le(CollUtil.isNotEmpty(finishTime)&& finishTime.size()>=2,ProcessInstanceRecord::getEndTime,
                        (CollUtil.isNotEmpty(finishTime)&& finishTime.size()>=2)?(DateUtil.endOfDay(DateUtil.parseDate(finishTime.get(1)))):null
                        )
                .in(CollUtil.isNotEmpty(starterList),ProcessInstanceRecord::getUserId, starterList ==null?new ArrayList<>(): starterList.stream().map(w->w.getId()).collect(Collectors.toList()))
                .eq(StrUtil.isNotBlank(pageDto.getProcessBizCode()),ProcessInstanceRecord::getProcessInstanceBizCode,pageDto.getProcessBizCode())
                .orderByDesc(ProcessInstanceRecord::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        List<ProcessInstanceRecord> records = instanceRecordPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return com.cxygzl.common.dto.R.success(instanceRecordPage);
        }


        //流程配置
        Set<String> flowIdSet = records.stream().map(w -> w.getFlowId()).collect(Collectors.toSet());
        List<Process> processList = processService.lambdaQuery().in(Process::getFlowId, flowIdSet).list();

        List<ProcessInstanceRecordVO> processInstanceRecordVOList = BeanUtil.copyToList(records, ProcessInstanceRecordVO.class);


        for (ProcessInstanceRecordVO record : processInstanceRecordVOList) {
            String formData = record.getFormData();

            UserDto userDto = ApiStrategyFactory.getStrategy().getUser(record.getUserId());

            //处理表单数据
            Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findFirst().get();
            List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), ProcessInstanceConstant.VariableKey.STARTER, JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
            }));

            record.setFormValueShowList(formValueShowList);
            record.setFormData(null);
            record.setProcess(null);
            record.setRootUserAvatarUrl(userDto.getAvatarUrl());
            record.setRootUserName(userDto.getName());

        }
        Page page = BeanUtil.copyProperties(instanceRecordPage, Page.class);
        page.setRecords(processInstanceRecordVOList);


        return com.cxygzl.common.dto.R.success(page);
    }

    /**
     * 查询流程实例详情
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R queryDetailByProcessInstanceId(String processInstanceId) {


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();

        Process process = processService.getByFlowId(processInstanceRecord.getFlowId());

        ProcessInstanceRecordVO record = BeanUtil.copyProperties(processInstanceRecord, ProcessInstanceRecordVO.class);


        //流程配置

        UserDto userDto = ApiStrategyFactory.getStrategy().getUser(processInstanceRecord.getUserId());


        VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
        variableQueryParamDto.setExecutionId(processInstanceId);
        Map<String, Object> data = CoreHttpUtil.queryVariables(variableQueryParamDto).getData();


        List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), null, data);

        record.setFormValueShowList(formValueShowList);
        record.setFormData(null);
        record.setProcess(null);
        record.setRootUserAvatarUrl(userDto.getAvatarUrl());
        record.setRootUserName(userDto.getName());


        return com.cxygzl.common.dto.R.success(record);
    }

    /**
     * 查询抄送给我的
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryMineCC(ProcessDataQueryVO pageDto) {

        String userId = StpUtil.getLoginIdAsString();

        Page<ProcessInstanceCopy> page = processCopyService.lambdaQuery()
                .eq(ProcessInstanceCopy::getUserId, userId)
                .in(CollUtil.isNotEmpty(pageDto.getFlowIdList()), ProcessInstanceCopy::getFlowId,
                        pageDto.getFlowIdList())
                .orderByDesc(ProcessInstanceCopy::getNodeTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        List<ProcessInstanceCopy> records = page.getRecords();

        List<ProcessInstanceCopyVo> processCopyVoList = BeanUtil.copyToList(records, ProcessInstanceCopyVo.class);

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


            for (ProcessInstanceCopyVo record : processCopyVoList) {

                ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                        record.getProcessInstanceId())).findFirst().get();


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(record.getStartUserId())).findAny().orElse(null);
                record.setStartUserName(startUser.getName());


                Process process = processList.stream().filter(w -> StrUtil.equals(w.getFlowId(), record.getFlowId())).findFirst().get();

                List<Dict> formValueShowList = getFormValueShowList(process, record.getFlowId(), ProcessInstanceConstant.VariableKey.STARTER, JsonUtil.parseObject(record.getFormData(), new JsonUtil.TypeReference<Map<String, Object>>() {
                }));

                record.setFormValueShowList(formValueShowList);
                record.setFormData(null);
                record.setProcessInstanceResult(processInstanceRecord.getResult());
                record.setProcessInstanceBizCode(processInstanceRecord.getProcessInstanceBizCode());

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
        List<FormItemVO> formItemVOList = JsonUtil.parseArray(formItems, FormItemVO.class);

        Map<String, String> formPermMap = new HashMap<>();

        if (StrUtil.isNotBlank(nodeId)) {
            String data = processNodeDataService.getNodeData(flowId, nodeId).getData();
            Node node = JsonUtil.parseObject(data, Node.class);
            Map<String, String> map = node.getFormPerms();
            formPermMap.putAll(map);
        } else {
            for (FormItemVO formItemVO : formItemVOList) {
                formPermMap.put(formItemVO.getId(), ProcessInstanceConstant.FormPermClass.READ);
            }
        }


        List<Dict> formValueShowList = new ArrayList<>();


        buildFormValueShow(paramMap, formItemVOList, formPermMap, formValueShowList);

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
//        String s = CoreHttpUtil.showImg(procInsId);
//        com.cxygzl.common.dto.R<String> stringR = CommonUtil.parseObject(s, new TypeReference<com.cxygzl.common.dto.R<String>>() {
//        });
//        String data = stringR.getData();

        //
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, procInsId).one();
        String flowId = processInstanceRecord.getFlowId();
        Process process = processService.getByFlowId(flowId);
        String content = process.getProcess();
        Node node = JsonUtil.parseObject(content, Node.class);
        NodeUtil.addEndNode(node);


        NodeImageVO imageVO = NodeImageUtil.initPosition(node, procInsId);

        return com.cxygzl.common.dto.R.success(imageVO);
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
        if (StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            flowId = processInstanceRecord.getFlowId();
            process = processInstanceRecord.getProcess();

        }
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            String s = CoreHttpUtil.queryTaskVariables(nodeFormatParamVo.getTaskId(), null);
            com.cxygzl.common.dto.R<Map<String, Object>> r = JsonUtil.parseObject(s,
                    new JsonUtil.TypeReference<com.cxygzl.common.dto.R<Map<String, Object>>>() {
                    });
            if (!r.isOk()) {

                List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessInstanceAssignUserRecord::getTaskId, nodeFormatParamVo.getTaskId())
                        .orderByDesc(ProcessInstanceAssignUserRecord::getEndTime)
                        .list();

                String data = list.get(0).getData();
                Map<String, Object> variableMap = JsonUtil.parseObject(data, new JsonUtil.TypeReference<Map<String, Object>>() {
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

        } else if (StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            //任务里没有
            String formData = processInstanceRecord.getFormData();
            Map<String, Object> map = JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
            });
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (paramMap.get(key) == null) {
                    paramMap.put(key, value);
                }
            }
        }


        if (StrUtil.isBlank(process)) {
            Process oaForms = processService.getByFlowId(flowId);
            process = oaForms.getProcess();
        }
        Node nodeDto = JsonUtil.parseObject(process, Node.class);

        //查询所有的节点
        List<ProcessInstanceNodeRecordParamDto> processInstanceNodeRecordParamDtoList = new ArrayList<>();
        if (StrUtil.isNotBlank(processInstanceId)) {
            List<ProcessInstanceNodeRecord> list = processNodeRecordService.lambdaQuery().eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId).list();
            processInstanceNodeRecordParamDtoList.addAll(BeanUtil.copyToList(list, ProcessInstanceNodeRecordParamDto.class));
        }
        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto,
                processInstanceId, paramMap, processInstanceNodeRecordParamDtoList, null);

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


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());
        if (oaForms == null) {
            return com.cxygzl.common.dto.R.fail("流程不存在");
        }


        //发起人变量数据
        String formData = processInstanceRecord.getFormData();
        Map<String, Object> variableMap = JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
        });
        //发起人表单权限
        String process = oaForms.getProcess();
        Node nodeDto = JsonUtil.parseObject(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();


        List<FormItemVO> jsonObjectList = JsonUtil.parseArray(oaForms.getFormItems(), FormItemVO.class);
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


        UserDto starterUser = ApiStrategyFactory.getStrategy().getUser(processInstanceRecord.getUserId());

        TaskDetailViewVO taskDetailViewVO = TaskDetailViewVO.builder()
                .formItems(jsonObjectList)
                .processInstanceId(processInstanceId)
                .process(oaForms.getProcess())
                .processName(oaForms.getName())
                .starterAvatarUrl(starterUser.getAvatarUrl())
                .starterName(starterUser.getName())
                .startTime(processInstanceRecord.getCreateTime())
                .processInstanceStatus(processInstanceRecord.getStatus())
                .processInstanceResult(processInstanceRecord.getResult())
                .build();

        return com.cxygzl.common.dto.R.success(taskDetailViewVO);
    }

    /**
     * 导出流程实例数据
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R export(String processInstanceId) {
        //查询变量参数
        VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
        variableQueryParamDto.setExecutionId(processInstanceId);
        Map<String, Object> paramMap = CoreHttpUtil.queryVariables(variableQueryParamDto).getData();

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        //审批记录
        List<ProcessInstanceOperRecord> processInstanceOperRecordList = processInstanceOperRecordService.lambdaQuery().eq(ProcessInstanceOperRecord::getProcessInstanceId,
                processInstanceId).list();

        String operDesc = processInstanceOperRecordList.stream().map(w -> w.getOperDesc()).collect(Collectors.joining("\r\n\r\n\r\n"));

        List records = new ArrayList();

        UserDto user = ApiStrategyFactory.getStrategy().getUser(processInstanceRecord.getUserId());
        DeptDto dept = ApiStrategyFactory.getStrategy().getDept(user.getDeptId());

        //计算需要几行
        Process process = processService.getByFlowId(processInstanceRecord.getFlowId());
        String formItems = process.getFormItems();
        List<FormItemVO> formItemVOList = JsonUtil.parseArray(formItems, FormItemVO.class);

        //需要表格内部换行的
        Set<Integer> brColList = new LinkedHashSet<>();

        //找出明细
        List<FormItemVO> layoutFormList = formItemVOList.stream().filter(w -> StrUtil.equals(w.getType(), FormTypeEnum.LAYOUT.getType())).collect(Collectors.toList());
        if (layoutFormList.isEmpty()) {
            Dict set = createExcelCommonContent(processInstanceRecord, user, dept, operDesc);
            int colIndex = 0;
            for (FormItemVO formItemVO : formItemVOList) {
                Object o = paramMap.get(formItemVO.getId());
                set.set(formItemVO.getName(), o == null ? "" :
                        FormStrategyFactory.getStrategy(formItemVO.getType()).getProcessInstanceExcelShow(JsonUtil.toJSONString(o)));
                if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.UPLOAD_IMAGE.getType())) {
                    brColList.add(colIndex);
                }
                colIndex++;
            }
            records.add(set);
        } else {


            for (FormItemVO layoutFormItem : layoutFormList) {

                Object o = paramMap.get(layoutFormItem.getId());
                if (o == null) {
                    continue;
                }
                List<?> list = Convert.toList(o);
                if (list.isEmpty()) {
                    continue;
                }
                int index = 1;
                for (Object object : list) {
                    int colIndex = 0;
                    Dict set = createExcelCommonContent(processInstanceRecord, user, dept, operDesc);
                    for (FormItemVO formItemVO : formItemVOList) {

                        if (StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())) {
                            if (formItemVO.getId().equals(layoutFormItem.getId())) {
                                set.put(formItemVO.getName(), StrUtil.format("{}{}", formItemVO.getName(), index));
                                colIndex++;
                                Map<String, Object> map = BeanUtil.beanToMap(object);
                                List<FormItemVO> subItemList = Convert.toList(FormItemVO.class,
                                        formItemVO.getProps().getValue());
                                for (FormItemVO itemVO : subItemList) {
                                    Object value = map.get(itemVO.getId());
                                    String v = value == null ? "" :
                                            FormStrategyFactory.getStrategy(itemVO.getType()).getProcessInstanceExcelShow(JsonUtil.toJSONString(value));
                                    set.put(itemVO.getName(), v);


                                    if (StrUtil.equalsAny(itemVO.getType(), FormTypeEnum.UPLOAD_IMAGE.getType())) {

                                        if (StrUtil.isNotBlank(v)) {
                                            set.put(itemVO.getName(), new XSSFRichTextString(v));

                                        }

                                        brColList.add(colIndex);
                                    }
                                    colIndex++;
                                }

                            } else {
                                set.put(formItemVO.getName(), "");
                                colIndex++;
                                List<FormItemVO> subItemList = Convert.toList(FormItemVO.class,
                                        formItemVO.getProps().getValue());

                                for (FormItemVO itemVO : subItemList) {
                                    set.put(itemVO.getName(), "");

                                    if (StrUtil.equalsAny(itemVO.getType(), FormTypeEnum.UPLOAD_IMAGE.getType())) {
                                        brColList.add(colIndex);
                                    }
                                    colIndex++;

                                }
                            }

                        } else {
                            Object value = paramMap.get(formItemVO.getId());
                            String v = value == null ? "" :
                                    FormStrategyFactory.getStrategy(formItemVO.getType()).getProcessInstanceExcelShow(JsonUtil.toJSONString(value));
                            set.put(formItemVO.getName(), v);

                            if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.UPLOAD_IMAGE.getType())) {
                                if (StrUtil.isNotBlank(v)) {
                                    set.put(formItemVO.getName(), new XSSFRichTextString(v));

                                }

                                brColList.add(colIndex);
                            }
                            colIndex++;
                        }
                    }
                    records.add(set);

                    index++;
                }
            }
        }

        String format = StrUtil.format("/tmp/{}.xls", IdUtil.fastSimpleUUID());
        ExcelWriter writer = new ExcelWriter(format, "表1");

        for (int k = 1; k < records.size(); k++) {
            for (int x : brColList) {
                CellStyle cellStyle = writer.getOrCreateCellStyle(x + 11, k);
                cellStyle.setWrapText(true);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                writer.setStyle(cellStyle, x + 11, k);
            }

        }

        //定义宽度
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 20);
        writer.setColumnWidth(4, 20);
        writer.setColumnWidth(5, 20);
        writer.setColumnWidth(6, 20);
        writer.setColumnWidth(7, 20);
        writer.setColumnWidth(8, 20);
        writer.setColumnWidth(9, 20);
        writer.setColumnWidth(10, 50);

        writer.write(records, true);
        //writer.autoSizeColumnAll();

        writer.close();

        //拼装url

        R<String> r = fileService.save(FileUtil.readBytes(format), StrUtil.format("{}.xls", process.getName()));

        return r;
    }

    private static Dict createExcelCommonContent(ProcessInstanceRecord processInstanceRecord, UserDto user,
                                                 DeptDto dept, String operDesc) {
        String processInstanceId = processInstanceRecord.getProcessInstanceId();
        Integer result = processInstanceRecord.getResult();
        Date endTime = processInstanceRecord.getEndTime();
        Integer duration = null;
        if (endTime != null) {
            int second1 = DateUtil.second(endTime);
            int second2 = DateUtil.second(processInstanceRecord.getCreateTime());

            duration = second1 - second2;
        }

        Dict set = Dict.create()
                .set("标题", processInstanceRecord.getName())
                .set("编号", processInstanceRecord.getProcessInstanceBizCode())
                .set("审批状态", NodeStatusEnum.get(processInstanceRecord.getStatus()).getName())
                .set("审批结果", result == null ? "" : (ApproveResultEnum.getByValue(result).getName()))
                .set("发起时间", processInstanceRecord.getCreateTime())
                .set("完成时间", endTime)
                .set("耗时", DataUtil.getDate(duration))
                .set("发起人UserID", user.getId())
                .set("发起人姓名", user.getName())
                .set("发起人部门", dept.getName())
                .set("审批记录(含处理人UserID)", operDesc);

        if (endTime == null) {
            List<TaskDto> taskDtoList = CoreHttpUtil.queryTaskAssignee(null, processInstanceId).getData();
            Set<String> userIdSet = taskDtoList.stream().map(w -> w.getAssign()).collect(Collectors.toSet());
            List<String> userNameList = new ArrayList<>();
            for (String s : userIdSet) {
                UserDto u = ApiStrategyFactory.getStrategy().getUser(s);
                userNameList.add(u.getName());
            }
            set.set("当前处理人姓名", CollUtil.join(userNameList, ","));
        }


        return set;
    }

    private void handApproveRecord(Node node, List<String> list) {
        Node childNode = node.getChildNode();
        if (!NodeUtil.isNode(childNode)) {
            return;
        }

    }

    /**
     * 终止流程
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R stopProcessInstance(String processInstanceId) {


        TaskParamDto taskParamDto = new TaskParamDto();

        List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceId);
        CollUtil.reverse(allStopProcessInstanceIdList);
        allStopProcessInstanceIdList.add(processInstanceId);

        taskParamDto.setProcessInstanceIdList(allStopProcessInstanceIdList);
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        com.cxygzl.common.dto.R r = CoreHttpUtil.stopProcessInstance(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }
        processInstanceOperRecordService.saveCancelProcessRecord(StpUtil.getLoginIdAsString(), processInstanceId);

        return R.success();
    }

    private List<String> getAllStopProcessInstanceIdList(String processInstanceId) {
        List<ProcessInstanceRecord> list = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getParentProcessInstanceId, processInstanceId).list();

        List<String> collect = list.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toList());

        for (ProcessInstanceRecord processInstanceRecord : list) {
            List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceRecord.getProcessInstanceId());

            collect.addAll(allStopProcessInstanceIdList);

        }
        return collect;
    }

    /**
     * 催办
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public R urgeProcessInstance(TaskParamDto taskParamDto) {
        List<TaskDto> taskDtoList = CoreHttpUtil.queryTaskAssignee(null, taskParamDto.getProcessInstanceId()).getData();
        if (taskDtoList.isEmpty()) {
            return R.fail("暂无待审批任务需要催办");
        }

        String loginIdAsString = StpUtil.getLoginIdAsString();
        UserDto userDto = ApiStrategyFactory.getStrategy().getUser(loginIdAsString);

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId, taskParamDto.getProcessInstanceId())
                .one();

        for (TaskDto taskDto : taskDtoList) {
            if (StrUtil.equals(taskDto.getAssign(), loginIdAsString)) {
                continue;
            }
            com.cxygzl.common.dto.third.MessageDto messageDto = new com.cxygzl.common.dto.third.MessageDto();
            messageDto.setType(MessageTypeEnum.URGE_TASK.getType());
            messageDto.setReaded(false);
            messageDto.setUserId(taskDto.getAssign());
            messageDto.setUniqueId(taskDto.getTaskId());
            messageDto.setContent(StrUtil.format("[{}]提醒您审批他的[{}]:{}", userDto.getName(), processInstanceRecord.getName(),
                    taskParamDto.getApproveDesc()));
            messageDto.setTitle("催办任务");
            messageDto.setFlowId(taskDto.getFlowId());


            messageDto.setProcessInstanceId(taskParamDto.getProcessInstanceId());

            remoteService.saveMessage(messageDto);
        }

        return R.success();
    }

    /**
     * 查询处理中的任务
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R queryTaskListInProgress(String processInstanceId) {

        R<List<TaskDto>> listR = CoreHttpUtil.queryTaskAssignee(null, processInstanceId);
        if(!listR.isOk()){
            return listR;
        }
        List<TaskDto> taskDtoList = listR.getData();
        for (TaskDto taskDto : taskDtoList) {
            UserDto userDto = ApiStrategyFactory.getStrategy().getUser(taskDto.getAssign());
            taskDto.setUserName(userDto.getName());
        }

        return listR;
    }
}
