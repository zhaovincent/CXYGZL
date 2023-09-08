package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.api.ApiStrategyFactory;
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
import com.cxygzl.common.utils.CommonUtil;
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
    private IProcessExecutionService executionService;
    @Resource
    private IProcessNodeDataService nodeDataService;

    @Resource
    private IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessCopyService processCopyService;

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
        List<Map> mapList = JSON.parseArray(s, Map.class);

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
    public R getFormList(QueryFormListParamVo taskDto, boolean handleForm) {
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
        List<FormItemVO> formItemVOList = taskDto.getFormItemVOList();

        Process process = processService.getByFlowId(taskDto.getFlowId());
        if (process == null) {
            return R.fail("流程不存在");
        }
        String nodeId = taskDto.getNodeId();
        Node node = null;
        if (StrUtil.isNotBlank(nodeId)) {
            node = nodeDataService.getNode(taskDto.getFlowId(), taskDto.getNodeId()).getData();
        } else {
            node = JSON.parseObject(process.getProcess(), Node.class);
        }
        HttpSetting dynamicFormConfig = node.getDynamicFormConfig();
        if (dynamicFormConfig != null&&StrUtil.isNotBlank(dynamicFormConfig.getUrl())) {
            handleForm(dynamicFormConfig, taskDto.getParamMap(), formItemVOList, taskDto.getFlowId(), null);
        }

        return R.success(formItemVOList);
    }

    private void handleForm(HttpSetting dynamicFormConfig, Map<String, Object> paramMap, List<FormItemVO> formItemVOList, String flowId, String processInstanceId) {
        String result = com.cxygzl.common.utils.HttpUtil.flowExtenstionHttpRequest(dynamicFormConfig,
                paramMap,
                flowId,
                processInstanceId);
        JSONObject jsonObject = JSON.parseObject(result);
        if(jsonObject.isEmpty()){
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
                if(StrUtil.equalsAny(contentConfig,"perm","required")){
                    ReflectUtil.setFieldValue(formItemVO, contentConfig,o);

                }else{
                    ReflectUtil.setFieldValue(formItemVO.getProps(), contentConfig,o);

                }
            }
        }

    }

    private List<FormItemVO> getCCFormList(long ccId) {

        ProcessCopy processCopy = processCopyService.getById(ccId);

        String flowId = processCopy.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);

        String formData = processCopy.getFormData();

        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });

        String nodeId = processCopy.getNodeId();


        String data = nodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = JSON.parseObject(data, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        List<FormItemVO> formItemVOList = JSON.parseArray(oaForms.getFormItems(), FormItemVO.class);
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
        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });
        //发起人表单权限
        String process = oaForms.getProcess();
        Node nodeDto = JSON.parseObject(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();


        List<FormItemVO> formItemVOList = JSON.parseArray(oaForms.getFormItems(), FormItemVO.class);
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


        List<ProcessExecution> processExecutionList = executionService.lambdaQuery()
                .eq(ProcessExecution::getExecutionId, taskResultDto.getExecutionId())
                .or()
                .eq(ProcessExecution::getChildExecutionId, taskResultDto.getExecutionId())
                .list();

        Set<String> executionIdSet = processExecutionList.stream().map(w -> w.getChildExecutionId()).collect(Collectors.toSet());
        processExecutionList.forEach(rr -> executionIdSet.add(rr.getExecutionId()));


        //变量
        Map<String, Object> paramMap = taskResultDto.getVariableAll();
        Boolean taskExist = taskResultDto.getCurrentTask();
        if (!taskExist) {
            //任务已完成了
            List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessNodeRecordAssignUser::getTaskId, taskId)
                    .eq(ProcessNodeRecordAssignUser::getUserId, userId)
                    .eq(StrUtil.isNotBlank(taskResultDto.getFlowUniqueId()),
                            ProcessNodeRecordAssignUser::getFlowUniqueId, taskResultDto.getFlowUniqueId())
                    .in(ProcessNodeRecordAssignUser::getExecutionId, executionIdSet)
                    .orderByDesc(ProcessNodeRecordAssignUser::getUpdateTime)
                    .list();

            String data = processNodeRecordAssignUserList.get(0).getData();
            if (StrUtil.isNotBlank(data)) {
                Map<String, Object> collect = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
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


        List<FormItemVO> formItemVOList = CommonUtil.toArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {


            String id = formItemVO.getId();

            String perm = formPerms.get(id);


            if (StrUtil.isNotBlank(perm)) {

                formItemVO.setPerm((!taskExist) ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(perm) ?
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
                            itemVO.setPerm((!taskExist) ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub) ?
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
        Node startNode = CommonUtil.toObj(process, Node.class);


        Map<String, String> formPerms = startNode.getFormPerms();
        List<FormItemVO> t = JSON.parseArray(formItems, FormItemVO.class);

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
