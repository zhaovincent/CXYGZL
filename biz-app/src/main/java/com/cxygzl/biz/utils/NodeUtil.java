package com.cxygzl.biz.utils;

import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Nobody;
import com.cxygzl.common.dto.flow.Node;

import java.util.List;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_REFUSE;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-09-12 14:45
 */
public class NodeUtil {


    /**
     * 处理发起人节点
     *
     * @param node
     */
    public static void handleStarterNode(Node node, List<FormItemVO> formItemVOList) {
        Map<String, String> formPerms = node.getFormPerms();
        for (FormItemVO formItemVO : formItemVOList) {
            if (formPerms.get(formItemVO.getId()) != null) {
                continue;
            }
            formPerms.put(formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
        }
    }

    /**
     * 处理用户任务节点的表单--如果没设置上 默认是只读
     *
     * @param node
     */
    public static void handleApproveForm(Node node, List<FormItemVO> formItemVOList) {
        if (!com.cxygzl.common.utils.NodeUtil.isNode(node)) {
            return;
        }

        Integer type = node.getType();
        if (type.intValue() == NodeTypeEnum.APPROVAL.getValue()) {
            Map<String, String> formPerms = node.getFormPerms();
            for (FormItemVO formItemVO : formItemVOList) {
                if (formPerms.get(formItemVO.getId()) != null) {
                    continue;
                }
                formPerms.put(formItemVO.getId(), ProcessInstanceConstant.FormPermClass.READ);
            }
        }


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                branch.setParentId(node.getId());
                Node children = branch.getChildNode();
                handleApproveForm(children, formItemVOList);
            }

        }

        handleApproveForm(node.getChildNode(), formItemVOList);

    }


    /**
     * 处理用户任务节点
     *
     * @param node
     */
    public static void handleApprove(Node node) {
        if (!com.cxygzl.common.utils.NodeUtil.isNode(node)) {
            return;
        }

        Integer type = node.getType();
        if (type.intValue() == NodeTypeEnum.APPROVAL.getValue()) {
            //如果是系统自动拒绝 则审批人为空选项置为自动拒绝
            if (node.getAssignedType() == ProcessInstanceConstant.AssignedTypeClass.SYSTEM_REFUSE) {
                Nobody nobody = new Nobody();
                nobody.setHandler(USER_TASK_NOBODY_HANDLER_TO_REFUSE);
                node.setNobody(nobody);
            }
        }


        if (NodeTypeEnum.getByValue(type).getBranch()) {

            //分支
            List<Node> branchs = node.getConditionNodes();
            for (Node branch : branchs) {
                branch.setParentId(node.getId());
                Node children = branch.getChildNode();
                handleApprove(children);
            }

        }

        handleApprove(node.getChildNode());

    }


}
