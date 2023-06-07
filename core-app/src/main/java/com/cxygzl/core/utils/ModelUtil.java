package com.cxygzl.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodePropDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.expression.condition.NodeExpressionStrategyFactory;
import com.cxygzl.core.listeners.*;
import com.cxygzl.core.node.INodeDataStoreHandler;
import com.cxygzl.core.node.NodeDataStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.time.format.DateTimeFormatter;
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
    public static BpmnModel buildBpmnModel(NodeDto nodeDto, String processName, String processId) {
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.setTargetNamespace("cxygjz");

        Process process = new Process();
        process.setId(processId);
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

        NodeDto endNodeDto = new NodeDto();
        endNodeDto.setId(StrUtil.format("root_end"));
        endNodeDto.setHeadId(endNodeDto.getId());
        endNodeDto.setName("endNode");

        //构建节点
        EndEvent endEvent = buildEndNode(endNodeDto, false);
        process.addFlowElement(endEvent);
        //创建所有的节点
        buildAllNode(process, nodeDto, processId);
        //创建所有的内部节点连接线
        buildAllNodeInnerSequence(process, nodeDto, processId);
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
     * @param processId
     */
    public static void buildAllNode(Process process, NodeDto nodeDto, String processId) {
        List<FlowElement> flowElementList = buildNode(nodeDto, processId);
        for (FlowElement flowElement : flowElementList) {
            if (process.getFlowElement(flowElement.getId()) == null) {
                process.addFlowElement(flowElement);
            }
        }

        //子节点
        NodeDto children = nodeDto.getChildren();
        if (StrUtil.equalsAny(nodeDto.getType(), NodeTypeEnum.CONDITIONS.getKey(), NodeTypeEnum.INCLUSIVES.getKey(), NodeTypeEnum.CONCURRENTS.getKey())) {
//            {
//                //存储EMPTY节点数据
//                INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();
//                nodeDataStoreHandler.save(processId, children.getId(), JSON.toJSONString(children));
//            }
//            children = children.getChildren();
            //条件分支
            List<NodeDto> branchs = nodeDto.getBranchs();
            for (NodeDto branch : branchs) {
                buildAllNode(process, branch.getChildren(),
                        processId);


            }
            if (NodeUtil.isNode(children)) {
                buildAllNode(process, children, processId);
            }

        } else {

            if (NodeUtil.isNode(children)) {
                buildAllNode(process, children, processId);
            }
        }


    }

    /**
     * 先创建所有的内部节点连接线
     *
     * @param process
     * @param nodeDto
     * @param processId
     */
    public static void buildAllNodeInnerSequence(Process process, NodeDto nodeDto,String processId) {

        //画内部线
        List<SequenceFlow> flowList = buildInnerSequenceFlow(nodeDto, processId);
        for (SequenceFlow sequenceFlow : flowList) {
            process.addFlowElement(sequenceFlow);
        }

        //子节点
        NodeDto children = nodeDto.getChildren();
        if (StrUtil.equalsAny(nodeDto.getType(), NodeTypeEnum.CONDITIONS.getKey(), NodeTypeEnum.INCLUSIVES.getKey(), NodeTypeEnum.CONCURRENTS.getKey())) {
            children = children.getChildren();
            //条件分支
            List<NodeDto> branchs = nodeDto.getBranchs();
            for (NodeDto branch : branchs) {
                buildAllNodeInnerSequence(process, branch.getChildren(),
                        processId);


            }
            if (NodeUtil.isNode(children)) {
                buildAllNodeInnerSequence(process, children, processId);
            }

        } else {

            if (NodeUtil.isNode(children)) {
                buildAllNodeInnerSequence(process, children, processId);
            }
        }


    }


    /**
     * 递归创建节点间连线
     *
     * @param process 流程
     * @param nodeDto 节点对象
     */
    public static void buildAllNodeOuterSequence(Process process, NodeDto nodeDto, NodeDto nextNodeDto) {


        //子节点
        NodeDto children = nodeDto.getChildren();
        if (StrUtil.equalsAny(nodeDto.getType(), NodeTypeEnum.CONDITIONS.getKey(), NodeTypeEnum.INCLUSIVES.getKey(), NodeTypeEnum.CONCURRENTS.getKey())) {
            children = children.getChildren();
            //条件分支
            List<NodeDto> branchs = nodeDto.getBranchs();
            int ord = 1;
            int size = branchs.size();
            for (NodeDto branch : branchs) {
                buildAllNodeOuterSequence(process, branch.getChildren(), NodeUtil.isNode(children) ? children : nextNodeDto
                );

                String expression = null;

                if (ord == size && StrUtil.equalsAny(nodeDto.getType(), NodeTypeEnum.CONDITIONS.getKey(), NodeTypeEnum.INCLUSIVES.getKey())) {
                    expression = NodeExpressionStrategyFactory.handleDefaultBranch(branchs);
                } else {
                    expression = NodeExpressionStrategyFactory.handle(branch.getProps());
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
     * @param processId
     * @return
     */
    private static List<FlowElement> buildNode(NodeDto node, String processId) {
        List<FlowElement> flowElementList = new ArrayList<>();

        //设置节点的连线头节点
        node.setHeadId(node.getId());
        //设置节点的连线尾节点
        node.setTailId(node.getId());
        node.setName(StrUtil.format("{}[{}]",node.getName(),RandomUtil.randomNumbers(5)));

        //存储节点数据
        INodeDataStoreHandler nodeDataStoreHandler = NodeDataStoreFactory.getInstance();
        nodeDataStoreHandler.save(processId, node.getId(), JSON.toJSONString(node));

        //开始
        if (StrUtil.equals(node.getType(), NodeTypeEnum.ROOT.getKey())) {
            flowElementList.addAll(buildStartNode(node));
        }
        //结束
        if (StrUtil.equals(node.getType(), NodeTypeEnum.END.getKey())) {
            flowElementList.add(buildEndNode(node, false));
        }
        //审批
        if (StrUtil.equals(node.getType(), NodeTypeEnum.APPROVAL.getKey())) {
            flowElementList.addAll(buildApproveNode(node));
        }
        //延时器
        if (StrUtil.equals(node.getType(), NodeTypeEnum.DELAY.getKey())) {
            flowElementList.add(buildDelayNode(node));
        }

        //抄送
        if (StrUtil.equals(node.getType(), NodeTypeEnum.CC.getKey())) {
            flowElementList.add(buildCCNode(node));
        }
        //触发器
        if (StrUtil.equals(node.getType(), NodeTypeEnum.TRIGGER.getKey())) {
            flowElementList.addAll(buildTriggerNode(node));
        }
        //包容分支-并行分支
        if (StrUtil.equalsAny(node.getType(), NodeTypeEnum.INCLUSIVES.getKey())) {
            flowElementList.addAll(buildInclusiveGatewayNode(node));
        }
        //包容分支-并行分支
        if (StrUtil.equalsAny(node.getType(),NodeTypeEnum.CONCURRENTS.getKey())) {
            flowElementList.addAll(buildParallelGatewayNode(node));
        }

        //条件分支
        if (StrUtil.equalsAny(node.getType(), NodeTypeEnum.CONDITIONS.getKey())) {
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
    private static List<FlowElement> buildStartNode(NodeDto node) {



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
    private static List<FlowElement> buildApproveNode(NodeDto node) {
        List<FlowElement> flowElementList = new ArrayList<>();


        node.setTailId(StrUtil.format("approve_gateway_{}", node.getId()));



        //创建了任务执行监听器
        //先执行指派人 后创建
        //https://tkjohn.github.io/flowable-userguide/#eventDispatcher
        FlowableListener createListener = new FlowableListener();
        createListener.setImplementation(ApprovalCreateListener.class.getCanonicalName());
        createListener.setImplementationType("class");
        createListener.setEvent("create");


        UserTask userTask = buildUserTask(node,  createListener);
        flowElementList.add(userTask);

        NodeDto exclusiveNode = new NodeDto();
        exclusiveNode.setId(StrUtil.format("approve_gateway_{}", node.getId()));
        exclusiveNode.setName("审批-排他网关");
        flowElementList.add(buildExclusiveGatewayNode(exclusiveNode));
        //创建结束节点

        NodeDto endNode = new NodeDto();
        endNode.setId(StrUtil.format("approve_end_{}", node.getId()));
        endNode.setName("审批-结束节点");
        EndEvent endEvent = buildEndNode(endNode, false);
        flowElementList.add(endEvent);


        {
            //如果是主管审批 只能是串行
            NodePropDto props = node.getProps();
            String assignedType = props.getAssignedType();
            if (StrUtil.equals(assignedType, "LEADER_TOP")) {
                props.setMode("NEXT");
            }
        }


        {


            //执行人处理

            String inputDataItem = "${multiInstanceHandler.resolveAssignee(execution)}";


            //串行
            NodePropDto props = node.getProps();
            boolean isSequential = true;

            String mode = props.getMode();
            //多人
            if (StrUtil.equals(mode, "AND")) {
                //并行会签
                isSequential = false;
            }
            if (StrUtil.equals(mode, "NEXT")) {
                //串行会签
            }
            if (StrUtil.equals(mode, "OR")) {
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
     * 构建触发器
     *
     * @param nodeDto
     * @return
     */
    private static List<FlowElement> buildTriggerNode(NodeDto nodeDto) {

        nodeDto.setTailId(StrUtil.format("trigger_gateway_{}", nodeDto.getId()));


        FlowableListener flowListener = new FlowableListener();
        flowListener.setImplementation(TriggerListener.class.getCanonicalName());
        flowListener.setImplementationType("class");
        flowListener.setEvent("create");

        UserTask userTask = buildUserTask(nodeDto, flowListener);
        userTask.addAttribute(generateExtensionAttribute("branchConditionKey", nodeDto.getId() + "_trigger_gateway_condition"));


        NodeDto exclusiveNode = new NodeDto();
        exclusiveNode.setId(StrUtil.format("trigger_gateway_{}", nodeDto.getId()));
        exclusiveNode.setName("触发器-排他网关");

        //结束
        NodeDto endNode = new NodeDto();
        endNode.setName("触发器-结束节点");
        endNode.setId(StrUtil.format("trigger_gateway_end_{}", nodeDto.getId()));
        EndEvent endEvent = buildEndNode(endNode, false);

        //排他网关
        FlowElement flowElement = buildExclusiveGatewayNode(exclusiveNode);

        return CollUtil.newArrayList(userTask, flowElement, endEvent);
    }


    /**
     * 创建抄送节点
     *
     * @param node
     * @return
     */
    private static FlowElement buildCCNode(NodeDto node) {


        //被指派了任务执行监听器
        FlowableListener assignListener = new FlowableListener();
        assignListener.setImplementation(CCAssignListener.class.getCanonicalName());
        assignListener.setImplementationType("class");
        assignListener.setEvent("assignment");


        UserTask userTask = buildUserTask(node, assignListener);


        //执行人处理

        String inputDataItem = "${multiInstanceHandler.resolveAssignee(execution)}";


        boolean isSequential = false;


        MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics();
        loopCharacteristics.setSequential(isSequential);
        loopCharacteristics.setInputDataItem(inputDataItem);
        loopCharacteristics.setElementVariable(StrUtil.format("{}_assignee_temp", node.getId()));

        loopCharacteristics.setCompletionCondition("${multiInstanceHandler.completionCondition(execution)}");

        userTask.setLoopCharacteristics(loopCharacteristics);

        String variable = StrUtil.format("${{}_assignee_temp}", node.getId());

        userTask.setAssignee(variable);


        return userTask;
    }

    /**
     * 构建审批节点
     *
     * @param node
     * @return
     */
    private static IntermediateCatchEvent buildDelayNode(NodeDto node) {

        NodePropDto props = node.getProps();

        TimerEventDefinition timerEventDefinition = new TimerEventDefinition();

        if (StrUtil.equals(props.getType(), "FIXED")) {
            if (props.getUnit().length() == 1) {
                //年月日
                timerEventDefinition.setTimeDuration(StrUtil.format("P{}{}", props.getTime(), props.getUnit()));
            } else {
                //时分秒
                timerEventDefinition.setTimeDuration(StrUtil.format("PT{}{}", props.getTime(),
                        StrUtil.subAfter(props.getUnit(), "T", true)));

            }
        }
        if (StrUtil.equals(props.getType(), "AUTO")) {
            DateTime dateTime = DateUtil.parseDateTime(props.getDateTime());
            timerEventDefinition.setTimeDate(DateUtil.format(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        }

        IntermediateCatchEvent catchEvent = new IntermediateCatchEvent();
        catchEvent.setId(node.getId());
        catchEvent.setName(node.getName());
        catchEvent.addEventDefinition(timerEventDefinition);

        return catchEvent;
    }

    /**
     * 创建用户任务
     *
     * @param node 前端传输节点
     * @return
     */
    private static UserTask buildUserTask(NodeDto node, FlowableListener... flowableListeners) {
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
     * 构建并行网关
     * @param node
     * @return
     */
    private static List<FlowElement> buildParallelGatewayNode(NodeDto node) {
        // node.setTailId(StrUtil.format("{}_merge_gateway", node.getId()));

        List<FlowElement> flowElementList = new ArrayList<>();

        ParallelGateway inclusiveGateway = new ParallelGateway();
        inclusiveGateway.setId(node.getId());
        inclusiveGateway.setName(node.getName());
        flowElementList.add(inclusiveGateway);

        //合并网关
        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(StrUtil.format("{}_merge_gateway", node.getId()));
        parallelGateway.setName(StrUtil.format("{}_合并网关", node.getName()));
        flowElementList.add(parallelGateway);

        return flowElementList;
    }
    /**
     * 构建包容网关
     *
     * @param node
     * @return
     */
    private static List<FlowElement> buildInclusiveGatewayNode(NodeDto node) {
       // node.setTailId(StrUtil.format("{}_merge_gateway", node.getId()));

        List<FlowElement> flowElementList = new ArrayList<>();

        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(node.getId());
        inclusiveGateway.setName(node.getName());
        flowElementList.add(inclusiveGateway);

        //合并网关
        InclusiveGateway parallelGateway = new InclusiveGateway();
        parallelGateway.setId(StrUtil.format("{}_merge_gateway", node.getId()));
        parallelGateway.setName(StrUtil.format("{}_合并网关", node.getName()));
        flowElementList.add(parallelGateway);

        return flowElementList;
    }

    /**
     * 构建排他网关
     *
     * @param node
     * @return
     */
    private static FlowElement buildExclusiveGatewayNode(NodeDto node) {
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
    private static EndEvent buildEndNode(NodeDto node, boolean terminateAll) {
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
    private static List<SequenceFlow> buildSequenceFlow(NodeDto node, NodeDto parentNode, String expression
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
     * 创建连接线
     *
     * @param node      父级节点
     * @param processId
     * @return 所有连接线
     */
    private static List<SequenceFlow> buildInnerSequenceFlow(NodeDto node,String processId
    ) {


        List<SequenceFlow> sequenceFlowList = new ArrayList<>();


        String nodeId = node.getId();
        if (StrUtil.hasBlank(nodeId)) {
            return sequenceFlowList;
        }
        boolean parentIsGateway=false;
        NodeDto prevNode=null;
        {
            //判断父级是否是包容或者并行网关
            NodeDto parentNodeDto = NodeDataStoreFactory.getInstance().getNodeDto(processId, node.getParentId());
            if(parentNodeDto!=null){
                prevNode = NodeDataStoreFactory.getInstance().getNodeDto(processId, parentNodeDto.getParentId());

                //父级是否是网关
                parentIsGateway=prevNode==null?false:StrUtil.equalsAny(prevNode.getType(),NodeTypeEnum.CONCURRENTS.getKey(),NodeTypeEnum.INCLUSIVES.getKey());

            }

        }


        if(parentIsGateway){
            String headId = node.getHeadId();
            node.setHeadId(StrUtil.format("{}_merge_gateway",prevNode.getId()));
            {
                SequenceFlow sequenceFlow = buildSingleSequenceFlow(node.getHeadId(), headId, "${12==12}");
                sequenceFlowList.add(sequenceFlow);
            }

        }


        if (StrUtil.equals(node.getType(), NodeTypeEnum.TRIGGER.getKey())) {


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

        } else if (StrUtil.equals(node.getType(), NodeTypeEnum.APPROVAL.getKey())) {


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
