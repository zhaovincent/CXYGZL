package com.cxygzl.common.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {


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
     */
    public static Node handleChildrenAfterJump(Node node, String parentId, Node c) {
        if (!isNode(node)) {
            return null;
        }

        if (node.getId().equals(parentId)) {
            node.setChildNode(c);
            return node;
        }

        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {

                Node children = branch.getChildNode();
                Node n = handleChildrenAfterJump(children, parentId, c);
                if(n!=null){
                    //branch.setChildNode(n);
                }
            }

        }

        Node n = handleChildrenAfterJump(node.getChildNode(), parentId, c);
        if(n!=null){
            //node.setChildNode(n);
        }

        return  node;
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

        if (type == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()
                || type == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()
                || type == NodeTypeEnum.INCLUSIVE_GATEWAY.getValue().intValue()
        ) {

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
