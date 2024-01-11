package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.NodeFormatUtil;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.ProcessInstanceCopyVo;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.Node;
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
    private IProcessInstanceCopyService processCopyService;

    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;


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
        paramMap.put("root", CollUtil.newArrayList(rootUser));

        String post = CoreHttpUtil.startProcess(processInstanceParamDto);
        R<String> r = JSON.parseObject(post, new TypeReference<R<String>>() {
        });
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }
        String data = r.getData();


        return R.success(data);
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

        R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryAssignTask(taskQueryParamDto);


        PageResultDto<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return R.success(pageResultDto);

        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<String> startUserIdSet =
                processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<UserDto> startUserList =new ArrayList<>();
        {
            for (String userIds : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(userIds);
                startUserList.add(user);
            }
        }
        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());

                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals( processInstanceRecord.getUserId())).findAny().orElse(null);


                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
            }
        }


        return R.success(pageResultDto);
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

        R<PageResultDto<TaskDto>> r= CoreHttpUtil.queryCompletedTask(taskQueryParamDto);


        PageResultDto<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return R.success(pageResultDto);

        }


        Set<String> processInstanceIdSet = records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<String> startUserIdSet =
                processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<UserDto> startUserList =new ArrayList<>();
        {
            for (String userIds : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(userIds);
                startUserList.add(user);
            }
        }


        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals( processInstanceRecord.getUserId())).findAny().orElse(null);


                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
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
    public R end(ProcessInstanceParamDto processInstanceParamDto) {
        processInstanceRecordService.lambdaUpdate()
                .set(processInstanceParamDto.getResult()!=null,ProcessInstanceRecord::getResult, processInstanceParamDto.getResult())
                .set(ProcessInstanceRecord::getEndTime, new Date())
                .set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceParamDto.getProcessInstanceId())
                .update(new ProcessInstanceRecord());
        return R.success();
    }

    /**
     * 查询我发起的
     *
     * @param pageDto
     * @return
     */
    @Override
    public Object queryMineStarted(PageDto pageDto) {

        String userId = StpUtil.getLoginIdAsString();

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getUserId, userId)
                .orderByDesc(ProcessInstanceRecord::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        return R.success(instanceRecordPage);
    }

    /**
     * 查询抄送给我的
     *
     * @param pageDto
     * @return
     */
    @Override
    public Object queryMineCC(PageDto pageDto) {

        String userId = StpUtil.getLoginIdAsString();

        Page<ProcessInstanceCopy> page = processCopyService.lambdaQuery()
                .eq(ProcessInstanceCopy::getUserId, userId)
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

            for (ProcessInstanceCopyVo record : processCopyVoList) {


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(record.getStartUserId())).findAny().orElse(null);
                record.setStartUserName(startUser.getName());
            }
        }

        Page p = BeanUtil.copyProperties(page, Page.class);

        p.setRecords(processCopyVoList);

        return R.success(p);
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
        R<String> stringR = JSON.parseObject(s, new TypeReference<R<String>>() {
        });
        String data = stringR.getData();
//
//        OutputStream out = response.getOutputStream();
//
//        Base64.decodeToStream(data, out, true);
        return R.success(data);
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    @Override
    public Object formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo) {
        String flowId = nodeFormatParamVo.getFlowId();
        String processInstanceId = nodeFormatParamVo.getProcessInstanceId();
        if (StrUtil.isAllBlank(flowId, processInstanceId)) {
            return R.success(new ArrayList<>());
        }
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();



        if (  StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            flowId = processInstanceRecord.getFlowId();

            String formData = processInstanceRecord.getFormData();
            JSONObject jsonObject = JSON.parseObject(formData);


            paramMap.putAll(jsonObject);

        }
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            String s = CoreHttpUtil.queryTaskVariables(nodeFormatParamVo.getTaskId(), null);
            R<Map<String, Object>> r = JSON.parseObject(s,
                    new TypeReference<R<Map<String, Object>>>() {
                    });
            if (!r.isOk()) {

                List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessInstanceAssignUserRecord::getTaskId, nodeFormatParamVo.getTaskId())
                        .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.YJS.getCode())

                        .orderByDesc(ProcessInstanceAssignUserRecord::getEndTime)
                        .list();

                String data = list.get(0).getData();
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

        Set<String> completeNodeSet=new HashSet<>();

        if(StrUtil.isNotBlank(processInstanceId)){
            List<ProcessInstanceNodeRecord> processInstanceNodeRecordList = processNodeRecordService.lambdaQuery()
                    .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId)
                    .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
                    .list();
            Set<String> collect = processInstanceNodeRecordList.stream().map(w -> w.getNodeId()).collect(Collectors.toSet());
            completeNodeSet.addAll(collect);
        }


        Process oaForms = processService.getByFlowId(flowId);
        String process = oaForms.getProcess();
        Node nodeDto = JSON.parseObject(process, Node.class);

        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto, completeNodeSet,
                new HashSet<>(), processInstanceId, paramMap);

        return R.success(processNodeShowDtos);
    }

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public Object detail(String processInstanceId) {


        String userId = StpUtil.getLoginIdAsString();


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());
        if (oaForms == null) {
            return R.fail("流程不存在");
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

            formItemVO.setPerm(StrUtil.isBlankIfStr(perm)? ProcessInstanceConstant.FormPermClass.READ:
                            ( StrUtil.equals(perm,ProcessInstanceConstant.FormPermClass.HIDE)?
                                    perm:ProcessInstanceConstant.FormPermClass.READ
                            )
                    );

            if(formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())){
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, id, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l=new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        itemVO.getProps().setValue(map.get(itemVO.getId()));

                        String permSub = formPerms1.get(itemVO.getId());

                            itemVO.setPerm(StrUtil.isBlankIfStr(permSub)? ProcessInstanceConstant.FormPermClass.READ:
                                    ( StrUtil.equals(permSub,ProcessInstanceConstant.FormPermClass.HIDE)?
                                            permSub:ProcessInstanceConstant.FormPermClass.READ
                                    ));



                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);


            }else{
                formItemVO.getProps().setValue(variableMap.get(id));

            }


        }
        Dict set = Dict.create()
                .set("processInstanceId", processInstanceId)
                .set("process", oaForms.getProcess())


                .set("formItems", jsonObjectList);

        return R.success(set);
    }
}
