package com.cxygzl.core.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.DeptDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.process.*;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("multiInstanceHandler")
@Slf4j
public class MultiInstanceHandler {
    @Resource
    private RuntimeService runtimeService;

    /**
     * 处理执行人
     *
     * @param execution
     * @return
     */
    public List<String> resolveAssignee(DelegateExecution execution) {
        //执行人集合
        List<String> assignList=new ArrayList<>();

        INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        String processId = entity.getProcessDefinitionKey();
        String nodeId = entity.getActivityId();

        log.debug("nodeId={} nodeName={}",nodeId,entity.getActivityName());

        //发起人
        Object rootUserObj = execution.getVariable("root");
        NodeUserDto rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj),NodeUserDto.class).get(0);

        //节点数据
        String nodeData = nodeDataStoreHandler.get(processId, nodeId);
        if (StrUtil.isNotBlank(nodeData)) {
            NodeDto nodeDto = JSON.parseObject(nodeData, NodeDto.class);


            NodePropDto props = nodeDto.getProps();

            if (StrUtil.equals(props.getAssignedType(), "ASSIGN_USER")||(StrUtil.equals(nodeDto.getType(), NodeTypeEnum.CC.getKey()))) {
                //指定人员
                List<NodeUserDto> userDtoList = props.getAssignedUser();
                //用户id
                List<String> userIdList = userDtoList.stream().filter(w->StrUtil.equals(w.getType(),NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());
                //部门id
                List<String> deptIdList = userDtoList.stream().filter(w->StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

                if(CollUtil.isNotEmpty(deptIdList)){

                    String s = CoreHttpUtil.queryUserIdListByDepIdList(deptIdList);
                    R<List<String>> r = JSON.parseObject(s, new TypeReference<R<List<String>>>() {
                    });
                    List<String> data = r.getData();
                    if(CollUtil.isNotEmpty(data)){
                        for (String datum : data) {
                            if(!userIdList.contains(datum)){
                                userIdList.add(datum);
                            }
                        }
                    }
                }

                assignList.addAll(userIdList);

//                return userIdList;
            } else if (StrUtil.equals(props.getAssignedType(), "SELF")) {
                //发起人自己


                List<String> userIdList = CollUtil.newArrayList(String.valueOf(rootUser.getId()));
                assignList.addAll(userIdList);

//                return userIdList;
            } else if (StrUtil.equals(props.getAssignedType(), "FORM_USER")) {
                //表单值

                Object variable = execution.getVariable(props.getFormUser());
                if (variable == null) {

                }else   if (StrUtil.isBlankIfStr(variable)) {

                }else{

                    String jsonString = JSON.toJSONString(variable);
                    List<NodeUserDto> nodeUserDtoList = JSON.parseArray(jsonString, NodeUserDto.class);

                    List<String> userIdList = nodeUserDtoList.stream().map(w->String.valueOf(w.getId())).collect(Collectors.toList());

                    assignList.addAll(userIdList);

                }

            } else if (StrUtil.equals(props.getAssignedType(), "LEADER")) {
                //指定主管审批
                NodeLeaderDto leader = props.getLeader();
                //第几级主管审批
                Integer level = leader.getLevel();

                //去获取主管

                String s = CoreHttpUtil.queryParentDepListByUserId(rootUser.getId());
                R<List<DeptDto>> r = JSON.parseObject(s, new TypeReference<R<List<DeptDto>>>() {
                });
                List<DeptDto> deptDtoList = r.getData();
                if (CollUtil.isNotEmpty(deptDtoList)) {
                    if (deptDtoList.size() >= level) {
                        DeptDto deptDto = deptDtoList.get(level - 1);
//                        return CollUtil.newArrayList(String.valueOf(deptDto.getLeaderUserId()));

                        assignList.add(String.valueOf(deptDto.getLeaderUserId()));
                    }
                }



            } else if (StrUtil.equals(props.getAssignedType(), "LEADER_TOP")) {

                //去获取主管

                String s = CoreHttpUtil.queryParentDepListByUserId(rootUser.getId());
                R<List<DeptDto>> r = JSON.parseObject(s, new TypeReference<R<List<DeptDto>>>() {
                });
                List<DeptDto> deptDtoList = r.getData();

                //上级主管依次审批
                NodeLeaderTopDto leaderTop = props.getLeaderTop();
                //第几级主管审批截止
                Integer level = leaderTop.getLevel();


                if (CollUtil.isNotEmpty(deptDtoList)) {
                    int index=1;
                    for (DeptDto deptDto : deptDtoList) {
                        if(level!=null&&level<index){
                            break;
                        }
                        assignList.add(String.valueOf(deptDto.getLeaderUserId()));
                        index++;
                    }
                }


            }else if (StrUtil.equals(props.getAssignedType(), "SELF_SELECT")) {
                //发起人自选
                Object variable = execution.getVariable(StrUtil.format("{}_assignee_select", nodeId));
                log.info("{}-发起人自选参数:{}",nodeDto.getName(),variable);
                List<NodeUserDto> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUserDto.class);

                List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

                assignList.addAll(collect);

            }

        }else{
            //默认值
            String format = StrUtil.format("{}_assignee_default_list", nodeId);
            Object variable = execution.getVariable(format);
            List<NodeUserDto> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUserDto.class);
            if(CollUtil.isNotEmpty(nodeUserDtos)){
                List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

                assignList.addAll(collect);
            }

        }

        if(CollUtil.isEmpty(assignList)){
            assignList.add(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN);
        }

        return assignList;

    }

    /**
     * 处理审批人为空的问题
     * @param assignList
     * @param taskId
     * @param nodeDto
     */
    public static void emptyAssignHandler(List<String> assignList,String taskId, NodeDto nodeDto){
        if(CollUtil.isNotEmpty(assignList)){
            return;
        }
        NodePropDto props = nodeDto.getProps();
        NodeNoBodyDto nobody = props.getNobody();
        String handler = nobody.getHandler();
        if(StrUtil.equals(handler,"TO_PASS")){
            //自动通过
            SpringUtil.getBean(TaskService.class).complete(taskId);
        }
    }

    /**
     * 会签或者或签完成条件检查
     *
     * @param execution
     */
    public boolean completionCondition(DelegateExecution execution) {

        ExecutionEntityImpl entity= (ExecutionEntityImpl) execution;
        String processDefinitionKey = entity.getProcessDefinitionKey();

        String nodeId = execution.getCurrentActivityId();

        String s = NodeDataStoreFactory.getInstance().get(processDefinitionKey, nodeId);
        NodeDto nodeDto = JSON.parseObject(s, NodeDto.class);


        NodePropDto props = nodeDto.getProps();
        String mode = props.getMode();
        BigDecimal modePercentage = props.getModePercentage();


        Object variable = execution.getVariable(StrUtil.format("{}_approve_condition", nodeId));
        log.debug("当前节点审批结果：{}",variable);
        Boolean approve = Convert.toBool(variable);
        if(StrUtil.equalsAny(mode,"AND","NEXT")){
            //会签或者顺序签署
            if(!approve){
                return true;
            }
        }
        if(StrUtil.equals(mode,"OR")){
            //或签
            if(approve){
                return true;
            }
        }

        //实例总数
        int nrOfInstances = (int) execution.getVariable("nrOfInstances");
        //完成的实例数
        int nrOfCompletedInstances = (int) execution.getVariable("nrOfCompletedInstances");
        log.debug("当前节点完成实例数：{}  总实例数:{} 需要完成比例:{}",nrOfCompletedInstances,nrOfInstances,modePercentage);
        if(StrUtil.equalsAny(mode,"AND")){
            //会签判断完成比例
            if(modePercentage!=null){
               if( Convert.toBigDecimal(nrOfCompletedInstances*100).compareTo(Convert.toBigDecimal(nrOfCompletedInstances).multiply(modePercentage))>0){
                   return true;
               }else{
                   return false;
               }
            }
        }
        if (nrOfCompletedInstances == nrOfInstances) {
            return true;
        }
        return false;
    }
}
