package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.constants.SystemConstants;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.form.FormStrategyFactory;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.NodeFormatUtil;
import com.cxygzl.biz.vo.*;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.constants.TaskTypeEnum;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BaseServiceImpl implements IBaseService {

    @Resource
    private IProcessInstanceCopyService processCopyService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

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
    /**
     * 修改前端版本号
     *
     * @param webVersionVO
     * @return
     */
    @Override
    public R setWebVersion(WebVersionVO webVersionVO) {

        redisTemplate.opsForValue().set(SystemConstants.VERSION_REDIS_KEY,webVersionVO.getVersionNo());

        return R.success();
    }

    /**
     * 首页数据
     *
     * @return
     */
    @Override
    public R index() {

        String userId = StpUtil.getLoginIdAsString();


        Long coypNum = processCopyService.lambdaQuery().eq(ProcessInstanceCopy::getUserId, userId).count();

        Long startendNum = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getUserId, userId).count();

        IndexPageStatistics indexPageStatistics = CoreHttpUtil.querySimpleData(userId).getData();
        indexPageStatistics.setCopyNum(coypNum);
        indexPageStatistics.setStartedNum(startendNum);

        return R.success(indexPageStatistics);
    }

    /**
     * 获取所有地区数据
     *
     * @return
     */
    @Override
    public R areaList() {

        ClassPathResource classPathResource = new ClassPathResource("area.json");
        String json = FileUtil.readUtf8String(classPathResource.getFile());
//
//        List<AreaVO> areaVOList = CommonUtil.parseArray(json, AreaVO.class);
//        List<AreaVO> provinceList = areaVOList.stream().filter(w -> StrUtil.endWith(w.getCode(), "0000")).collect(Collectors.toList());
//        List<AreaVO> cityList =
//                areaVOList.stream()
//                        .filter(w -> !StrUtil.endWith(w.getCode(), "0000"))
//                        .filter(w -> StrUtil.endWith(w.getCode(), "00"))
//                        .collect(Collectors.toList());
//        List<AreaVO> areaList =
//                areaVOList.stream()
//                        .filter(w -> !StrUtil.endWith(w.getCode(), "0000"))
//                        .filter(w -> !StrUtil.endWith(w.getCode(), "00"))
//                        .collect(Collectors.toList());
//        provinceList.forEach(res -> res.setParentCode("000000"));
//        cityList.forEach(res -> res.setParentCode(res.getProvince() + "0000"));
//        areaList.forEach(res -> res.setParentCode(res.getProvince() + res.getCity() + "00"));
//
//        List<AreaVO> list = new ArrayList<>();
//        list.addAll(provinceList);
//        list.addAll(cityList);
//        list.addAll(areaList);
//        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
//        for (AreaVO areaVO : list) {
//
//            TreeNode<String> treeNode = new TreeNode<>(areaVO.getCode(), areaVO.getParentCode(),
//                    areaVO.getName(), "000000");
//            nodeList.add(treeNode);
//        }
//        List<Tree<String>> treeList = TreeUtil.build(nodeList, "000000");


        return R.success(JsonUtil.parseArray(json));
    }

    /**
     * 同步数据
     *
     * @return
     */
    @Override
    public R loadRemoteData() {
        ApiStrategyFactory.getStrategy().loadRemoteData();
        return R.success();
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

        boolean disableSelectUser = true;

        //处理参数
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        if (StrUtil.isNotBlank(nodeFormatParamVo.getCcId())) {

            ProcessInstanceCopy processInstanceCopy = processCopyService.getById(nodeFormatParamVo.getCcId());
            Map<String, Object> variableMap = JsonUtil.parseObject(processInstanceCopy.getFormData(),
                    new JsonUtil.TypeReference<Map<String, Object>>() {
                    });
            if (variableMap == null) {
                variableMap = new HashMap<>();
            }

            paramMap.putAll(variableMap);
        } else if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {


            R<TaskResultDto> r = CoreHttpUtil.queryTask(nodeFormatParamVo.getTaskId(), StpUtil.getLoginIdAsString());


            TaskResultDto taskResultDto = r.getData();
            if (!r.isOk() || !taskResultDto.getCurrentTask()) {

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

                paramMap.putAll(variableMap);


            } else {
                Map<String, Object> variableMap = taskResultDto.getVariableAll();
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);


                String nodeId = taskResultDto.getNodeId();
                //判断是不是子流程的发起人任务

                Object subProcessStarterNode =
                        paramMap.get(ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE);
                Object rejectStarterNode = paramMap.get(ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE);
                disableSelectUser =
                        (!StrUtil.startWith(nodeId, ProcessInstanceConstant.VariableKey.STARTER)) || (!(Convert.toBool(subProcessStarterNode, false) && rejectStarterNode == null));

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
        } else {
            disableSelectUser = false;
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
                processInstanceId, paramMap, processInstanceNodeRecordParamDtoList, disableSelectUser);

        NodeFormatResultVo nodeFormatResultVo = NodeFormatResultVo.builder()
                .processNodeShowDtoList(processNodeShowDtos)
                .selectUserNodeIdList(disableSelectUser ? new ArrayList<>() : NodeUtil.selectUserNodeId(nodeDto))
                .disableSelectUser(disableSelectUser)
                .build();

        return com.cxygzl.common.dto.R.success(nodeFormatResultVo);
    }

    /**
     * 查询头部显示数据
     *
     * @param nodeFormatParamVo
     * @return
     */
    @Override
    public R queryHeaderShow(QueryFormListParamVo nodeFormatParamVo) {
        String taskId = nodeFormatParamVo.getTaskId();
        String flowId = nodeFormatParamVo.getFlowId();
        Long ccId = nodeFormatParamVo.getCcId();
        String processInstanceId = nodeFormatParamVo.getProcessInstanceId();

        if (ccId != null) {

            ProcessInstanceCopy processCopy = processCopyService.getById(ccId);
            processInstanceId = processCopy.getProcessInstanceId();

        } else if (StrUtil.isAllBlank(processInstanceId, taskId)) {
            //没有流程实例 没有任务


        }


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId,
                        processInstanceId).one();
        String starterUserId = processInstanceRecord.getUserId();
        UserDto starterUser = ApiStrategyFactory.getStrategy().getUser(starterUserId);


        TaskHeaderShowResultVO taskHeaderShowResultVO = new TaskHeaderShowResultVO();
        taskHeaderShowResultVO.setProcessInstanceId(processInstanceRecord.getProcessInstanceId());
        taskHeaderShowResultVO.setStarterName(starterUser.getName());
        taskHeaderShowResultVO.setStarterAvatarUrl(starterUser.getAvatarUrl());
        taskHeaderShowResultVO.setProcessName(processInstanceRecord.getName());
        taskHeaderShowResultVO.setStartTime(processInstanceRecord.getCreateTime());
        taskHeaderShowResultVO.setProcessInstanceResult(processInstanceRecord.getResult());


        return R.success(taskHeaderShowResultVO);
    }

    /**
     * 获取任务操作数据
     *
     * @param taskId
     * @return
     */
    @Override
    public R queryTaskOperData(String taskId) {
        String userId = StpUtil.getLoginIdAsString();


        com.cxygzl.common.dto.R<TaskResultDto> r = CoreHttpUtil.queryTask(taskId, userId);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }

        TaskResultDto taskResultDto = r.getData();
        Boolean currentTask = taskResultDto.getCurrentTask();
        if (!currentTask) {

            TaskOperDataResultVO taskOperDataResultVO = new TaskOperDataResultVO();
            taskOperDataResultVO.setProcessInstanceId(taskResultDto.getProcessInstanceId());

            taskOperDataResultVO.setTaskExist(false);


            return R.success(taskOperDataResultVO);

        }

        String flowId = taskResultDto.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);
        if (oaForms == null) {
            return R.fail("流程不存在");
        }
        //当前节点数据
        String nodeId = taskResultDto.getNodeId();
        if (StrUtil.startWith(nodeId, ProcessInstanceConstant.VariableKey.STARTER)) {
            nodeId = ProcessInstanceConstant.VariableKey.STARTER;
        }
        String nodeDataJson =
                processNodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = JsonUtil.parseObject(nodeDataJson, Node.class);
        List operList = node.getOperList();
        String process = oaForms.getProcess();

        TaskOperDataResultVO taskOperDataResultVO = new TaskOperDataResultVO();
        taskOperDataResultVO.setProcessInstanceId(taskResultDto.getProcessInstanceId());
        taskOperDataResultVO.setNodeId(nodeId);
        taskOperDataResultVO.setTaskExist(currentTask);
        taskOperDataResultVO.setFrontJoinTask(taskResultDto.getFrontJoinTask());
        taskOperDataResultVO.setOperList(operList);
        taskOperDataResultVO.setNode(node);
        taskOperDataResultVO.setProcess(JsonUtil.parseObject(process, Node.class));


        return R.success(taskOperDataResultVO);
    }

    /**
     * 查询打印数据
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public R queryPrintData(String processInstanceId) {

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId,
                        processInstanceId).one();
        String starterUserId = processInstanceRecord.getUserId();
        UserDto starterUser = ApiStrategyFactory.getStrategy().getUser(starterUserId);

        DeptDto dept = ApiStrategyFactory.getStrategy().getDept(starterUser.getDeptId());


        PrintDataResultVO printDataResultVO = new PrintDataResultVO();
        printDataResultVO.setProcessInstanceResult(processInstanceRecord.getResult());
        printDataResultVO.setProcessStatus(processInstanceRecord.getStatus());
        printDataResultVO.setProcessInstanceId(processInstanceRecord.getProcessInstanceId());
        printDataResultVO.setProcessStatusShow(NodeStatusEnum.get(processInstanceRecord.getStatus()).getName());
        printDataResultVO.setStarterName(starterUser.getName());
        printDataResultVO.setStarterDeptName(dept.getName());
        printDataResultVO.setProcessName(processInstanceRecord.getName());
        printDataResultVO.setStartTime(DateUtil.format(processInstanceRecord.getCreateTime(), "yyyy-MM-dd HH:mm"));


        //查询所有的变量
        Map<String, Object> paramMap = new HashMap<>();
        if (processInstanceRecord.getStatus().intValue() == NodeStatusEnum.JXZ.getCode()) {
            VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
            variableQueryParamDto.setExecutionId(processInstanceId);
            paramMap = CoreHttpUtil.queryVariables(variableQueryParamDto).getData();

        } else {
            paramMap = JsonUtil.parseObject(processInstanceRecord.getFormData(),
                    new JsonUtil.TypeReference<Map<String, Object>>() {
                    });
        }

        //表单
        List formList = new ArrayList();
        String flowId = processInstanceRecord.getFlowId();
        Process process = processService.getByFlowId(flowId);
        String formItems = process.getFormItems();
        List<FormItemVO> formItemVOList = JsonUtil.parseArray(formItems, FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {
            if (formItemVO.getPrintable() != null && !formItemVO.getPrintable()) {
                continue;
            }

            PrintDataResultVO.Form f = new PrintDataResultVO.Form();
            f.setFormName(formItemVO.getName());
            f.setFormType(formItemVO.getType());
            f.setFormValue(paramMap.get(formItemVO.getId()));


            //处理表单显示
            String printShow = FormStrategyFactory.getStrategy(formItemVO.getType()).printShow(formItemVO, paramMap.get(formItemVO.getId()));

            f.setFormValueShow(printShow);
            formList.add(f);
        }
        printDataResultVO.setFormList(formList);

        //流程审批节点
        List approveList = new ArrayList();
        List<ProcessInstanceAssignUserRecord> processInstanceAssignUserRecordList = processNodeRecordAssignUserService.lambdaQuery().eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceId)
                .orderByAsc(ProcessInstanceAssignUserRecord::getCreateTime)
                .list();
        for (ProcessInstanceAssignUserRecord processInstanceAssignUserRecord : processInstanceAssignUserRecordList) {

            String userId = processInstanceAssignUserRecord.getUserId();
            UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

            Date endTime = processInstanceAssignUserRecord.getEndTime();
            String taskType = processInstanceAssignUserRecord.getTaskType();

            List<SimpleApproveDescDto> simpleApproveDescDtoList = CoreHttpUtil.queryTaskComments(processInstanceAssignUserRecord.getTaskId()).getData();
            //非系统评论
            List<SimpleApproveDescDto> notSysApproveDescDtoList =
                    simpleApproveDescDtoList.stream().filter(w -> !w.getSys()).collect(Collectors.toList());

            PrintDataResultVO.Approve approve = new PrintDataResultVO.Approve();
            approve.setUserName(user.getName());
            approve.setNodeName(processInstanceAssignUserRecord.getNodeName());
            approve.setTaskType(taskType);
            approve.setTaskTypeShow(StrUtil.isBlankIfStr(taskType) ? "-" : TaskTypeEnum.getByValue(taskType).getName());
            approve.setDate(endTime == null ? null : DateUtil.format(endTime, "yyyy-MM-dd HH:mm"));
            approve.setComment(notSysApproveDescDtoList);


            approveList.add(approve);
        }
        printDataResultVO.setApproveList(approveList);

        return R.success(printDataResultVO);
    }
}
