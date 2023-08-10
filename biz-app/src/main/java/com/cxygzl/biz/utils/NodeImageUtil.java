package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.vo.node.NodeImageVO;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.dto.NodeLinkDto;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NodeImageUtil {

    public static void initPosition(Node allNode, NodeImageVO node, int lastX, int lastY, int px, int py) {
        Node parentNode = com.cxygzl.common.utils.NodeUtil.getParentNode(allNode, node.getId());
        log.info("节点:{}的上级是：{}", node.getNodeName(), parentNode == null ? "" : parentNode.getNodeName());
    }

    public static void initNum(Node node){
        if(!NodeUtil.isNode(node)){
            return;
        }
        List<Node> childrenNodeList = NodeUtil.getChildrenNodeList(node, node.getId());
        log.info("节点：{} 的子级数量：{} ",node.getNodeName(),childrenNodeList.size());

        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {
            List<Node> conditionNodes = node.getConditionNodes();
            for (Node conditionNode : conditionNodes) {
                initNum(conditionNode.getChildNode());
            }
        }
        initNum(node.getChildNode());
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

        if (!com.cxygzl.common.utils.NodeUtil.isNode(node)) {
            return list;
        }


        Integer type = node.getType();

        Node childNode = node.getChildNode();

        if (!NodeTypeEnum.getByValue(type).getBranch()) {

            if (com.cxygzl.common.utils.NodeUtil.isNode(childNode) && !StrUtil.equals(node.getId(), childNode.getId())) {
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


                List<NodeLinkDto> dtoList = buildLinkList(children, NodeUtil.isNode(childNode) ? childNode : parentBranchNextNode, true);
                list.addAll(dtoList);
            }
        }

        List<NodeLinkDto> dtoList = buildLinkList(childNode, parentBranchNextNode, !NodeTypeEnum.getByValue(type).getBranch());
        list.addAll(dtoList);
        return list;
    }


}
