package com.cxygzl.core.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.ProcessCopyDto;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeFormPermDto;
import com.cxygzl.common.dto.process.NodePropDto;
import com.cxygzl.common.dto.process.NodeUserDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.util.List;

/**
 * 抄送任务 指定人员执行监听器
 */
@Slf4j
public class CCAssignListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        org.flowable.task.service.impl.persistence.entity.TaskEntityImpl task = (TaskEntityImpl) delegateTask;

        //执行人id
        String assignee = task.getAssignee();

        //nodeid
        String taskDefinitionKey = task.getTaskDefinitionKey();

        //实例id
        String processInstanceId = task.getProcessInstanceId();

        String processDefinitionId = delegateTask.getProcessDefinitionId();
        //流程id
        String processId = NodeUtil.getProcessId(processDefinitionId);


        //获取发起人
        Object rootObj = task.getVariable("root");
        NodeUserDto nodeUserDto = JSON.parseArray(JSON.toJSONString(rootObj), NodeUserDto.class).get(0);

        //判断是抄送 自动通过
        String s = NodeDataStoreFactory.getInstance().get(processId, taskDefinitionKey);
        NodeDto nodeDto = JSON.parseObject(s, NodeDto.class);

        //表单
        NodePropDto props = nodeDto.getProps();
        List<NodeFormPermDto> formPerms = props.getFormPerms();
        for (NodeFormPermDto formPerm : formPerms) {
            String id = formPerm.getId();
            Object variable = task.getVariable(id);
            formPerm.setValue(variable);
        }

        ProcessCopyDto processCopyDto = new ProcessCopyDto();
        processCopyDto.setNodeTime(task.getCreateTime());
        processCopyDto.setStartUserId(nodeUserDto.getId());
        processCopyDto.setProcessId(processId);
        processCopyDto.setProcessInstanceId(processInstanceId);
        processCopyDto.setNodeId(taskDefinitionKey);
        processCopyDto.setNodeName(task.getName());
        processCopyDto.setFormData(JSON.toJSONString(formPerms));
        processCopyDto.setUserId(Convert.toLong(assignee));


        org.flowable.engine.TaskService taskService = SpringUtil.getBean(org.flowable.engine.TaskService.class);
        taskService.complete(task.getId());

        CoreHttpUtil.saveCC(processCopyDto);


    }
}
