package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.FormUtil;
import com.cxygzl.biz.vo.FormRemoteSelectOptionParamVo;
import com.cxygzl.biz.vo.QueryFormListParamVo;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.flow.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FormServiceImpl implements IFormService {

    @Resource
    private IProcessService processService;

    @Resource
    private IProcessInstanceExecutionService executionService;
    @Resource
    private IProcessNodeDataService nodeDataService;

    @Resource
    private ISignatureRecordService signatureRecordService;

    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessInstanceCopyService processCopyService;

    /**
     * 远程请求下拉选项
     *
     * @param formRemoteSelectOptionParamVo
     * @return
     */
    @Override
    public Object selectOptions(FormRemoteSelectOptionParamVo formRemoteSelectOptionParamVo) {

        String remoteUrl = formRemoteSelectOptionParamVo.getRemoteUrl();
        String s = HttpUtil.post(remoteUrl, "");
        List<Map> mapList = JsonUtil.parseArray(s, Map.class);

        for (Map map : mapList) {
            String str = MapUtil.getStr(map, "key");
            map.put("key", str);
        }

        return R.success(mapList);
    }

    /**
     * 获取表单数据
     *
     * @param taskDto
     * @param handleForm
     * @return
     */
    @Override
    public R getFormList(QueryFormListParamVo taskDto, boolean handleForm1) {
        String processInstanceId = taskDto.getProcessInstanceId();
        String flowId = taskDto.getFlowId();
        String taskId = taskDto.getTaskId();
        Long ccId = taskDto.getCcId();

        List<FormItemVO> formItemVOList = new ArrayList<>();

        if (ccId != null) {
            formItemVOList.addAll(getCCFormList(ccId));

        } else if (StrUtil.isAllBlank(processInstanceId, taskId)) {
            //没有流程实例 没有任务


            formItemVOList.addAll(getStartFormList(flowId));

        } else if (StrUtil.isNotBlank(taskId)) {

            formItemVOList.addAll(getTaskFormList(taskId));

        } else if (StrUtil.isNotBlank(processInstanceId)) {
            formItemVOList.addAll(getStartedProcessInstanceFormList(processInstanceId));

        }


        return R.success(formItemVOList);
    }

    /**
     * 动态表单
     *
     * @param taskDto
     * @return
     */
    @Override
    public R dynamicFormList(QueryFormListParamVo taskDto) {
        String flowId = taskDto.getFlowId();
        String processInstanceId = taskDto.getProcessInstanceId();
        String taskId = taskDto.getTaskId();
        Long ccId = taskDto.getCcId();


        Process process = processService.getByFlowId(flowId);
        if (process == null) {
            return R.fail("流程不存在");
        }
        List<FormItemVO> formItemVOList = taskDto.getFormItemVOList();


        if (ccId != null) {
            return R.success(formItemVOList);


        }


        String nodeId = taskDto.getNodeId();

        handleDynamicForm(nodeId, process.getProcess(), taskDto.getParamMap(), formItemVOList, flowId, processInstanceId,
                taskId);


        return R.success(formItemVOList);
    }

    /**
     * 处理动态表单
     *
     * @param paramMap
     * @param formItemVOList
     * @param flowId
     * @param processInstanceId
     */
    private void handleDynamicForm(String nodeId, String process, Map<String, Object> paramMap,
                                   List<FormItemVO> formItemVOList, String flowId, String processInstanceId, String taskId) {

        Node node = null;
        if (StrUtil.isNotBlank(nodeId)) {
            node = nodeDataService.getNode(flowId, nodeId).getData();
        } else {
            node = JsonUtil.parseObject(process, Node.class);
        }
        HttpSetting dynamicFormConfig = node.getDynamicFormConfig();
        if (dynamicFormConfig == null || dynamicFormConfig.getEnable() == null ||
                !dynamicFormConfig.getEnable() || StrUtil.isBlank(dynamicFormConfig.getUrl())) {
            return;
        }


        if (StrUtil.isNotBlank(taskId)) {
            String userId = StpUtil.getLoginIdAsString();


            com.cxygzl.common.dto.R<TaskResultDto> r = CoreHttpUtil.queryTask(taskId, userId);

            TaskResultDto taskResultDto = r.getData();

            if (!taskResultDto.getCurrentTask()) {
                return;
            }

        } else if (StrUtil.isNotBlank(processInstanceId)) {
            return;
        }


        String result = com.cxygzl.common.utils.HttpUtil.flowExtenstionHttpRequest(dynamicFormConfig,
                paramMap,
                flowId,
                processInstanceId, null, null);
        if(StrUtil.isBlank(result)){
            throw new BusinessException("网络请求异常");
        }
        JSONObject jsonObject = JsonUtil.parseObject(result);
        if (jsonObject.isEmpty()) {
            return;
        }
        for (FormItemVO formItemVO : formItemVOList) {
            List<HttpSettingData> resultConfigList = dynamicFormConfig.getResult();
            for (HttpSettingData httpSettingData : resultConfigList) {
                if (!StrUtil.equals(httpSettingData.getValue(), formItemVO.getId())) {
                    continue;
                }
                String field = httpSettingData.getField();
                Object o = jsonObject.get(field);
                if (o == null) {
                    continue;
                }
                String contentConfig = httpSettingData.getContentConfig();
                if (StrUtil.equalsAny(contentConfig, "perm", "required")) {
                    ReflectUtil.setFieldValue(formItemVO, contentConfig, o);

                } else {
                    ReflectUtil.setFieldValue(formItemVO.getProps(), contentConfig, o);

                }
            }
        }

    }

    private List<FormItemVO> getCCFormList(long ccId) {

        ProcessInstanceCopy processCopy = processCopyService.getById(ccId);

        String flowId = processCopy.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);

        String formData = processCopy.getFormData();

        Map<String, Object> variableMap = JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
        });

        String nodeId = processCopy.getNodeId();


        String data = nodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = JsonUtil.parseObject(data, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        List<FormItemVO> formItemVOList = JsonUtil.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {


            String fid = formItemVO.getId();
            String perm = formPerms.get(fid);
            formItemVO.setPerm(StrUtil.isBlankIfStr(perm) ? ProcessInstanceConstant.FormPermClass.HIDE : perm);


            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, fid, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        Object value1 = map.get(itemVO.getId());


                        FormUtil.handValue(itemVO, value1);


                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub) ? ProcessInstanceConstant.FormPermClass.READ : permSub

                            );


                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);


            } else {
                Object value = variableMap.get(fid);

                FormUtil.handValue(formItemVO, value);
            }

        }
        return formItemVOList;
    }


    /**
     * 我发起的流程表单
     *
     * @param processInstanceId
     * @return
     */
    private List<FormItemVO> getStartedProcessInstanceFormList(String processInstanceId) {


        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());


        //发起人变量数据
        String formData = processInstanceRecord.getFormData();
        Map<String, Object> variableMap = JsonUtil.parseObject(formData, new JsonUtil.TypeReference<Map<String, Object>>() {
        });
        //发起人表单权限
        String process = oaForms.getProcess();
        Node nodeDto = JsonUtil.parseObject(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();


        List<FormItemVO> formItemVOList = JsonUtil.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {
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
        return formItemVOList;
    }

    /**
     * 查询任务表单列表
     *
     * @param taskId
     * @return
     */
    private List<FormItemVO> getTaskFormList(String taskId) {


        String userId = StpUtil.getLoginIdAsString();


        com.cxygzl.common.dto.R<TaskResultDto> r = CoreHttpUtil.queryTask(taskId, userId);

        TaskResultDto taskResultDto = r.getData();

        String flowId = taskResultDto.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);


        List<ProcessInstanceExecution> processExecutionList = executionService.lambdaQuery()
                .eq(ProcessInstanceExecution::getExecutionId, taskResultDto.getExecutionId())
                .or()
                .eq(ProcessInstanceExecution::getChildExecutionId, taskResultDto.getExecutionId())
                .list();

        Set<String> executionIdSet = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toSet());
        processExecutionList.forEach(rr -> executionIdSet.add(rr.getExecutionId()));


        //变量
        Map<String, Object> paramMap = taskResultDto.getVariableAll();
        Boolean currentTask = taskResultDto.getCurrentTask();
        if (!currentTask) {
            //任务已完成了
            List<ProcessInstanceAssignUserRecord> processInstanceAssignUserRecordList = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, taskId)
                    .eq(ProcessInstanceAssignUserRecord::getUserId, userId)
                    .eq(StrUtil.isNotBlank(taskResultDto.getFlowUniqueId()),
                            ProcessInstanceAssignUserRecord::getFlowUniqueId, taskResultDto.getFlowUniqueId())
                    .in(ProcessInstanceAssignUserRecord::getExecutionId, executionIdSet)
                    .orderByDesc(ProcessInstanceAssignUserRecord::getUpdateTime)
                    .list();

            String data = processInstanceAssignUserRecordList.get(0).getData();
            if (StrUtil.isNotBlank(data)) {
                Map<String, Object> collect = JsonUtil.parseObject(data, new JsonUtil.TypeReference<Map<String, Object>>() {
                });
                paramMap.putAll(collect);

            }
        }


        //当前节点数据
        String nodeId = taskResultDto.getNodeId();
        if (StrUtil.startWith(nodeId, ProcessInstanceConstant.VariableKey.STARTER)) {
            nodeId = ProcessInstanceConstant.VariableKey.STARTER;
        }

        Node node = nodeDataService.getNode(flowId, nodeId).getData();
        Map<String, String> formPerms = node.getFormPerms();


        List<FormItemVO> formItemVOList = JsonUtil.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {


            String id = formItemVO.getId();

            String perm = formPerms.get(id);


            if (StrUtil.isNotBlank(perm)) {

                formItemVO.setPerm((!currentTask) ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(perm) ?
                        ProcessInstanceConstant.FormPermClass.READ : perm) : perm);

            } else {
                formItemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
            }

            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(paramMap, id, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                if (subParamList == null) {
                    subParamList = new ArrayList<>();
                }

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        Object value1 = map.get(itemVO.getId());

                        FormUtil.handValue(itemVO, value1);


                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm((!currentTask) ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub) ?
                                    ProcessInstanceConstant.FormPermClass.READ : permSub)
                                    : permSub
                            );


                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);
                {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {

                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {


                            itemVO.setPerm(permSub);

                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    formItemVO.getProps().setOriForm(subItemList);

                }

            } else {

                Object value = paramMap.get(id);
                FormUtil.handValue(formItemVO, value);


            }

        }


        return formItemVOList;

    }

    /**
     * 发起流程表单
     *
     * @param flowId
     * @return
     */
    private List<FormItemVO> getStartFormList(String flowId) {


        Process oaForms = processService.getByFlowId(flowId);

        String process = oaForms.getProcess();
        String formItems = oaForms.getFormItems();
        Node startNode = JsonUtil.parseObject(process, Node.class);


        Map<String, String> formPerms = startNode.getFormPerms();
        List<FormItemVO> t = JsonUtil.parseArray(formItems, FormItemVO.class);

        for (FormItemVO formItemVO : t) {
            String perm = MapUtil.getStr(formPerms, formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
            formItemVO.setPerm(perm);

            FormItemVO.Props formItemVOProps = formItemVO.getProps();
            if (StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())) {
                //明细
                Object value = formItemVOProps.getValue();
                List<FormItemVO> subList = Convert.toList(FormItemVO.class, value);
                for (FormItemVO itemVO : subList) {
                    String perm1 = MapUtil.getStr(formPerms, itemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
                    itemVO.setPerm(perm1);
                    if (true) {
                        handleForm(itemVO);

                    }
                }


                formItemVOProps.setValue(subList);


            }
            if (true) {

                handleForm(formItemVO);
            }

        }
        return t;
    }


    /**
     * 处理表单
     *
     * @param formItemVO
     */
    private static void handleForm(FormItemVO formItemVO) {
        FormItemVO.Props formItemVOProps = formItemVO.getProps();
        if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.SELECT_USER.getType(), FormTypeEnum.SELECT_MULTI_USER.getType())) {
            Boolean defaultRoot = formItemVOProps.getDefaultRoot();
            if (defaultRoot != null && defaultRoot) {
                //处理默认值
                UserDto user = ApiStrategyFactory.getStrategy().getUser(StpUtil.getLoginIdAsString());
                NodeUser nodeUserDto = new NodeUser();
                nodeUserDto.setId(String.valueOf(user.getId()));
                nodeUserDto.setName(user.getName());
                nodeUserDto.setType(NodeUserTypeEnum.USER.getKey());
                nodeUserDto.setSelected(true);
                nodeUserDto.setAvatar(user.getAvatarUrl());

                formItemVOProps.setValue(CollUtil.newArrayList(nodeUserDto));
            }
        }
        if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.SIGNATURE.getType())&&formItemVOProps.getLastContent()&&StrUtil.isBlankIfStr(formItemVOProps.getValue())) {

            ISignatureRecordService bean = SpringUtil.getBean(ISignatureRecordService.class);

            List<SignatureRecord> signatureRecordList = bean.lambdaQuery().eq(SignatureRecord::getUserId, StpUtil.getLoginIdAsString())
                    .orderByDesc(SignatureRecord::getCreateTime)
                    .list();
            if(CollUtil.isNotEmpty(signatureRecordList)){
                SignatureRecord signatureRecord = signatureRecordList.get(0);
                formItemVOProps.setValue(signatureRecord.getUrl());

            }

        }
        if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.SELECT_DEPT.getType(), FormTypeEnum.SELECT_MULTI_DEPT.getType())) {
            Boolean defaultRoot = formItemVOProps.getDefaultRoot();
            if (defaultRoot != null && defaultRoot) {
                //处理默认值
                UserDto user = ApiStrategyFactory.getStrategy().getUser(StpUtil.getLoginIdAsString());
                String deptId = user.getDeptId();
                DeptDto dept = ApiStrategyFactory.getStrategy().getDept(deptId);
                NodeUser nodeUserDto = new NodeUser();
                nodeUserDto.setId(String.valueOf(dept.getId()));
                nodeUserDto.setName(dept.getName());
                nodeUserDto.setType(NodeUserTypeEnum.DEPT.getKey());
                nodeUserDto.setSelected(true);

                formItemVOProps.setValue(CollUtil.newArrayList(nodeUserDto));
            }
        }
    }
}
