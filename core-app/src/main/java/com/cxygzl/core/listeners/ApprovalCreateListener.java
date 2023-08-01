package com.cxygzl.core.listeners;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Nobody;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.APPROVE_NODE_RESULT;

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
        String flowId = NodeUtil.getFlowId(processDefinitionId);

        if (StrUtil.isBlank(assignee) || StrUtil.equals(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN, assignee)) {

            Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);

            Nobody nobody = node.getNobody();

            String handler = nobody.getHandler();
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_PASS)) {
                //直接通过

                taskService.complete(taskEntity.getId());
            }
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_ADMIN)) {
                //指派给管理员

                R<String> longR  = CoreHttpUtil.queryProcessAdmin(flowId);

                String adminId = longR.getData();


                taskService.setAssignee(taskEntity.getId(), String.valueOf(adminId));
            }
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_USER)) {
                //指定用户

                NodeUser nodeUser = nobody.getAssignedUser().get(0);


                taskService.setAssignee(taskEntity.getId(), nodeUser.getId());
            }
            if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_REFUSE)) {
                //自动拒绝

                Dict dict = Dict.create().set(StrUtil.format("{}_{}", node.getId(), APPROVE_NODE_RESULT),
                        ProcessInstanceConstant.ApproveNodeResult.REFUSE);
                taskService.complete(taskEntity.getId(),dict);


            }

        }

    }
}
