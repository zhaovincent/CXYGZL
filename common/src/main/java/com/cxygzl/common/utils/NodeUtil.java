package com.cxygzl.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.NodeLinkDto;
import com.cxygzl.common.dto.flow.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {

    public static void handleNodeAddExecutionId(Node node, String nodeId, String executionId) {

        if (!isNode(node)) {
            return;
        }

        if (StrUtil.contains(nodeId, node.getId()) && StrUtil.isBlank(node.getExecutionId())) {
            node.setExecutionId(executionId);
            return;
        }


        Integer type = node.getType();

        if (NodeTypeEnum.getByValue(type).getBranch()&&CollUtil.isNotEmpty(node.getConditionNodes())) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                handleNodeAddExecutionId(children, nodeId, executionId);

            }
        }

        handleNodeAddExecutionId(node.getChildNode(), nodeId, executionId);
    }

    /**
     * 处理排他网关为线性
     *
     * @param node
     * @param parentId
     * @param nodeId
     */
    public static void handleExclusiveGatewayAsLine(Node node, String parentId, String nodeId) {

        if (!isNode(node)) {
            return;
        }
        Node childNode = node.getChildNode();

        List<Node> branchs = node.getConditionNodes();

        if (StrUtil.equals(node.getId(), parentId)) {
            //找到排他分支了
            if (CollUtil.isNotEmpty(branchs)) {
                for (Node branch : branchs) {
                    Node children = branch.getChildNode();
                    if (StrUtil.equals(children.getId(), nodeId)) {
                        node.setConditionNodes(null);
                        //就是该分支
                        node.setChildNode(children);

                        Node c = BeanUtil.copyProperties(children, Node.class);

                        List<Node> nList = new ArrayList<>();
                        while (true) {
                            if (isNode(c)) {
                                nList.add(c);
                                c = c.getChildNode();
                            } else {
                                break;
                            }
                        }
                        Node n = nList.get(nList.size() - 1);
                        n.setChildNode(childNode);
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

                handleExclusiveGatewayAsLine(children, parentId, nodeId);


            }
        }

        handleExclusiveGatewayAsLine(childNode, parentId, nodeId);

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
        return nodeList.get(nodeList.size() - 1);
    }

    /**
     * 获取父级节点
     *
     * @param node
     * @param nodeId
     * @return
     */
    public static Node getParentNode(Node node, String nodeId) {

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
                Node children = branch.getChildNode();

                if (isNode(children) && StrUtil.equals(children.getId(), nodeId)) {
                    return node;
                }
                Node parentNode = getParentNode(children, nodeId);
                if (parentNode != null) {
                    return parentNode;
                }

            }
        }

        return getParentNode(childNode, nodeId);
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
     * 创建节点连线集合
     *
     * @param node
     * @param parentBranchNextNode
     * @param effect
     * @return
     */
    public static List<NodeLinkDto> buildLinkList(Node node, Node parentBranchNextNode, boolean effect) {
        List<NodeLinkDto> list = new ArrayList();

        if (!isNode(node)) {
            return list;
        }


        Integer type = node.getType();

        Node childNode = node.getChildNode();

        if (!NodeTypeEnum.getByValue(type).getBranch()) {

            if (isNode(childNode) && !StrUtil.equals(node.getId(), childNode.getId())) {
                NodeLinkDto build = NodeLinkDto.builder()
                        .prevId(node.getTempId())
                        .prevNodeId(node.getId())
                        .prevName(node.getNodeName())
                        .nextId(childNode.getTempId())
                        .nextNodeId(childNode.getId())
                        .nextName(childNode.getNodeName()).build();
                list.add(build);
            } else if (parentBranchNextNode != null && !StrUtil.equals(node.getId(), parentBranchNextNode.getId())) {
                NodeLinkDto build = NodeLinkDto.builder()
                        .prevId(node.getTempId())
                        .prevNodeId(node.getId())
                        .prevName(node.getNodeName())
                        .nextId(parentBranchNextNode.getTempId())
                        .nextNodeId(parentBranchNextNode.getId())
                        .nextName(parentBranchNextNode.getNodeName())
                        .build();
                list.add(build);
            }
        }


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //条件分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                Node children = branch.getChildNode();

                //记录节点和分支下的节点数据
                NodeLinkDto build = NodeLinkDto.builder()
                        .prevId(node.getTempId())
                        .prevNodeId(node.getId())
                        .prevName(node.getNodeName())
                        .nextId(children.getTempId())
                        .nextNodeId(children.getId())
                        .nextName(children.getNodeName())
                        .build();
                list.add(build);


                List<NodeLinkDto> dtoList = buildLinkList(children, isNode(childNode) ? childNode : parentBranchNextNode, true);
                list.addAll(dtoList);
            }
        }

        List<NodeLinkDto> dtoList = buildLinkList(childNode, parentBranchNextNode, !NodeTypeEnum.getByValue(type).getBranch());
        list.addAll(dtoList);
        return list;
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

    public static String getFlowId(String processDefinitionId) {
        return StrUtil.subBefore(processDefinitionId, ":", false);
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
     * @param executionId
     */
    public static void handleChildrenAfterJump(Node node, String parentId, Node c,String executionId) {
        if (!isNode(node)) {
            return;
        }
        Node childNode = node.getChildNode();


        if (StrUtil.contains(parentId,node.getId())&&(!isNode(childNode)||StrUtil.isBlank(childNode.getExecutionId()))) {
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
                handleChildrenAfterJump(children, parentId, c, executionId);

            }

        }

        handleChildrenAfterJump(childNode, parentId, c, executionId);

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
