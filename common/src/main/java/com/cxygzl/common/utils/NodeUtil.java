package com.cxygzl.common.utils;

import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodePropDto;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {

    public static String getProcessId(String processDefinitionId){
        return StrUtil.subBefore(processDefinitionId, ":", false);
    }

    public static boolean isNode(NodeDto childNode) {
        if (childNode != null && StrUtil.isNotBlank(childNode.getId())) {
            return true;
        }
        return false;
    }


    /**
     * 需要发起人选择用户的节点
     * @param nodeDto
     */
    public static List<String> selectUserNodeId(NodeDto nodeDto){
        List<String> list=new ArrayList();

        if(!isNode(nodeDto)){
            return list;
        }

        String type = nodeDto.getType();
        if(StrUtil.equals(type,NodeTypeEnum.EMPTY.getKey())){
            return selectUserNodeId(nodeDto.getChildren());
        }

        //SELF_SELECT




        if(StrUtil.equals(type, NodeTypeEnum.APPROVAL.getKey())){
            NodePropDto props = nodeDto.getProps();
            String assignedType = props.getAssignedType();
            boolean selfSelect = StrUtil.equals(assignedType, "SELF_SELECT");
            if(selfSelect){
                list.add(nodeDto.getId());
            }
        }

        if(StrUtil.equalsAny(type, NodeTypeEnum.INCLUSIVES.getKey(), NodeTypeEnum.CONCURRENTS.getKey(), NodeTypeEnum.CONDITIONS.getKey())){

            //条件分支
            List<NodeDto> branchs = nodeDto.getBranchs();
            for (NodeDto branch : branchs) {
                NodeDto children = branch.getChildren();
                List<String> strings = selectUserNodeId(children);
                list.addAll(strings);
            }
        }

        List<String> next = selectUserNodeId(nodeDto.getChildren());
        list.addAll(next);
        return list;
    }

}
