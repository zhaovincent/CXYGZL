package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.Node;

import java.util.List;
import java.util.Map;

/**
 * 节点格式化显示工具
 */
public class NodeUtil {

    public static void handleNodeLine(String processInstanceId, String nodeId) {
        IProcessInstanceRecordService service = SpringUtil.getBean(IProcessInstanceRecordService.class);
        ProcessInstanceRecord processInstanceRecord = service.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
        String process = processInstanceRecord.getProcess();
        Node node = JSON.parseObject(process, Node.class);

        //找出所有的路由节点id
        List<String> routeNodeIdList = com.cxygzl.common.utils.NodeUtil.selectRouteId(node);

        boolean match = routeNodeIdList.stream().anyMatch(w -> StrUtil.startWith(nodeId, w));
        if (!match) {
            return;
        }

        //


    }

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
                Node children = branch.getChildren();
                handleApproveForm(children, formItemVOList);
            }

        }

        handleApproveForm(node.getChildren(), formItemVOList);

    }


}
