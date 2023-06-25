package com.cxygzl.core.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.expression.condition.NodeExpressionStrategyFactory;
import com.cxygzl.core.listeners.ApprovalCreateListener;
import com.cxygzl.core.listeners.FlowProcessEventListener;
import com.cxygzl.core.node.INodeDataStoreHandler;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.servicetask.CopyServiceTask;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型工具类 处理模型构建相关的
 */
@Slf4j
public class ModelUtil {
    /**
     * 构建模型
     *
     * @param nodeDto 前端传输节点
     * @return
     */
    public static BpmnModel buildBpmnModel(Node nodeDto, String processName, String flowId) {
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.setTargetNamespace("cxygzl");

        Process process = new Process();
        process.setId(flowId);
        process.setName(processName);

        //流程监听器
        ArrayList<EventListener> eventListeners = new ArrayList<>();

        {
            //流程实例监听器
            EventListener eventListener = new EventListener();

            eventListener.setImplementationType("class");
            eventListener.setImplementation(FlowProcessEventListener.class.getCanonicalName());


            eventListeners.add(eventListener);

        }
        process.setEventListeners(eventListeners);

        //构建结束节点

        Node endNodeDto = new Node();
        endNodeDto.setId(StrUtil.format("end"));
        endNodeDto.setHeadId(endNodeDto.getId());
        endNodeDto.setName("endNode");

        //构建节点
        EndEvent endEvent = buildEndNode(endNodeDto, false);
        process.addFlowElement(endEvent);
        //创建所有的节点
        buildAllNode(process, nodeDto, flowId);
        //创建所有的内部节点连接线
        buildAllNodeInnerSequence(process, nodeDto, flowId);
        //创建节点间连线
        buildAllNodeOuterSequence(process, nodeDto, endNodeDto);


        bpmnModel.addProcess(process);
        return bpmnModel;
    }

    /**
     * 先创建所有的节点
     *
     * @param process
     * @param nodeDto
     * @param flowId
     */
    public static void buildAllNode(Process process, Node nodeDto, String flowId) {
        List<FlowElement> flowElementList = buildNode(nodeDto, flowId);
        for (FlowElement flowElement : flowElementList) {
            if (process.getFlowElement(flowElement.getId()) == null) {
                process.addFlowElement(flowElement);
            }
        }

        //子节点
        Node children = nodeDto.getChildren();
        if (nodeDto.getType()== NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue()) {

            //条件分支
            List<Node> branchs = nodeDto.getConditionNodes();
            for (Node branch : branchs) {
                buildAllNode(process, branch.getChildren(),
                        flowId);


            }
            if (NodeUtil.isNode(children)) {
                buildAllNode(process, children, flowId);
            }

        } else {

            if (NodeUtil.isNode(children)) {
                buildAllNode(process, children, flowId);
            }
        }


    }

    /**
     * 先创建所有的内部节点连接线
     *
     * @param process
     * @param nodeDto
     * @param flowId
     */
    public static void buildAllNodeInnerSequence(Process process, Node nodeDto, String flowId) {

        //画内部线
        List<SequenceFlow> flowList = buildInnerSequenceFlow(nodeDto, flowId);
        for (SequenceFlow sequenceFlow : flowList) {
            process.addFlowElement(sequenceFlow);
        }

        //子节点
        Node children = nodeDto.getChildren();
        if (nodeDto.getType()== NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()) {
            //条件分支
            List<Node> branchs = nodeDto.getConditionNodes();
            for (Node branch : branchs) {
                buildAllNodeInnerSequence(process, branch.getChildren(),
                        flowId);


            }
            if (NodeUtil.isNode(children)) {
                buildAllNodeInnerSequence(process, children, flowId);
            }

        } else {

            if (NodeUtil.isNode(children)) {
                buildAllNodeInnerSequence(process, children, flowId);
            }
        }


    }


    /**
     * 递归创建节点间连线
     *
     * @param process 流程
     * @param nodeDto 节点对象
     */
    public static void buildAllNodeOuterSequence(Process process, Node nodeDto, Node nextNodeDto) {


        //子节点
        Node children = nodeDto.getChildren();
        if (nodeDto.getType()== NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()) {
//            children = children.getChildren();
            //条件分支
            List<Node> branchs = nodeDto.getConditionNodes();
            int ord = 1;
            int size = branchs.size();
            for (Node branch : branchs) {
                buildAllNodeOuterSequence(process, branch.getChildren(), NodeUtil.isNode(children) ? children : nextNodeDto );

                String expression = null;

                if (ord == size ) {
                    expression = NodeExpressionStrategyFactory.handleDefaultBranch(branchs);
                } else {
                    expression = NodeExpressionStrategyFactory.handle(branch);
                }


                //添加连线
                if (!NodeUtil.isNode(branch.getChildren())) {
                    //当前分支 没有其他节点了  所有就是网关和网关后面节点直接连线
                    List<SequenceFlow> sequenceFlowList = buildSequenceFlow(NodeUtil.isNode(children) ? children : nextNodeDto,
                            nodeDto, expression);
                    for (SequenceFlow sequenceFlow : sequenceFlowList) {
                        process.addFlowElement(sequenceFlow);
                    }
                } else {
                    //当前节点 就是网关和第一个节点（非EMPTY节点）连线
                    List<SequenceFlow> sequenceFlowList = buildSequenceFlow(branch.getChildren(), nodeDto, expression);
                    for (SequenceFlow sequenceFlow : sequenceFlowList) {
                        process.addFlowElement(sequenceFlow);
                    }
                }
                ord++;

            }
            if (NodeUtil.isNode(children)) {
                buildAllNodeOuterSequence(process, children, nextNodeDto);
            }

        } else {
            //添加连线
            List<SequenceFlow> sequenceFlowList = buildSequenceFlow(children, nodeDto, "");
            for (SequenceFlow sequenceFlow : sequenceFlowList) {
                process.addFlowElement(sequenceFlow);
            }
            if (NodeUtil.isNode(children)) {
                buildAllNodeOuterSequence(process, children, nextNodeDto);
            } else {
                List<SequenceFlow> sequenceFlowList1 = buildSequenceFlow(nextNodeDto, nodeDto, "");
                for (SequenceFlow sequenceFlow : sequenceFlowList1) {
                    process.addFlowElement(sequenceFlow);
                }
            }
        }

    }


    /**
     * 构建节点
     *
     * @param node      前端传输节点
     * @param flowId
     * @return
     */
    private static List<FlowElement> buildNode(Node node, String flowId) {
        List<FlowElement> flowElementList = new ArrayList<>();

        //设置节点的连线头节点
        node.setHeadId(node.getId());
        //设置节点的连线尾节点
        node.setTailId(node.getId());
        node.setName(StrUtil.format("{}[{}]", node.getName(), RandomUtil.randomNumbers(5)));

        //存储节点数据
        INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();
        nodeDataStoreHandler.save(flowId, node.getId(), node);

        //开始
        if (node.getType()== NodeTypeEnum.ROOT.getValue().intValue()) {
            flowElementList.addAll(buildStartNode(node));
        }

        //审批
        if (node.getType()== NodeTypeEnum.APPROVAL.getValue().intValue()) {


            flowElementList.addAll(buildApproveNode(node));
        }

        //抄送
        if (node.getType()== NodeTypeEnum.CC.getValue().intValue()) {


            flowElementList.add(buildCCNode(node));
        }
        //条件分支
        if (node.getType()== NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()) {

            flowElementList.add(buildExclusiveGatewayNode(node));
        }
        return flowElementList;
    }

    /**
     * 构建开始节点
     * 添加一个自动完成任务的用户任务节点
     *
     * @param node 前端传输节点
     * @return
     */
    private static List<FlowElement> buildStartNode(Node node) {

        StartEvent startEvent = new StartEvent();
        startEvent.setId(node.getId());
        startEvent.setName(node.getName());

        List<FlowElement> flowElementList = new ArrayList<>();
        flowElementList.add(startEvent);

        return flowElementList;
    }

    /**
     * 构建审批节点
     *
     * @param node
     * @return
     */
    private static List<FlowElement> buildApproveNode(Node node) {
        List<FlowElement> flowElementList = new ArrayList<>();


        node.setTailId(StrUtil.format("approve_gateway_{}", node.getId()));


        //创建了任务执行监听器
        //先执行指派人 后创建
        //https://tkjohn.github.io/flowable-userguide/#eventDispatcher
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementation(ApprovalCreateListener.class.getCanonicalName());
        createListener.setImplementationType("class");
        createListener.setEvent("create");


        UserTask userTask = buildUserTask(node, createListener);
        flowElementList.add(userTask);

        Node exclusiveNode = new Node();
        exclusiveNode.setId(StrUtil.format("approve_gateway_{}", node.getId()));
        exclusiveNode.setName("审批-排他网关");
        flowElementList.add(buildExclusiveGatewayNode(exclusiveNode));
        //创建结束节点

        Node endNode = new Node();
        endNode.setId(StrUtil.format("approve_end_{}", node.getId()));
        endNode.setName("审批-结束节点");
        EndEvent endEvent = buildEndNode(endNode, false);
        flowElementList.add(endEvent);





        {


            //执行人处理

            String inputDataItem = "${multiInstanceHandler.resolveAssignee(execution)}";


            //串行

            boolean isSequential = true;

            Integer multipleMode = node.getMultipleMode();
            //多人
            if ((multipleMode== ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME)) {
                //并行会签
                isSequential = false;
            }
            if ((multipleMode==ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT)) {

                //串行会签
            }
            if ((multipleMode==ProcessInstanceConstant.MULTIPLE_MODE_ONE)) {

                //或签
                isSequential = false;
            }

            MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics();
            loopCharacteristics.setSequential(isSequential);
            loopCharacteristics.setInputDataItem(inputDataItem);
            loopCharacteristics.setElementVariable(StrUtil.format("{}_assignee_temp", node.getId()));

            loopCharacteristics.setCompletionCondition("${multiInstanceHandler.completionCondition(execution)}");

            userTask.setLoopCharacteristics(loopCharacteristics);
            String format = StrUtil.format("${{}_assignee_temp}", node.getId());
            userTask.setAssignee(format);

        }
        return flowElementList;
    }

    /**
     * 创建用户任务
     *
     * @param node 前端传输节点
     * @return
     */
    private static UserTask buildUserTask(Node node, FlowableListener... flowableListeners) {
        UserTask userTask = new UserTask();
        userTask.setId(node.getId());
        userTask.setName(node.getName());

        if (flowableListeners != null) {
            List<FlowableListener> taskListeners = new ArrayList<>();

            for (FlowableListener flowableListener : flowableListeners) {
                taskListeners.add(flowableListener);

            }
            userTask.setTaskListeners(taskListeners);
        }

        return userTask;
    }

    /**
     * 构建排他网关
     *
     * @param node
     * @return
     */
    private static FlowElement buildExclusiveGatewayNode(Node node) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(node.getId());
        exclusiveGateway.setName(node.getName());
        return exclusiveGateway;
    }


    /**
     * 构建结束节点
     *
     * @param node         前端传输节点
     * @param terminateAll
     * @return
     */
    private static EndEvent buildEndNode(Node node, boolean terminateAll) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(node.getId());
        endEvent.setName(node.getName());


        List<EventDefinition> definitionList = new ArrayList<>();
        TerminateEventDefinition definition = new TerminateEventDefinition();
        definition.setTerminateAll(terminateAll);
        definitionList.add(definition);
        endEvent.setEventDefinitions(definitionList);

        return endEvent;
    }

    /**
     * 创建连接线
     *
     * @param node       子级节点
     * @param parentNode 父级节点
     * @param expression
     * @return 所有连接线
     */
    private static List<SequenceFlow> buildSequenceFlow(Node node, Node parentNode, String expression
    ) {
        List<SequenceFlow> sequenceFlowList = new ArrayList<>();
        //没有子级了
        if (!NodeUtil.isNode(node)) {
            return sequenceFlowList;
        }

        String pid = parentNode.getId();

        if (StrUtil.hasBlank(pid, node.getId())) {
            return sequenceFlowList;
        }


        SequenceFlow sequenceFlow = buildSingleSequenceFlow(parentNode.getTailId(), node.getHeadId(), expression);
        sequenceFlowList.add(sequenceFlow);


        return sequenceFlowList;
    }
    /**
     * 生成扩展数据
     *
     * @param key
     * @param val
     * @return
     */
    public static ExtensionAttribute generateExtensionAttribute(String key, String val) {
        ExtensionAttribute ea = new ExtensionAttribute();

        ea.setName(key);
        ea.setValue(val);
        return ea;
    }
    /**
     * 创建抄送节点
     *
     * @param node
     * @return
     */
    private static FlowElement buildCCNode(Node node) {

        ServiceTask serviceTask=new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setName(node.getName());
        serviceTask.setImplementationType("class");
        serviceTask.setImplementation(CopyServiceTask.class.getCanonicalName());
        return serviceTask;
    }

    /**
     * 创建连接线
     *
     * @param node      父级节点
     * @param flowId
     * @return 所有连接线
     */
    private static List<SequenceFlow> buildInnerSequenceFlow(Node node, String flowId
    ) {


        List<SequenceFlow> sequenceFlowList = new ArrayList<>();


        String nodeId = node.getId();
        if (StrUtil.hasBlank(nodeId)) {
            return sequenceFlowList;
        }
        boolean parentIsGateway = false;
        Node prevNode = null;
        {
            //判断父级是否是包容或者并行网关
            Node parentNodeDto = NodeDataStoreFactory.getInstance().getNode(flowId, node.getParentId());
            if (parentNodeDto != null) {
                prevNode = NodeDataStoreFactory.getInstance().getNode(flowId, parentNodeDto.getParentId());

                //父级是否是网关
//    TODO            parentIsGateway = prevNode == null ? false : StrUtil.equalsAny(prevNode.getType(), NodeTypeEnum
//    .CONCURRENTS.getKey(), NodeTypeEnum.INCLUSIVES.getKey());
                parentIsGateway = prevNode == null ? false : false;

            }

        }


        if (parentIsGateway) {
            String headId = node.getHeadId();
            node.setHeadId(StrUtil.format("{}_merge_gateway", prevNode.getId()));
            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(node.getHeadId(), headId, "${12==12}");
                sequenceFlowList.add(sequenceFlow);
            }

        }


//   TODO     if (StrUtil.equals(node.getType(), NodeTypeEnum.TRIGGER.getKey())) {
        if (false) {


            String triggerGatewayId = StrUtil.format("trigger_gateway_{}", nodeId);
            String triggerGatewayEndId = StrUtil.format("trigger_gateway_end_{}", nodeId);

            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeId, triggerGatewayId, "${12==12}");
                sequenceFlowList.add(sequenceFlow);
            }

            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(triggerGatewayId, triggerGatewayEndId, StrUtil.format("${!" +
                        "{}_trigger_gateway_condition}", nodeId));
                sequenceFlowList.add(sequenceFlow);
            }

        } else if (node.getType()==NodeTypeEnum.APPROVAL.getValue().intValue()) {


            String gatewayId = StrUtil.format("approve_gateway_{}", nodeId);
            String endId = StrUtil.format("approve_end_{}", nodeId);

            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeId, gatewayId, "${12==12}");
                sequenceFlowList.add(sequenceFlow);
            }

            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(gatewayId, endId, StrUtil.format("${!" +
                        "{}_approve_condition}", nodeId));
                sequenceFlowList.add(sequenceFlow);
            }

        }
//        else if (node instanceof StartNode) {
//            //子流程
//
//
//            String userTaskId = StrUtil.format("{}_start_user_task", nodeId);
//
//
//            {
//                SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeId, userTaskId, "${12==12}");
//                sequenceFlowList.add(sequenceFlow);
//            }
//
//
//        }

        return sequenceFlowList;
    }

    /**
     * 创建单个连接线
     *
     * @param pId        父级id
     * @param childId    子级id
     * @param expression 表达式
     * @return
     */
    private static SequenceFlow buildSingleSequenceFlow(String pId, String childId, String expression) {
        SequenceFlow sequenceFlow = new SequenceFlow(pId, childId);
        sequenceFlow.setConditionExpression(expression);
        sequenceFlow.setName(StrUtil.format("{}|{}", pId, childId));
        sequenceFlow.setId(StrUtil.format("sq-id-{}-{}", IdUtil.fastSimpleUUID(), RandomUtil.randomInt(1, 10000000)));
        return sequenceFlow;
    }


}
