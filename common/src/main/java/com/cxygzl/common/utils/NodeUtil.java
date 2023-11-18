package com.cxygzl.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.common.dto.flow.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {
    /**
     * 处理节点添加执行id和流程唯一id
     *
     * @param node
     * @param nodeId
     * @param executionId
     * @param flowUniqueId
     */
    public static void handleNodeAddExecutionIdFlowUniqueId(Node node, String nodeId, String executionId,
                                                            String flowUniqueId) {

        if (!isNode(node)) {
            return;
        }

        if (((StrUtil.contains(nodeId, node.getId()) && StrUtil.startWith(nodeId, ProcessInstanceConstant.VariableKey.STARTER)) ||
                (StrUtil.equals(nodeId, node.getId()))) && StrUtil.isBlank(node.getExecutionId())) {
            node.setExecutionId(executionId);
            node.setFlowUniqueId(flowUniqueId);
            return;
        }


        Integer type = node.getType();

        if (NodeTypeEnum.getByValue(type).getBranch() && CollUtil.isNotEmpty(node.getConditionNodes())) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                handleNodeAddExecutionIdFlowUniqueId(children, nodeId, executionId, flowUniqueId);

            }
        }

        handleNodeAddExecutionIdFlowUniqueId(node.getChildNode(), nodeId, executionId, flowUniqueId);
    }

    /**
     * 处理排他网关为线性
     *
     * @param node
     * @param parentId
     * @param nodeId
     * @param parentNode
     */
    public static void handleExclusiveGatewayAsLine(Node node, String parentId, String nodeId, Node parentNode) {

        if (!isNode(node)) {
            return;
        }
        Node childNode = node.getChildNode();

        List<Node> branchs = node.getConditionNodes();

        if (StrUtil.equals(node.getId(), parentId)) {
            //找到排他分支了
            if (CollUtil.isNotEmpty(branchs)) {

                node.setConditionNodes(null);
                for (Node branch : branchs) {
                    Node children = branch.getChildNode();

                    if (children != null && StrUtil.equals(children.getId(), nodeId)) {
                        //就是该分支
                        parentNode.setChildNode(children);

                        Node c = getFinalChildrenNode(children);

                        c.setChildNode(childNode);
                        break;
                    }

                }

            }
        }


        Integer type = node.getType();

        if (NodeTypeEnum.getByValue(type).getBranch() && CollUtil.isNotEmpty(branchs)) {

            //条件分支
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                handleExclusiveGatewayAsLine(children, parentId, nodeId, node);


            }
        }

        handleExclusiveGatewayAsLine(childNode, parentId, nodeId, node);

    }

    /**
     * 处理包容网关 删除没用执行的节点
     *
     * @param nodeId
     * @param node
     * @param gatewayNodeId
     * @param parentNode
     */
    public static void handleInclusiveGatewayAsLine(Node node, String gatewayNodeId, String executionId,
                                                    String flowUniqueId,
                                                    List<ProcessInstanceNodeRecordParamDto> processInstanceNodeRecordParamDtoList, Node parentNode) {

        if (!isNode(node)) {
            return;
        }
        Node childNode = node.getChildNode();

        List<Node> branchList = new ArrayList<>();

        List<Node> branchs = node.getConditionNodes();

        if (StrUtil.equals(node.getId(), gatewayNodeId) && StrUtil.equals(node.getExecutionId(), executionId) && StrUtil.equals(node.getFlowUniqueId(), flowUniqueId)) {
            //找到排他分支了
            if (CollUtil.isNotEmpty(branchs)) {

                for (Node branch : branchs) {

                    Node children = branch.getChildNode();
                    if (children == null) {
                        //iterator.remove();
                    } else {
                        String id = children.getId();
                        String executionId1 = children.getExecutionId();
                        String flowUniqueId1 = children.getFlowUniqueId();


                        long count = processInstanceNodeRecordParamDtoList.stream()
                                .filter(w -> StrUtil.equals(w.getNodeId(), id))
                                .filter(w -> StrUtil.equals(w.getFlowUniqueId(), flowUniqueId1))
                                .filter(w -> StrUtil.equals(w.getExecutionId(), executionId1)).count();
                        if (count == 0) {
                            //  iterator.remove();
                        } else {
                            branchList.add(branch);
                        }


                    }

                }


            }
        } else if (branchs != null) {
            branchList.addAll(branchs);
        }

        node.setConditionNodes(branchList);

        if (branchList.size() == 1) {
            //只有一个分支执行了

            Node children = branchList.get(0).getChildNode();

            //就是该分支
            parentNode.setChildNode(children);

            Node c = getFinalChildrenNode(children);

            c.setChildNode(childNode);

        }

        Integer type = node.getType();

        if (NodeTypeEnum.getByValue(type).getBranch() && CollUtil.isNotEmpty(branchList)) {

            //条件分支
            for (Node branch : branchList) {
                Node children = branch.getChildNode();

                handleInclusiveGatewayAsLine(children, gatewayNodeId, executionId, flowUniqueId, processInstanceNodeRecordParamDtoList, node);


            }
        }

        handleInclusiveGatewayAsLine(childNode, gatewayNodeId, executionId, flowUniqueId, processInstanceNodeRecordParamDtoList, node);

    }

    /**
     * 获取自己节点列表
     *
     * @param node
     * @param nodeId
     * @return
     */
    public static List<Node> getChildrenNodeList(Node node, String nodeId) {
        List<Node> nodeList = new ArrayList<>();
        if (!isNode(node)) {
            return nodeList;
        }
        Node childNode = node.getChildNode();

        if (StrUtil.equals(node.getId(), nodeId)) {

            nodeList.add(node);
            while (true) {
                if (isNode(childNode)) {
                    nodeList.add(childNode);
                    childNode = childNode.getChildNode();
                } else {
                    break;
                }
            }
            return nodeList;
        }


        Integer type = node.getType();

        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                List<Node> c = getChildrenNodeList(children, nodeId);
                if (CollUtil.isNotEmpty(c)) {
                    return c;
                }

            }
        }

        return getChildrenNodeList(childNode, nodeId);

    }

    /**
     * 获取最后一个节点
     *
     * @param n
     * @return
     */
    private static Node getFinalChildrenNode(Node n) {
        List<Node> nodeList = getChildrenNodeListSameBranch(n);
        return nodeList.get(nodeList.size() - 1);
    }

    /**
     * 获取最后一个节点
     *
     * @param n
     * @return
     */
    public static List<Node> getChildrenNodeListSameBranch(Node n) {

        List<Node> nodeList = new ArrayList<>();
        nodeList.add(n);


        while (true) {
            Node node = nodeList.get(nodeList.size() - 1);
            Node c = node.getChildNode();

            if (isNode(c)) {
                nodeList.add(c);
            } else {
                break;
            }
        }
        return nodeList;
    }


    /**
     * 一直到最上层的节点
     *
     * @param node
     * @param nodeId
     * @return
     */
    public static List<Node> getParentNodeUntilRoot(Node node, String nodeId) {

        List<Node> nodeList = new ArrayList<>();


        while (true) {
            Node parentNode = getParentNode(node, nodeId, true);

            if (isNode(parentNode)) {
                nodeList.add(parentNode);
                nodeId = parentNode.getId();
            } else {
                break;
            }
        }


        return nodeList;
    }

    /**
     * 获取父级节点
     *
     * @param node
     * @param nodeId
     * @param containEmptyNode
     * @return
     */
    public static Node getParentNode(Node node, String nodeId, boolean containEmptyNode) {

        if (!isNode(node)) {
            return null;
        }


        Node childNode = node.getChildNode();

        if (isNode(childNode) && StrUtil.equals(childNode.getId(), nodeId)) {
            return node;
        }

        Integer type = node.getType();

        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {

                if (containEmptyNode) {
                    if (isNode(branch)&&StrUtil.equals(branch.getId(), nodeId)) {
                        return node;
                    }
                    Node parentNode = getParentNode(branch, nodeId, containEmptyNode);
                    if (parentNode != null) {
                        return parentNode;
                    }
                    continue;
                }

                Node children = branch.getChildNode();

                if (isNode(children) && StrUtil.equals(children.getId(), nodeId)) {
                    return node;
                }
                Node parentNode = getParentNode(children, nodeId, containEmptyNode);
                if (parentNode != null) {
                    return parentNode;
                }

            }
        }

        return getParentNode(childNode, nodeId, containEmptyNode);
    }

    /**
     * 初始化nodeId
     *
     * @param node
     */
    public static void initRandomNodeId(Node node) {

        if (!isNode(node)) {
            return;
        }

        node.setTempId(IdUtil.fastSimpleUUID());

        Integer type = node.getType();

        Node childNode = node.getChildNode();


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                initRandomNodeId(children);

            }
        }

        initRandomNodeId(childNode);

    }


    /**
     * 获取指定节点的下一个节点
     *
     * @param node
     * @param currentNodeId
     * @param parentBranchNextNode
     * @return
     */
    public static Node getNextNodeById(Node node, String currentNodeId, Node parentBranchNextNode) {
        if (!isNode(node)) {
            return null;
        }
        Node childNode = node.getChildNode();
        if (StrUtil.equals(currentNodeId, node.getId())) {

            if (isNode(childNode)) {
                return childNode;
            }
            return parentBranchNextNode;
        }

        if (NodeTypeEnum.getByValue(node.getType()).getBranch()) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                return getNextNodeById(children, currentNodeId, isNode(childNode) ? childNode : parentBranchNextNode);

            }
        }


        return getNextNodeById(childNode, currentNodeId, parentBranchNextNode);
    }

    /**
     * 添加结束节点
     *
     * @param node
     */
    public static void addEndNode(Node node) {

        Node children = node.getChildNode();
        if (isNode(children)) {
            addEndNode(children);
        } else {
            Node end = new Node();
            end.setId(ProcessInstanceConstant.VariableKey.END);
            end.setType(NodeTypeEnum.END.getValue());
            end.setNodeName("结束节点");
            end.setParentId(node.getId());
            node.setChildNode(end);
        }

    }


    public static boolean isNode(Node childNode) {
        if (childNode != null && StrUtil.isNotBlank(childNode.getId())) {
            return true;
        }
        return false;
    }


    /**
     * 处理父级id
     *
     * @param node
     */
    public static void handleParentId(Node node, String parentId) {
        if (!isNode(node)) {
            return;
        }
        node.setParentId(parentId);
        node.setTempId(StrUtil.format("{}|{}", node.getId(), IdUtil.fastSimpleUUID()));
        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                branch.setParentId(node.getId());
                Node children = branch.getChildNode();
                handleParentId(children, branch.getId());
            }

        }

        handleParentId(node.getChildNode(), node.getId());

    }

    /**
     * 节点跳转之后处理子级
     *
     * @param node
     */
    public static void handleChildrenAfterJump(Node node, String parentId, Node c) {
        if (!isNode(node)) {
            return;
        }
        Node childNode = node.getChildNode();


        if (((StrUtil.contains(parentId, node.getId()) &&
                StrUtil.startWith(parentId, ProcessInstanceConstant.VariableKey.STARTER)
        ) || (StrUtil.equals(parentId, node.getId()))) && (!isNode(childNode) || StrUtil.isBlank(childNode.getExecutionId()))) {
            node.setChildNode(c);
            Node finalChildrenNode = getFinalChildrenNode(c);
            if (finalChildrenNode.getType().intValue() != NodeTypeEnum.END.getValue()) {
                finalChildrenNode.setChildNode(childNode);
            }
            return;
        }

        Integer type = node.getType();
        List<Node> branchs = node.getConditionNodes();


        if (NodeTypeEnum.getByValue(type).getBranch() && CollUtil.isNotEmpty(branchs)) {

            //分支
            for (Node branch : branchs) {

                Node children = branch.getChildNode();
                handleChildrenAfterJump(children, parentId, c);

            }

        }

        handleChildrenAfterJump(childNode, parentId, c);

    }

    /**
     * 查找指定类型节点id
     *
     * @param node
     * @return
     */
    public static List<String> selectId(Node node, int tp) {

        List<String> list = new ArrayList();

        if (!isNode(node)) {
            return list;
        }

        Integer type = node.getType();


        if (type == tp) {

            list.add(node.getSubFlowId());
        }

        if (type == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()
                || type == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()
                || type == NodeTypeEnum.INCLUSIVE_GATEWAY.getValue().intValue()
        ) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();
                List<String> strings = selectSubProcessId(children);
                list.addAll(strings);
            }
        }

        List<String> next = selectSubProcessId(node.getChildNode());
        list.addAll(next);
        return list;
    }

    /**
     * 查找动态路由节点id
     *
     * @param node
     * @return
     */
    public static List<String> selectRouteId(Node node) {
        return selectId(node, NodeTypeEnum.ROUTE.getValue().intValue());
    }

    /**
     * 查找子流程id
     *
     * @param node
     * @return
     */
    public static List<String> selectSubProcessId(Node node) {
        return selectId(node, NodeTypeEnum.SUB_PROCESS.getValue().intValue());
    }

    /**
     * 需要发起人选择用户的节点
     *
     * @param node
     */
    public static List<String> selectUserNodeId(Node node) {
        List<String> list = new ArrayList();

        if (!isNode(node)) {
            return list;
        }

        Integer type = node.getType();


        if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

            Integer assignedType = node.getAssignedType();

            boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
            if (selfSelect) {
                list.add(node.getId());
            }
        }


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();
                List<String> strings = selectUserNodeId(children);
                list.addAll(strings);
            }
        }

        List<String> next = selectUserNodeId(node.getChildNode());
        list.addAll(next);
        return list;
    }

}
