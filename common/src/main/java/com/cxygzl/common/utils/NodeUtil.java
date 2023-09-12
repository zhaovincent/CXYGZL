package com.cxygzl.common.utils;

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
            end.setId("end");
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
     * 需要发起人选择用户的节点
     * @param node
     */
    public static List<String> selectUserNodeId(Node node){
        List<String> list=new ArrayList();

        if(!isNode(node)){
            return list;
        }

        Integer type = node.getType();
//        if(StrUtil.equals(type,NodeTypeEnum.EMPTY.getKey())){
//            return selectUserNodeId(nodeDto.getChildren());
//        }

        //SELF_SELECT




        if(type==NodeTypeEnum.APPROVAL.getValue().intValue()){

            Integer assignedType = node.getAssignedType();

            boolean selfSelect =assignedType== ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
            if(selfSelect){
                list.add(node.getId());
            }
        }

        if(type== NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()||type== NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()){

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
