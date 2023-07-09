package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessNodeRecordAssignUser;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskParamDto;
import com.cxygzl.common.dto.TaskResultDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
     * @param view
     * @return
     */
    @Override
    public Object queryTask(String taskId, boolean view) {


        long userId = StpUtil.getLoginIdAsLong();


        com.cxygzl.common.dto.R<TaskResultDto> r = CoreHttpUtil.queryTask(taskId,userId);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }

        TaskResultDto taskResultDto = r.getData();

        //变量
        Map<String, Object> paramMap=taskResultDto.getVariableAll();
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

            if(processNodeRecordAssignUser!=null){
                String data = processNodeRecordAssignUser.getData();
                if(StrUtil.isNotBlank(data)){
                    Map<String, Object> collect = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                    });
                    paramMap.putAll(collect);

                }
            }

        }else{

        }

        //当前节点数据
        String nodeDataJson =
                nodeDataService.getNodeData(taskResultDto.getFlowId(), taskResultDto.getNodeId()).getData();
        Node node = CommonUtil.toObj(nodeDataJson, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        Process oaForms = processService.getByFlowId(taskResultDto.getFlowId());
        if (oaForms == null) {
            return  R.fail("流程不存在");
        }

        List<FormItemVO> formItemVOList = CommonUtil.toArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {



            String id = formItemVO.getId();

            String perm = formPerms.get(id);


            if (StrUtil.isNotBlank(perm)) {

                formItemVO.setPerm(view?(ProcessInstanceConstant.FormPermClass.EDIT.equals(perm)?ProcessInstanceConstant.FormPermClass.READ:perm):perm);

            }else{
              formItemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
            }

            if(formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())){
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(paramMap, id, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l=new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        itemVO.getProps().setValue(map.get(itemVO.getId()));

                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm(view?(ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub)?ProcessInstanceConstant.FormPermClass.READ:permSub)
                                    :permSub
                                    );


                        }else{
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

                        }else{
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    formItemVO.getProps().setOriForm(subItemList);

                }

            }else{
                formItemVO.getProps().setValue(paramMap.get(id));

            }

        }
        Dict set = Dict.create()
                .set("processInstanceId", taskResultDto.getProcessInstanceId())
                .set("node", taskResultDto.getTaskNode())
                .set("process",oaForms.getProcess())
                .set("delegateAgain", taskResultDto.getDelegate())
                .set("delegationTask",StrUtil.equals(taskResultDto.getDelegationState(),"PENDING"))

                .set("formItems", formItemVOList);

        return R.success(set);
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


        com.cxygzl.common.dto.R r =CoreHttpUtil.completeTask(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 前加签
     *
     * @param taskParamDto
     * @return
     */
    @Transactional
    @Override
    public Object delegateTask(TaskParamDto taskParamDto) {


        taskParamDto.setUserId(StpUtil.getLoginIdAsString());

        String post =CoreHttpUtil.delegateTask(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return  R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 加签完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Object resolveTask(TaskParamDto taskParamDto) {
        String post = CoreHttpUtil.resolveTask(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return  R.fail(r.getMsg());
        }


        return  R.success();
    }

    /**
     * 设置执行人
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Object setAssignee(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.setAssignee(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return  R.fail(r.getMsg());
        }


        return  R.success();
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
        com.cxygzl.common.dto.R r= CoreHttpUtil.stopProcessInstance(taskParamDto);

        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }


        return R.success();
    }

    /**
     * 退回
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Object back(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(StpUtil.getLoginIdAsString());
        String post = CoreHttpUtil.back(taskParamDto);
        com.cxygzl.common.dto.R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isOk()) {
            return  R.fail(r.getMsg());
        }


        return R.success();
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
