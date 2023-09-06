package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.NodeFormatUtil;
import com.cxygzl.biz.vo.NodeFormatParamVo;
import com.cxygzl.biz.vo.NodeFormatResultVo;
import com.cxygzl.biz.vo.QueryFormListParamVo;
import com.cxygzl.biz.vo.node.NodeVo;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.IndexPageStatistics;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BaseServiceImpl implements IBaseService {

    @Resource
    private IProcessCopyService processCopyService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    @Resource
    private IProcessNodeDataService processNodeDataService;

    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeRecordService processNodeRecordService;
    @Resource
    private IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;

    /**
     * 首页数据
     *
     * @return
     */
    @Override
    public R index() {

        String userId = StpUtil.getLoginIdAsString();


        Long coypNum = processCopyService.lambdaQuery().eq(ProcessCopy::getUserId, userId).count();

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
//        List<AreaVO> areaVOList = JSON.parseArray(json, AreaVO.class);
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


        return R.success(JSON.parseArray(json));
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
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            String s = CoreHttpUtil.queryTaskVariables(nodeFormatParamVo.getTaskId(), null);
            com.cxygzl.common.dto.R<Map<String, Object>> r = com.alibaba.fastjson2.JSON.parseObject(s,
                    new TypeReference<R<Map<String, Object>>>() {
                    });
            if (!r.isOk()) {

                List<ProcessNodeRecordAssignUser> list = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessNodeRecordAssignUser::getTaskId, nodeFormatParamVo.getTaskId())
                        .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                        .list();

                String data = list.get(0).getData();
                Map<String, Object> variableMap = com.alibaba.fastjson2.JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
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


                //判断是不是子流程的发起人任务

                Object subProcessStarterNode =
                        paramMap.get(ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE);
                Object rejectStarterNode = paramMap.get(ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE);
                disableSelectUser = !(Convert.toBool(subProcessStarterNode, false) && rejectStarterNode == null);

            }


        } else if (StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            //任务里没有
            String formData = processInstanceRecord.getFormData();
            Map<String, Object> map = com.alibaba.fastjson2.JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
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
        Node nodeDto = com.alibaba.fastjson2.JSON.parseObject(process, Node.class);


        //查询所有的节点
        List<ProcessNodeRecordParamDto> processNodeRecordParamDtoList = new ArrayList<>();
        if (StrUtil.isNotBlank(processInstanceId)) {
            List<ProcessNodeRecord> list = processNodeRecordService.lambdaQuery().eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId).list();
            processNodeRecordParamDtoList.addAll(BeanUtil.copyToList(list, ProcessNodeRecordParamDto.class));
        }
        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto,
                processInstanceId, paramMap, processNodeRecordParamDtoList, disableSelectUser);

        NodeFormatResultVo nodeFormatResultVo = NodeFormatResultVo.builder()
                .processNodeShowDtoList(processNodeShowDtos)
                .selectUserNodeIdList(NodeUtil.selectUserNodeId(nodeDto))
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

            ProcessCopy processCopy = processCopyService.getById(ccId);
            processInstanceId = processCopy.getProcessInstanceId();

        } else if (StrUtil.isAllBlank(processInstanceId, taskId)) {
            //没有流程实例 没有任务


        }


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId,
                        processInstanceId).one();
        String starterUserId = processInstanceRecord.getUserId();
        UserDto starterUser = ApiStrategyFactory.getStrategy().getUser(starterUserId);

        Dict set = Dict.create()
                .set("processInstanceResult", processInstanceRecord.getResult())
                .set("starterName", starterUser.getName())
                .set("starterAvatarUrl", starterUser.getAvatarUrl())
                .set("processName", processInstanceRecord.getName())
                .set("startTime", processInstanceRecord.getCreateTime());

        return R.success(set);
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
        Boolean taskExist = taskResultDto.getCurrentTask();
        if (!taskExist) {
            Dict set = Dict.create()

                    .set("processInstanceId", taskResultDto.getProcessInstanceId())

                    .set("taskExist", taskExist);

            return R.success(set);

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
        Node node = CommonUtil.toObj(nodeDataJson, Node.class);
        List operList = node.getOperList();
        String process = oaForms.getProcess();

        Dict set = Dict.create()
                .set("operList", operList)
                .set("processInstanceId", taskResultDto.getProcessInstanceId())
                .set("nodeId", nodeId)
                .set("node", node)
                .set("taskExist", taskExist)
                .set("process", CommonUtil.toObj(process, Node.class));

        return R.success(set);
    }
}
