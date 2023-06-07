package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessNodeData;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeFormMappingDto;
import com.cxygzl.common.dto.process.NodeFormPermDto;
import com.cxygzl.common.dto.process.NodeUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeRecordService processNodeRecordService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    /**
     * 查询任务
     *
     * @param taskId
     * @return
     */
    @Override
    public Object queryTask(String taskId) {


        long userId = StpUtil.getLoginIdAsLong();


        String post = CoreHttpUtil.queryTask(taskId,userId);
        com.cxygzl.common.dto.R<TaskResultDto> r = JSON.parseObject(post, new TypeReference<R<TaskResultDto>>() {
        });
        if (!r.isOk()) {
            return com.cxygzl.biz.utils.R.badRequest(r.getMsg());
        }

        TaskResultDto taskResultDto = r.getData();

        List<NodeFormPermDto> formPerms = taskResultDto.getTaskNodeDto().getProps().getFormPerms();

        //变量
        Map<String, Object> paramMap=new HashMap<>();
        //是否是当前活动任务
        Boolean currentTask = taskResultDto.getCurrentTask();
        if(!currentTask){
            ProcessNodeRecordAssignUser processNodeRecordAssignUser = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessNodeRecordAssignUser::getTaskId, taskId)
                    .eq(ProcessNodeRecordAssignUser::getUserId, userId)
                    .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YJS.getCode())
                    .last("limit 1")
                    .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                    .one();

            String data = processNodeRecordAssignUser.getData();
            if(StrUtil.isNotBlank(data)){
                Map<String, Object> collect = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                });
                paramMap.putAll(collect);

            }
        }else{
            Map<String, Object> collect = formPerms.stream().collect(Collectors.toMap(w -> w.getId(), w -> w.getValue()));
            paramMap.putAll(collect);
        }



        Process oaForms = processService.getByFormId(taskResultDto.getFlowId());
        if (oaForms == null) {
            return com.cxygzl.biz.utils.R.badRequest("流程不存在");
        }

        List<JSONObject> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), JSONObject.class);
        for (JSONObject jsonObject : jsonObjectList) {
            String id = jsonObject.getString("id");
            NodeFormPermDto nodeFormPermDto = formPerms.stream().filter(w -> StrUtil.equals(id, w.getId())).findAny().orElse(null);
            if (nodeFormPermDto != null) {
                JSONObject props = jsonObject.getJSONObject("props");
                props.put("perm", nodeFormPermDto.getPerm());
                if(!currentTask){
                    props.put("perm", StrUtil.equals(nodeFormPermDto.getPerm(),"H")?nodeFormPermDto.getPerm():"R");

                }

                jsonObject.put("value",paramMap.get(id));
            }else{
                JSONObject props = jsonObject.getJSONObject("props");
                props.put("perm", "H");
            }
        }
        Dict set = Dict.create()
                .set("processInstanceId", taskResultDto.getProcessInstanceId())
                .set("node", taskResultDto.getTaskNodeDto())
                .set("process",oaForms.getProcess())
                .set("delegateAgain", taskResultDto.getDelegate())
                .set("delegationTask",StrUtil.equals(taskResultDto.getDelegationState(),"PENDING"))

                .set("formItems", jsonObjectList);

        return com.cxygzl.biz.utils.R.ok(set);
    }

    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Object completeTask(TaskParamDto taskParamDto) {
        long userId = StpUtil.getLoginIdAsLong();
        taskParamDto.setUserId(String.valueOf(userId));

        Map<String, Object> paramMap = taskParamDto.getParamMap();

        Boolean appendChildProcessRootId = taskParamDto.getAppendChildProcessRootId();
        if (appendChildProcessRootId != null && appendChildProcessRootId) {



            //子流程
            String nodeId = taskParamDto.getNodeId();


            ProcessNodeData processNodeData =
                    nodeDataService.lambdaQuery().eq(ProcessNodeData::getProcessId, taskParamDto.getProcessId()).eq(ProcessNodeData::getNodeId,
                            nodeId).one();

            String data = processNodeData.getData();
            NodeDto nodeDto = JSON.parseObject(data, NodeDto.class);
            List<NodeFormMappingDto> inputFormMapping = nodeDto.getProps().getInputFormMapping();

            for (NodeFormMappingDto nodeFormMappingDto : inputFormMapping) {
                String childId = nodeFormMappingDto.getChildId();
                Object o = paramMap.get(childId);
                paramMap.put(nodeFormMappingDto.getMainId(),o);
            }


            NodeUserDto nodeUserDto = new NodeUserDto();
            nodeUserDto.setId(userId);
            paramMap.put(StrUtil.format("{}_root", nodeId), CollUtil.newArrayList(nodeUserDto));
        }


        String post =CoreHttpUtil.completeTask(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return com.cxygzl.biz.utils.R.badRequest(r.getMsg());
        }


        return com.cxygzl.biz.utils.R.ok("提交成功");
    }

    /**
     * 结束流程
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Object stopProcessInstance(TaskParamDto taskParamDto) {

        String processInstanceId = taskParamDto.getProcessInstanceId();

        List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceId);
        CollUtil.reverse(allStopProcessInstanceIdList);
        allStopProcessInstanceIdList.add(processInstanceId);

        taskParamDto.setProcessInstanceIdList(allStopProcessInstanceIdList);
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.stopProcessInstance(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return com.cxygzl.biz.utils.R.badRequest(r.getMsg());
        }


        return com.cxygzl.biz.utils.R.ok("提交成功");
    }


    private List<String> getAllStopProcessInstanceIdList(String processInstanceId){
        List<ProcessInstanceRecord> list = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getParentProcessInstanceId, processInstanceId).list();

        List<String> collect = list.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toList());

        for (ProcessInstanceRecord processInstanceRecord : list) {
            List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceRecord.getProcessInstanceId());

            collect.addAll(allStopProcessInstanceIdList);

        }
        return collect;
    }
}
