package com.cxygzl.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.expression.condition.NodeExpressionStrategyFactory;
import com.cxygzl.core.listeners.AllEventListener;
import com.cxygzl.core.listeners.ApprovalCreateListener;
import com.cxygzl.core.listeners.StarterUserTaskCreateListener;
import com.cxygzl.core.node.IDataStoreHandler;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.servicetask.*;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cxygzl.common.constants.ProcessInstanceConstant.MERGE_GATEWAY_FLAG;
import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.REJECT_TO_STARTER_NODE;
import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE;

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

            {
                //流程监听器
                EventListener eventListener = new EventListener();

                eventListener.setImplementationType("class");
                eventListener.setImplementation(AllEventListener.class.getCanonicalName());
                eventListeners.add(eventListener);
            }


        }
        process.setEventListeners(eventListeners);

        NodeUtil.addEndNode(nodeDto);


        //创建所有的节点
        buildAllNode(process, nodeDto, flowId, bpmnModel);
        //创建所有的内部节点连接线
        buildAllNodeInnerSequence(process, nodeDto, flowId);
        //创建节点间连线
        buildAllNodeOuterSequence(process, nodeDto, null);
        //处理分支和下级连线


        bpmnModel.addProcess(process);
        return bpmnModel;
    }


    /**
     * 先创建所有的节点
     *
     * @param process
     * @param nodeDto
     * @param flowId
     * @param bpmnModel
     */
    public static void buildAllNode(Process process, Node nodeDto, String flowId, BpmnModel bpmnModel) {
        if (!NodeUtil.isNode(nodeDto)) {
            return;
        }


        List<FlowElement> flowElementList = buildNode(nodeDto, flowId, process, bpmnModel);
        for (FlowElement flowElement : flowElementList) {
            if (process.getFlowElement(flowElement.getId()) == null) {
                process.addFlowElement(flowElement);
            }
        }

        //子节点
        Node children = nodeDto.getChildNode();

        if (NodeTypeEnum.getByValue(nodeDto.getType()).getBranch()) {

            //条件分支
            List<Node> branchs = nodeDto.getConditionNodes();
            for (Node branch : branchs) {
                buildAllNode(process, branch.getChildNode(),
                        flowId, bpmnModel);


            }
            if (NodeUtil.isNode(children)) {
                buildAllNode(process, children, flowId, bpmnModel);
            }

        } else {

            if (NodeUtil.isNode(children)) {
                buildAllNode(process, children, flowId, bpmnModel);
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
        if (!NodeUtil.isNode(nodeDto)) {
            return;
        }

        //画内部线
        List<SequenceFlow> flowList = buildInnerSequenceFlow(nodeDto, flowId);
        for (SequenceFlow sequenceFlow : flowList) {
            process.addFlowElement(sequenceFlow);
        }

        //子节点
        Node children = nodeDto.getChildNode();
        if (NodeTypeEnum.getByValue(nodeDto.getType()).getBranch()) {
            //条件分支
            List<Node> branchs = nodeDto.getConditionNodes();
            for (Node branch : branchs) {
                buildAllNodeInnerSequence(process, branch.getChildNode(),
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
     * @param nextId
     */
    public static void buildAllNodeOuterSequence(Process process, Node nodeDto, String nextId) {

        if (!NodeUtil.isNode(nodeDto)) {
            return;
        }

        //子节点
        Node children = nodeDto.getChildNode();
        if (NodeTypeEnum.getByValue(nodeDto.getType()).getBranch()) {
//            children = children.getChildren();
            //条件分支
            List<Node> branchs = nodeDto.getConditionNodes();
            int ord = 1;
            int size = branchs.size();
            for (Node branch : branchs) {


                buildAllNodeOuterSequence(process, branch.getChildNode(), nodeDto.getTailId());

                String expression = null;

                if (nodeDto.getType() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue() || nodeDto.getType() == NodeTypeEnum.INCLUSIVE_GATEWAY.getValue().intValue()) {
                    if (ord == size) {
                        expression = NodeExpressionStrategyFactory.handleDefaultBranch(branchs, ord - 1);
                    } else if (nodeDto.getType() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue() && ord > 1) {
                        expression = NodeExpressionStrategyFactory.handleDefaultBranch(branchs, ord - 1);
                    } else {
                        expression = NodeExpressionStrategyFactory.handle(branch);
                    }

                }


                //添加连线
                if (!NodeUtil.isNode(branch.getChildNode())) {
                    //当前分支 没有其他节点了  所有就是网关和网关后面节点直接连线

                    SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getId(), nodeDto.getTailId(),
                            expression,
                            StrUtil.format("{}->{}", nodeDto.getNodeName(), nodeDto.getNodeName())
                    );
                    process.addFlowElement(sequenceFlow);
                } else {

                    SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getId(), branch.getChildNode().getHeadId(),
                            expression,
                            StrUtil.format("{}->{}", nodeDto.getNodeName(), branch.getChildNode().getNodeName())
                    );
                    process.addFlowElement(sequenceFlow);
                }
                ord++;

            }
            //分支结尾的合并分支节点-》下一个节点
            if (children != null && StrUtil.isNotBlank(children.getHeadId()) && StrUtil.isNotBlank(nodeDto.getTailId())) {

                SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getTailId(), children.getHeadId(),
                        "",
                        StrUtil.format("{}->{}", nodeDto.getNodeName(), children.getNodeName())
                );
                process.addFlowElement(sequenceFlow);

            } else if (StrUtil.isAllNotBlank(nodeDto.getTailId(), nextId)) {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getTailId(), nextId,
                        "",
                        StrUtil.format("{}->{}", nodeDto.getNodeName(), nextId)
                );
                process.addFlowElement(sequenceFlow);
            }


            buildAllNodeOuterSequence(process, children, nextId);


        } else {
            //添加连线
            if (NodeUtil.isNode(children)) {
                List<SequenceFlow> sequenceFlowList = buildSequenceFlow(children, nodeDto, "");
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    process.addFlowElement(sequenceFlow);
                }
                buildAllNodeOuterSequence(process, children, nextId);
            } else if (nodeDto.getType() != NodeTypeEnum.END.getValue().intValue()) {
                SequenceFlow seq = buildSingleSequenceFlow(nodeDto.getTailId(), nextId, "", StrUtil.format("{}->{}",
                        nodeDto.getNodeName(), nextId));

                process.addFlowElement(seq);

            }
        }

    }


    /**
     * 构建节点
     *
     * @param node      前端传输节点
     * @param flowId
     * @param process
     * @param bpmnModel
     * @return
     */
    private static List<FlowElement> buildNode(Node node, String flowId, Process process, BpmnModel bpmnModel) {
        List<FlowElement> flowElementList = new ArrayList<>();
        if (!NodeUtil.isNode(node)) {
            return flowElementList;
        }

        //设置节点的连线头节点
        node.setHeadId(node.getId());
        //设置节点的连线尾节点
        node.setTailId(node.getId());
        node.setNodeName(StrUtil.format("{}[{}]", node.getNodeName(), RandomUtil.randomNumbers(5)));

        //存储节点数据
        IDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();
        nodeDataStoreHandler.save(flowId, node.getId(), node);

        //开始
        if (node.getType() == NodeTypeEnum.ROOT.getValue().intValue()) {
            flowElementList.addAll(buildStartNode(node));
        }

        //结束
        if (node.getType() == NodeTypeEnum.END.getValue().intValue()) {
            flowElementList.add(buildEndNode(node, false));
        }

        //审批
        if (node.getType() == NodeTypeEnum.APPROVAL.getValue().intValue()) {


            flowElementList.addAll(buildApproveNode(node));
        }

        //抄送
        if (node.getType() == NodeTypeEnum.CC.getValue().intValue()) {


            flowElementList.add(buildCCNode(node));
        }
        //触发器
        if (node.getType() == NodeTypeEnum.TRIGGER.getValue().intValue()) {


            flowElementList.add(buildTriggerNode(node));
        }
        //异步触发器
        if (node.getType() == NodeTypeEnum.ASYN_TRIGGER.getValue().intValue()) {


            flowElementList.addAll(buildAsynTriggerNode(node, bpmnModel));
        }
        //路由
        if (node.getType() == NodeTypeEnum.ROUTE.getValue().intValue()) {


            flowElementList.addAll(buildRouteNode(node));
        }
        //延时器
        if (node.getType() == NodeTypeEnum.DELAY.getValue().intValue()) {


            flowElementList.add(buildDelayNode(node));
        }
        //子流程
        if (node.getType() == NodeTypeEnum.SUB_PROCESS.getValue().intValue()) {


            flowElementList.addAll(buildCallActivityNode(node));
        }
        //条件分支
        if (node.getType() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()) {

            flowElementList.addAll(buildInclusiveGatewayNode(node));
        }
        //并行分支
        if (node.getType() == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()) {

            flowElementList.addAll(buildParallelGatewayNode(node));
        }
        //包容分支
        if (node.getType() == NodeTypeEnum.INCLUSIVE_GATEWAY.getValue().intValue()) {

            flowElementList.addAll(buildInclusiveGatewayNode(node));
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

        node.setTailId(StrUtil.format("{}_user_task", node.getId()));


        List<FlowElement> flowElementList = new ArrayList<>();


        StartEvent startEvent = new StartEvent();
        startEvent.setId(node.getId());
        startEvent.setName(node.getNodeName());

        flowElementList.add(startEvent);


        {

            FlowableListener createListener = new FlowableListener();
            createListener.setImplementation(StarterUserTaskCreateListener.class.getCanonicalName());
            createListener.setImplementationType("class");
            createListener.setEvent("create");


            Node rootUserTask = new Node();
            rootUserTask.setId(StrUtil.format("{}_user_task", node.getId()));
            rootUserTask.setNodeName("发起人");


            UserTask userTask = buildUserTask(rootUserTask, node.getId(), createListener);
//            userTask.setSkipExpression("${expressionHandler.isAllNull(rootReject)}");
            String exp = StrUtil.format("expressionHandler.isAllNotTrue(execution,\"{}\",\"{}\")", REJECT_TO_STARTER_NODE,
                    SUB_PROCESS_STARTER_NODE);

            userTask.setSkipExpression(StrUtil.format("${{}}", exp));


            {
                //发起人用户任务 用来处理驳回


                //执行人处理

                String inputDataItem = "${multiInstanceHandler.resolveStarAssignee(execution)}";


                //串行

                boolean isSequential = false;
                MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics();
                loopCharacteristics.setSequential(isSequential);
                loopCharacteristics.setInputDataItem(inputDataItem);
                loopCharacteristics.setElementVariable(StrUtil.format("{}_assignee_temp", node.getId()));
                loopCharacteristics.setCompletionCondition("${multiInstanceHandler.completionCondition(execution)}");
                userTask.setLoopCharacteristics(loopCharacteristics);
                String format = StrUtil.format("${{}_assignee_temp}", node.getId());
                userTask.setAssignee(format);
            }

            flowElementList.add(userTask);

        }


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


        node.setTailId(StrUtil.format("approve_service_task_{}", node.getId()));


        //创建了任务执行监听器
        //先执行指派人 后创建
        //https://tkjohn.github.io/flowable-userguide/#eventDispatcher
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementation(ApprovalCreateListener.class.getCanonicalName());
        createListener.setImplementationType("class");
        createListener.setEvent("create");


        UserTask userTask = buildUserTask(node, node.getId(), createListener);
        //1分钟过期
//        userTask.setDueDate(DateUtil);
        flowElementList.add(userTask);


        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(StrUtil.format("approve_service_task_{}", node.getId()));
        serviceTask.setName(StrUtil.format("{}_服务任务", node.getNodeName()));
        serviceTask.setImplementationType("class");
        serviceTask.setImplementation(ApproveServiceTask.class.getCanonicalName());
        serviceTask.setAsynchronous(false);


        serviceTask.setExtensionElements(FlowableUtils.generateFlowNodeIdExtensionMap(node.getId()));

        flowElementList.add(serviceTask);


        {


            //执行人处理

            String inputDataItem = "${multiInstanceHandler.resolveAssignee(execution)}";


            //默认并行
            boolean isSequential = false;

            Integer multipleMode = node.getMultipleMode();
            //多人
            if ((multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SAME)) {
                //并行会签
                isSequential = false;
            }
            if ((multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT)) {

                //串行会签
                isSequential = true;
            }
            if ((multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ONE)) {

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
     * 构建审批节点
     *
     * @param node
     * @return
     */
    private static IntermediateCatchEvent buildDelayNode(Node node) {


        TimerEventDefinition timerEventDefinition = new TimerEventDefinition();

        if (node.getMode()) {
            if (node.getDelayUnit().length() == 1) {
                //年月日
                timerEventDefinition.setTimeDuration(StrUtil.format("P{}{}", node.getValue().toString(), node.getDelayUnit()));
            } else {
                //时分秒
                timerEventDefinition.setTimeDuration(StrUtil.format("PT{}{}", node.getValue().toString(),
                        StrUtil.subAfter(node.getDelayUnit(), "T", true)));

            }
        } else {
            DateTime dateTime = DateUtil.parseDateTime(node.getValue().toString());
            timerEventDefinition.setTimeDate(DateUtil.format(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        }

        IntermediateCatchEvent catchEvent = new IntermediateCatchEvent();
        catchEvent.setId(node.getId());
        catchEvent.setName(node.getNodeName());
        catchEvent.addEventDefinition(timerEventDefinition);

        return catchEvent;
    }

    /**
     * 创建用户任务
     *
     * @param node      前端传输节点
     * @param oriNodeId
     * @return
     */
    private static UserTask buildUserTask(Node node, String oriNodeId, FlowableListener... flowableListeners) {
        UserTask userTask = new UserTask();
        userTask.setId(node.getId());
        userTask.setName(node.getNodeName());

        if (flowableListeners != null) {
            List<FlowableListener> taskListeners = new ArrayList<>();

            for (FlowableListener flowableListener : flowableListeners) {
                taskListeners.add(flowableListener);

            }
            userTask.setTaskListeners(taskListeners);
        }

        if (StrUtil.isNotBlank(oriNodeId)) {


            userTask.setExtensionElements(FlowableUtils.generateFlowNodeIdExtensionMap(oriNodeId));

        }

        return userTask;
    }


    /**
     * 构建并行网关
     *
     * @param node
     * @return
     */
    private static List<FlowElement> buildParallelGatewayNode(Node node) {
        node.setTailId(StrUtil.format("{}{}", node.getId(), MERGE_GATEWAY_FLAG));
        List<FlowElement> flowElementList = new ArrayList<>();

        ParallelGateway inclusiveGateway = new ParallelGateway();
        inclusiveGateway.setId(node.getId());
        inclusiveGateway.setName(node.getNodeName());
        flowElementList.add(inclusiveGateway);

        //合并网关
        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(StrUtil.format("{}{}", node.getId(), MERGE_GATEWAY_FLAG));
        parallelGateway.setName(StrUtil.format("{}_合并网关", node.getNodeName()));
        flowElementList.add(parallelGateway);

        return flowElementList;
    }

    /**
     * 构建包容网关
     *
     * @param node
     * @return
     */
    private static List<FlowElement> buildInclusiveGatewayNode(Node node) {

        node.setTailId(StrUtil.format("{}{}", node.getId(), MERGE_GATEWAY_FLAG));

        List<FlowElement> flowElementList = new ArrayList<>();

        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(node.getId());
        inclusiveGateway.setName(node.getNodeName());

        flowElementList.add(inclusiveGateway);

        //合并网关
        InclusiveGateway gateway = new InclusiveGateway();
        gateway.setId(StrUtil.format("{}{}", node.getId(), MERGE_GATEWAY_FLAG));
        gateway.setName(StrUtil.format("{}_合并网关", node.getNodeName()));

        flowElementList.add(gateway);

        return flowElementList;
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
        endEvent.setName(node.getNodeName());


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


        SequenceFlow sequenceFlow = buildSingleSequenceFlow(parentNode.getTailId(), node.getHeadId(), expression,
                StrUtil.format("{}->{}", parentNode.getNodeName(), node.getNodeName())
        );
        sequenceFlowList.add(sequenceFlow);


        return sequenceFlowList;
    }


    /**
     * 创建抄送节点
     *
     * @param node
     * @return
     */
    private static FlowElement buildCCNode(Node node) {

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setName(node.getNodeName());
        serviceTask.setAsynchronous(true);
        serviceTask.setImplementationType("class");
        serviceTask.setImplementation(CopyServiceTask.class.getCanonicalName());
        return serviceTask;
    }

    /**
     * 创建触发器节点
     *
     * @param node
     * @return
     */
    private static FlowElement buildTriggerNode(Node node) {

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setName(node.getNodeName());
        serviceTask.setImplementationType("class");
        serviceTask.setImplementation(TriggerServiceTask.class.getCanonicalName());
        serviceTask.setAsynchronous(true);
        ExtensionElement e = new ExtensionElement();
        {


            e.setName("flowable:failedJobRetryTimeCycle");
            //上面的例子会让作业执行器重试5次，并在每次重试前等待30秒。
            e.setElementText("R5/PT30S");

        }
        serviceTask.addExtensionElement(e);

        return serviceTask;
    }

    /**
     * 创建异步触发器节点
     *
     * @param node
     * @param bpmnModel
     * @return
     */
    private static List<FlowElement> buildAsynTriggerNode(Node node, BpmnModel bpmnModel) {
        node.setHeadId(StrUtil.format("asyn_trigger_service_task_{}", node.getId()));


        List<FlowElement> flowElementList = new ArrayList<>();

        String messageId = StrUtil.format("message_notify_{}", node.getId());

        Message message = new Message();
        message.setId(messageId);
        message.setName(messageId);
        bpmnModel.addMessage(message);

        {
            ServiceTask serviceTask = new ServiceTask();
            serviceTask.setId(StrUtil.format("asyn_trigger_service_task_{}", node.getId()));
            serviceTask.setName(StrUtil.format("{}_请求", node.getNodeName()));
            serviceTask.setImplementationType("class");
            serviceTask.setImplementation(AsynTriggerServiceTask.class.getCanonicalName());
            serviceTask.setAsynchronous(true);
            serviceTask.setExtensionElements(FlowableUtils.generateFlowNodeIdExtensionMap(node.getId()));

            ExtensionElement e = new ExtensionElement();
            {


                e.setName("flowable:failedJobRetryTimeCycle");
                //上面的例子会让作业执行器重试5次，并在每次重试前等待30秒。
                e.setElementText("R5/PT30S");

            }
            serviceTask.addExtensionElement(e);

            flowElementList.add(serviceTask);

        }
        {
            IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
            intermediateCatchEvent.setId(node.getId());
            intermediateCatchEvent.setName(StrUtil.format("{}_消息捕获", node.getNodeName()));
            ExtensionElement e = new ExtensionElement();
            {

                HashMap<String, List<ExtensionAttribute>> attributes = new HashMap<>();

                ArrayList<ExtensionAttribute> value1 = new ArrayList<>();
                {
                    ExtensionAttribute e1 = new ExtensionAttribute();
                    e1.setName("messageRef");
                    e1.setValue(messageId);
                    value1.add(e1);
                }

                attributes.put(IdUtil.fastSimpleUUID(), value1);
                e.setName("flowable:messageEventDefinition");

                e.setAttributes(attributes);

            }
            intermediateCatchEvent.addExtensionElement(e);
            flowElementList.add(intermediateCatchEvent);
        }


        return flowElementList;
    }

    /**
     * 创建路由节点
     *
     * @param node
     * @return
     */
    private static List<FlowElement> buildRouteNode(Node node) {
        List<FlowElement> flowElementList = new ArrayList<>();


        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setName(node.getNodeName());
        serviceTask.setImplementationType("class");
        serviceTask.setImplementation(RouteServiceTask.class.getCanonicalName());
        serviceTask.setExtensionElements(FlowableUtils.generateFlowNodeIdExtensionMap(node.getId()));

        flowElementList.add(serviceTask);
        return flowElementList;

    }

    /**
     * 构建子流程节点
     *
     * @param node
     * @return
     */
    private static List<FlowElement> buildCallActivityNode(Node node) {


        List<FlowElement> flowElementList = new ArrayList<>();

        CallActivity callActivity = new CallActivity();
        callActivity.setId(node.getId());
        callActivity.setName(node.getNodeName());
        callActivity.setCalledElement(node.getSubFlowId());
        if (node.getMultiple()) {


            Integer multipleMode = node.getMultipleMode();

            //多实例


            MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics();
            loopCharacteristics.setSequential(node.getSequential());

            if (ProcessInstanceConstant.SubProcessMultipleMode.FIX == multipleMode.intValue()) {
                loopCharacteristics.setLoopCardinality(node.getMultipleModeValue().toString());
            } else {
                String inputDataItem = StrUtil.format("${multiInstanceHandler.resolveCallActivity(execution,\"{}\")}", node.getId());
                loopCharacteristics.setInputDataItem(inputDataItem);
                loopCharacteristics.setElementVariable(StrUtil.format("{}_call_activity_temp", node.getId()));
            }
            loopCharacteristics.setCompletionCondition(StrUtil.format("${(nrOfCompletedInstances*100)>=" +
                    "(nrOfInstances*{})}", node.getCompleteRate()));

            callActivity.setLoopCharacteristics(loopCharacteristics);


        }

        HashMap<String, List<ExtensionElement>> extensionElements = new HashMap<>();
        {

            ArrayList<ExtensionElement> value = new ArrayList<>();
            {
                //发起人
                ExtensionElement e = new ExtensionElement();
                e.setName("flowable:in");
                HashMap<String, List<ExtensionAttribute>> attributes = new HashMap<>();

                ArrayList<ExtensionAttribute> value1 = new ArrayList<>();
                {
                    ExtensionAttribute e1 = new ExtensionAttribute();
                    e1.setName("target");
                    e1.setValue(ProcessInstanceConstant.VariableKey.STARTER);
                    value1.add(e1);
                }


                //同主流程
                if (node.getStarterMode() == ProcessInstanceConstant.SubProcessStarterMode.MAIN) {
                    ExtensionAttribute e1 = new ExtensionAttribute();
                    e1.setName("source");
                    e1.setValue(ProcessInstanceConstant.VariableKey.STARTER);
                    value1.add(e1);
                }


                //来自表单
                if (node.getStarterMode() == ProcessInstanceConstant.SubProcessStarterMode.FORM) {
                    ExtensionAttribute e1 = new ExtensionAttribute();
                    e1.setName("source");
                    e1.setValue(node.getStarterValue());
                    value1.add(e1);
                }

                //来自多实例表单
                if (node.getStarterMode() == ProcessInstanceConstant.SubProcessStarterMode.MULTIPLE_FORM_SINGLE) {


                    ExtensionAttribute e1 = new ExtensionAttribute();
                    e1.setName("sourceExpression");

                    e1.setValue(StrUtil.format("${expressionHandler.callActivityVariables(\"{}\",execution,1)}",
                            StrUtil.format("{}_call_activity_temp", node.getId())));


                    value1.add(e1);
                }
                attributes.put(IdUtil.fastSimpleUUID(), value1);
                e.setAttributes(attributes);
                value.add(e);
            }


            List<HttpSettingData> pcFormList = node.getPcFormList();


            //主流程向子流程传递变量
            if (CollUtil.isNotEmpty(pcFormList)) {
                for (HttpSettingData httpSettingData : pcFormList) {


                    ExtensionElement e = new ExtensionElement();
                    e.setName("flowable:in");
                    HashMap<String, List<ExtensionAttribute>> attributes = new HashMap<>();

                    ArrayList<ExtensionAttribute> value1 = new ArrayList<>();
                    {
                        ExtensionAttribute e1 = new ExtensionAttribute();
                        e1.setName("target");
                        e1.setValue(httpSettingData.getValue());
                        value1.add(e1);
                    }
                    {
                        ExtensionAttribute e1 = new ExtensionAttribute();
                        e1.setName("sourceExpression");
                        e1.setValue(StrUtil.format("${expressionHandler.callActivityVariables(\"{}\",execution,3)}",
                                httpSettingData.getField()));
                        value1.add(e1);
                    }
                    attributes.put(IdUtil.fastSimpleUUID(), value1);
                    e.setAttributes(attributes);
                    value.add(e);

                }
            }

            List<HttpSettingData> cpFormList = node.getCpFormList();

//子流程向主流程传递变量
            if (CollUtil.isNotEmpty(cpFormList) && !node.getMultiple()) {
                for (HttpSettingData httpSettingData : cpFormList) {


                    ExtensionElement e = new ExtensionElement();
                    e.setName("flowable:out");
                    HashMap<String, List<ExtensionAttribute>> attributes = new HashMap<>();

                    ArrayList<ExtensionAttribute> value1 = new ArrayList<>();
                    {
                        ExtensionAttribute e1 = new ExtensionAttribute();
                        e1.setName("target");
                        e1.setValue(httpSettingData.getValue());
                        value1.add(e1);
                    }
                    {
                        ExtensionAttribute e1 = new ExtensionAttribute();
                        e1.setName("sourceExpression");
                        e1.setValue(StrUtil.format("${expressionHandler.callActivityVariables(\"{}\",execution,3)}",
                                httpSettingData.getField()));
                        value1.add(e1);
                    }
                    attributes.put(IdUtil.fastSimpleUUID(), value1);
                    e.setAttributes(attributes);
                    value.add(e);

                }
            }

            {
                //多实例子流程 --多项表单 -- 多项表单传递给子流程单向表单
                if (node.getMultiple()) {


                    Integer multipleMode = node.getMultipleMode();
                    if (ProcessInstanceConstant.SubProcessMultipleMode.FORM_MULTIPLE == multipleMode.intValue()) {
                        if (StrUtil.isNotBlank(node.getMultipleSubFormId())) {


                            ExtensionElement e = new ExtensionElement();
                            e.setName("flowable:in");
                            HashMap<String, List<ExtensionAttribute>> attributes = new HashMap<>();

                            ArrayList<ExtensionAttribute> value1 = new ArrayList<>();
                            {
                                ExtensionAttribute e1 = new ExtensionAttribute();
                                e1.setName("target");
                                e1.setValue(node.getMultipleSubFormId());
                                value1.add(e1);
                            }
                            {
                                ExtensionAttribute e1 = new ExtensionAttribute();
                                e1.setName("sourceExpression");
                                e1.setValue(StrUtil.format("${expressionHandler.callActivityVariables(\"{}\"," +
                                                "execution,4)}",
                                        StrUtil.format("{}_call_activity_temp", node.getId())));
                                value1.add(e1);
                            }
                            attributes.put(IdUtil.fastSimpleUUID(), value1);
                            e.setAttributes(attributes);
                            value.add(e);
                        }

                    }
                }
            }

            extensionElements.put("flowable", value);
        }
        callActivity.setExtensionElements(extensionElements);

        flowElementList.add(callActivity);
        return flowElementList;
    }

    /**
     * 创建连接线
     *
     * @param node   父级节点
     * @param flowId
     * @return 所有连接线
     */
    private static List<SequenceFlow> buildInnerSequenceFlow(Node node, String flowId
    ) {


        List<SequenceFlow> sequenceFlowList = new ArrayList<>();
        if (!NodeUtil.isNode(node)) {
            return sequenceFlowList;
        }


        String nodeId = node.getId();
        if (StrUtil.hasBlank(nodeId)) {
            return sequenceFlowList;
        }
        if (node.getType() == NodeTypeEnum.APPROVAL.getValue().intValue()) {


            String gatewayId = StrUtil.format("approve_service_task_{}", nodeId);


            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeId, gatewayId, "${12==12}", null);
                sequenceFlowList.add(sequenceFlow);
            }


        }
        if (node.getType() == NodeTypeEnum.ASYN_TRIGGER.getValue().intValue()) {


            String gatewayId = StrUtil.format("asyn_trigger_service_task_{}", nodeId);


            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(gatewayId, nodeId, "${12==12}", null);
                sequenceFlowList.add(sequenceFlow);
            }


        }

        if (node.getType() == NodeTypeEnum.ROOT.getValue().intValue()) {


            SequenceFlow sequenceFlow = buildSingleSequenceFlow(node.getId(), StrUtil.format("{}_user_task",
                    node.getId()), "${12==12}", null);
            sequenceFlowList.add(sequenceFlow);


        }

        return sequenceFlowList;
    }

    /**
     * 创建单个连接线
     *
     * @param pId        父级id
     * @param childId    子级id
     * @param expression 表达式
     * @param name
     * @return
     */
    private static SequenceFlow buildSingleSequenceFlow(String pId, String childId, String expression, String name) {
        if (StrUtil.hasBlank(pId, childId)) {
            return null;
        }
        SequenceFlow sequenceFlow = new SequenceFlow(pId, childId);
        sequenceFlow.setConditionExpression(expression);
        sequenceFlow.setName(StrUtil.format("{}|{}", pId, childId));
        sequenceFlow.setName(StrUtil.format("连线[{}]", RandomUtil.randomString(5)));
        if (StrUtil.isNotBlank(name)) {
            sequenceFlow.setName(name);
        }
        sequenceFlow.setId(StrUtil.format("sq-id-{}-{}", IdUtil.fastSimpleUUID(), RandomUtil.randomInt(1, 10000000)));
        return sequenceFlow;
    }


}
