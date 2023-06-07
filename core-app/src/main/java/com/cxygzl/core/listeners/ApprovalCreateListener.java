package com.cxygzl.core.listeners;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.process.*;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

/**
 * 审批节点
 */
@Slf4j
public class ApprovalCreateListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug(delegateTask.getClass().getCanonicalName());
        TaskService taskService = SpringUtil.getBean(TaskService.class);

        String assignee = delegateTask.getAssignee();
        String name = delegateTask.getName();
        log.debug("任务{}-执行人:{}", name, assignee);
        String processInstanceId = delegateTask.getProcessInstanceId();
        org.flowable.task.service.impl.persistence.entity.TaskEntityImpl taskEntity = (TaskEntityImpl) delegateTask;
        String nodeId = taskEntity.getTaskDefinitionKey();
        String processDefinitionId = taskEntity.getProcessDefinitionId();
        //流程id
        String processId = NodeUtil.getProcessId(processDefinitionId);

        if (StrUtil.isBlank(assignee) || StrUtil.equals(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN, assignee)) {

            String s = NodeDataStoreFactory.getInstance().get(processId, nodeId);
            NodeDto nodeDto = JSON.parseObject(s, NodeDto.class);

            NodePropDto props = nodeDto.getProps();
            NodeNoBodyDto nobody = props.getNobody();
            String handler = nobody.getHandler();
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_PASS)) {
                //直接通过
                Dict param = Dict.create().set(StrUtil.format("{}_approve_condition", nodeId), true);
                taskService.complete(taskEntity.getId(), param);
            }
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_ADMIN)) {
                //指派给管理员

                String s1 = CoreHttpUtil.queryProcessAdmin(processId);
                R<Long> longR = JSON.parseObject(s1, new TypeReference<R<Long>>() {
                });
                Long adminId = longR.getData();


                taskService.setAssignee(taskEntity.getId(), String.valueOf(adminId));
            }
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_USER)) {
                //指定用户

                NodeUserDto nodeUserDto = nobody.getAssignedUser().get(0);


                taskService.setAssignee(taskEntity.getId(), String.valueOf(nodeUserDto.getId()));
            }
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_END)) {
                //结束
                Dict param = Dict.create().set(StrUtil.format("{}_approve_condition", nodeId), false);
                taskService.complete(taskEntity.getId(), param);

            }

        }

    }
}
