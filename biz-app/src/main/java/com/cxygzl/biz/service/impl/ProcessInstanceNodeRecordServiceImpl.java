package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessInstanceNodeRecord;
import com.cxygzl.biz.mapper.ProcessInstanceNodeRecordMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.dto.ProcessInstanceNodeRecordParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.cxygzl.common.constants.ProcessInstanceConstant.MERGE_GATEWAY_FLAG;

/**
 * <p>
 * 流程节点记录 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Slf4j
@Service
public class ProcessInstanceNodeRecordServiceImpl extends ServiceImpl<ProcessInstanceNodeRecordMapper, ProcessInstanceNodeRecord> implements IProcessInstanceNodeRecordService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessNodeDataService processNodeDataService;
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceExecutionService processExecutionService;

    /**
     * 节点开始
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public R start(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {
        String flowId = processInstanceNodeRecordParamDto.getFlowId();

        String nodeId = processInstanceNodeRecordParamDto.getNodeId();
        String parentNodeId = processInstanceNodeRecordParamDto.getParentNodeId();

        log.info("开始节点：{} - {} - {} -{}", processInstanceNodeRecordParamDto.getNodeId(), processInstanceNodeRecordParamDto.getNodeName(), processInstanceNodeRecordParamDto.getExecutionId(), processInstanceNodeRecordParamDto.getFlowUniqueId());

        {
            //判断是否存在同一个节点 执行中了
            Long count = this.lambdaQuery()
                    .eq(ProcessInstanceNodeRecord::getProcessInstanceId,
                            processInstanceNodeRecordParamDto.getProcessInstanceId())
                    .eq(ProcessInstanceNodeRecord::getNodeId,
                            processInstanceNodeRecordParamDto.getNodeId())
                    .eq(ProcessInstanceNodeRecord::getFlowUniqueId,
                            processInstanceNodeRecordParamDto.getFlowUniqueId())
                    .eq(ProcessInstanceNodeRecord::getExecutionId,
                            processInstanceNodeRecordParamDto.getExecutionId())
                    .eq(ProcessInstanceNodeRecord::getStatus,
                            NodeStatusEnum.JXZ.getCode()).count();
            if (count > 0) {
                log.info("有进行中的 不处理了 直接返回");
                return R.success();
            }

        }
        ProcessInstanceNodeRecord processNodeRecord = BeanUtil.copyProperties(processInstanceNodeRecordParamDto, ProcessInstanceNodeRecord.class);
        processNodeRecord.setStartTime(new Date());
        processNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());
        if (processInstanceNodeRecordParamDto.getNodeType() != null && processInstanceNodeRecordParamDto.getNodeType() == NodeTypeEnum.END.getValue().intValue()) {
            processNodeRecord.setStatus(NodeStatusEnum.YJS.getCode());

        }

        //设置上级id
        String lastNodeId = com.cxygzl.biz.utils.NodeUtil.getLastNodeId(flowId, nodeId, parentNodeId);
        processNodeRecord.setParentNodeId(lastNodeId);

        this.save(processNodeRecord);


        //查询父级
        Process process = processService.getByFlowId(flowId);
        final Node rootNode = JSON.parseObject(process.getProcess(), Node.class);
        Node parentNode = NodeUtil.getParentNode(rootNode, nodeId);

        //当前流程
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceNodeRecordParamDto.getProcessInstanceId()).one();
        Node currentProcessRootNode = JSON.parseObject(processInstanceRecord.getProcess(), Node.class);


        if (parentNode != null) {
            if (parentNode.getType().intValue() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue()) {

                //处理排他分支为线性
                NodeUtil.handleExclusiveGatewayAsLine(currentProcessRootNode, parentNode.getId(), nodeId, null);
                processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));
            }
        }

        log.info("{}-{}上级跳转过来的id:{}", nodeId, processInstanceNodeRecordParamDto.getNodeName(), parentNodeId);

        if (StrUtil.isNotBlank(parentNodeId)) {
            //说明是跳转过来的 要重新构建流程树

            Node currentNode = processNodeDataService.getNode(flowId, nodeId).getData();
            // currentNode.setExecutionId(processNodeRecordParamDto.getExecutionId());

            NodeUtil.handleChildrenAfterJump(currentProcessRootNode, parentNodeId, currentNode, processInstanceNodeRecordParamDto.getExecutionId());
            processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));

        }

        //设置executionId
        NodeUtil.handleNodeAddExecutionIdFlowUniqueId(currentProcessRootNode,
                nodeId,
                processInstanceNodeRecordParamDto.getExecutionId(),
                processInstanceNodeRecordParamDto.getFlowUniqueId());
        processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));

        //判断是聚合网关--处理条件分支和包容分支 删除没用执行的
        if (StrUtil.endWith(processInstanceNodeRecordParamDto.getNodeId(), MERGE_GATEWAY_FLAG)) {
            Node node = processNodeDataService.getNode(processInstanceNodeRecordParamDto.getFlowId(),
                    StrUtil.replace(processInstanceNodeRecordParamDto.getNodeId(), MERGE_GATEWAY_FLAG, "")).getData();

            if (node.getType().intValue() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue()) {
                //排他
                NodeUtil.handleExclusiveGatewayAsLine(currentProcessRootNode, node.getId(), null, null);
                processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));
            }
            if (node.getType().intValue() == NodeTypeEnum.INCLUSIVE_GATEWAY.getValue()) {
                //包容

                //找到所有的执行节点
                List<ProcessInstanceNodeRecord> processNodeRecordList = this.lambdaQuery().eq(ProcessInstanceNodeRecord::getProcessInstanceId,
                        processInstanceRecord.getProcessInstanceId()).orderByDesc(ProcessInstanceNodeRecord::getCreateTime).list();

                List<ProcessInstanceNodeRecordParamDto> processInstanceNodeRecordParamDtos = BeanUtil.copyToList(processNodeRecordList, ProcessInstanceNodeRecordParamDto.class);

                //找到当前这个节点
                ProcessInstanceNodeRecord p = processNodeRecordList.stream().filter(w -> StrUtil.equals(w.getNodeId(), node.getId())).findFirst().get();

                NodeUtil.handleInclusiveGatewayAsLine(currentProcessRootNode, node.getId(), p.getExecutionId(), p.getFlowUniqueId(),
                        processInstanceNodeRecordParamDtos, null);
                processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));

            }

        }


        processInstanceRecordService.updateById(processInstanceRecord);

        return R.success();
    }

    /**
     * 节点结束
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public R endNodeEvent(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {

        String processInstanceId = processInstanceNodeRecordParamDto.getProcessInstanceId();

        this.lambdaUpdate()
                .set(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .set(ProcessInstanceNodeRecord::getEndTime, new Date())
                .set(ProcessInstanceNodeRecord::getData, processInstanceNodeRecordParamDto.getData())
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId)
                .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .eq(ProcessInstanceNodeRecord::getNodeId, processInstanceNodeRecordParamDto.getNodeId())

                .update(new ProcessInstanceNodeRecord());

        return R.success();
    }

    /**
     * 驳回
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public R cancelNodeEvent(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {
        log.info("节点取消：{}-{}", processInstanceNodeRecordParamDto.getNodeId(), processInstanceNodeRecordParamDto.getNodeName());
        String processInstanceId = processInstanceNodeRecordParamDto.getProcessInstanceId();
        String nodeId = processInstanceNodeRecordParamDto.getNodeId();
        this.lambdaUpdate().set(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.YCX.getCode())
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId)
                .eq(ProcessInstanceNodeRecord::getNodeId, nodeId)
                .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .update(new ProcessInstanceNodeRecord());
        return R.success();
    }
}
