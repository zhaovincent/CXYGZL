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

    public static final int POSITION_WIDTH = 200;
    public static final int POSITION_HEIGHT = 150;

    public static NodeImageVO initPosition(Node node) {
        //初始化各个分支下的各个子级最大数量
        initNum(node);
        //初始化X坐标
        initXPosition(node, 0);
        //初始化Y坐标
        initYPosition(node, 150);

        List<NodeLinkDto> nodeLinkDtoList = buildLinkList(node, null, true);
        List<NodeImageVO.Edge> edges = new ArrayList<>();
        for (NodeLinkDto nodeLinkDto : nodeLinkDtoList) {
            NodeImageVO.Edge edge =
                    NodeImageVO.Edge.builder().sourceNodeId(nodeLinkDto.getPrevNodeId()).targetNodeId(nodeLinkDto.getNextNodeId()).type("polyline").build();
            edges.add(edge);
        }


        List<NodeImageVO.Node> nodeShowList = getNodeShowList(node);
        return NodeImageVO.builder().nodes(nodeShowList).edges(edges).build();
    }

    public static List<NodeImageVO.Node> getNodeShowList(Node node) {
        List<NodeImageVO.Node> list = new ArrayList<>();
        if (!NodeUtil.isNode(node)) {
            return list;
        }

        NodeImageVO.Node imageVO = NodeImageVO.Node.builder()
                .text(node.getNodeName())
                .id(node.getId())
                .type("rect")
                .x(node.getXPosition())
                .y(node.getYPosition())
                .build();
        list.add(imageVO);


        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            List<Node> conditionNodes = node.getConditionNodes();
            for (Node conditionNode : conditionNodes) {
                List<NodeImageVO.Node> nodeShowList = getNodeShowList(conditionNode);
                list.addAll(nodeShowList);
            }


        }
        List<NodeImageVO.Node> nodeShowList = getNodeShowList(node.getChildNode());
        list.addAll(nodeShowList);
        return list;
    }

    public static void initYPosition(Node node, int p) {
        if (!NodeUtil.isNode(node)) {
            return;
        }

        node.setYPosition(p);


        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            List<Node> conditionNodes = node.getConditionNodes();

            int max = 1;

            for (Node conditionNode : conditionNodes) {

                // initYPosition(conditionNode, p + 1);

                List<Node> childrenNodeList = NodeUtil.getChildrenNodeListSameBranch(conditionNode);
                int sum = childrenNodeList.stream().filter(w -> w.getContainBranchNum() != null).mapToInt(w -> w.getContainBranchNum()).sum();
                max = Math.max(max, sum);
            }

            int h = max * POSITION_HEIGHT;

            int size = conditionNodes.size();
            if (size % 2 == 0) {
                //偶数
                int index = 1;
                for (Node conditionNode : conditionNodes) {

                    if (size / 2 > index) {
                        initYPosition(conditionNode, p + h / 2 + (h * ((size - index))));

                    } else {
                        initYPosition(conditionNode, p - h / 2 - (h * ((size / 2 - index))));

                    }


                    index++;

                }
            } else {
                //奇数
                int index = 1;
                for (Node conditionNode : conditionNodes) {

                    int p1 = p - (h * ((size + 1) / 2 - index));
                    log.info("分支：{} Y坐标：{} 索引:{}", conditionNode.getNodeName(), p1, index);

                    initYPosition(conditionNode, p1);


                    index++;

                }
            }


        }
        initYPosition(node.getChildNode(), p);
    }


    public static void initXPosition(Node node, int p) {

        if (!NodeUtil.isNode(node)) {
            return;
        }

        node.setXPosition((p + 1) * POSITION_WIDTH);


        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            List<Node> conditionNodes = node.getConditionNodes();

            for (Node conditionNode : conditionNodes) {

                initXPosition(conditionNode, p + 1);

            }


        }
        initXPosition(node.getChildNode(), p + (node.getContainNodeNum() == null ? 1 : node.getContainNodeNum()));

    }

    public static void initNum(Node node) {
        if (!NodeUtil.isNode(node)) {
            return;
        }

        Integer type = node.getType();


        if (NodeTypeEnum.getByValue(type).getBranch()) {
            int maxNum = 0;
            int maxBranchNum = 0;
            List<Node> conditionNodes = node.getConditionNodes();
            for (Node conditionNode : conditionNodes) {


                initNum(conditionNode.getChildNode());

                {
                    List<Node> childrenNodeList = NodeUtil.getChildrenNodeListSameBranch(conditionNode);

                    int sum = childrenNodeList.stream().mapToInt(w ->
                            w.getContainNodeNum() == null ? 1 : (w.getContainNodeNum())
                    ).sum() + 1;
                    maxNum = Math.max(maxNum, sum);
                }
                {
                    List<Node> childrenNodeList = NodeUtil.getChildrenNodeListSameBranch(conditionNode);
                    int sum = childrenNodeList.stream().filter(w -> w.getContainBranchNum() != null).mapToInt(w -> w.getContainBranchNum()).sum();

                    maxBranchNum = Math.max(maxBranchNum, sum);
                }
            }
            node.setContainNodeNum(maxNum);
            node.setContainBranchNum(maxBranchNum + conditionNodes.size());
            log.info("分支:{} 最大子级数量:{} 最大分支数量:{}", node.getNodeName(), maxNum, node.getContainBranchNum());

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
