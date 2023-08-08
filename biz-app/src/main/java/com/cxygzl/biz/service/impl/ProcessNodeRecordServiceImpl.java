package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessExecution;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessNodeRecord;
import com.cxygzl.biz.mapper.ProcessNodeRecordMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.common.constants.NodeTypeEnum;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
public class ProcessNodeRecordServiceImpl extends ServiceImpl<ProcessNodeRecordMapper, ProcessNodeRecord> implements IProcessNodeRecordService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessNodeDataService processNodeDataService;
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessExecutionService processExecutionService;

    /**
     * 节点开始
     *
     * @param processNodeRecordParamDto
     * @return
     */
    @Override
    public R start(ProcessNodeRecordParamDto processNodeRecordParamDto) {

        Long count = this.lambdaQuery().eq(ProcessNodeRecord::getExecutionId, processNodeRecordParamDto.getExecutionId()).count();
        if (count > 0) {
            return R.success();
        }
        List<String> childExecutionId = processNodeRecordParamDto.getChildExecutionId();
        if (CollUtil.isNotEmpty(childExecutionId)) {
            //子级

            for (String s : childExecutionId) {
                ProcessExecution entity = new ProcessExecution();
                entity.setChildExecutionId(s);
                entity.setExecutionId(processNodeRecordParamDto.getExecutionId());
                processExecutionService.save(entity);
            }
        }

        ProcessNodeRecord processNodeRecord = BeanUtil.copyProperties(processNodeRecordParamDto, ProcessNodeRecord.class);
        processNodeRecord.setStartTime(new Date());
        processNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());

        this.save(processNodeRecord);

        String flowId = processNodeRecordParamDto.getFlowId();

        String parentNodeId = processNodeRecordParamDto.getParentNodeId();
        String nodeId = processNodeRecordParamDto.getNodeId();
        //查询父级
        Process process = processService.getByFlowId(flowId);
        final Node rootNode = JSON.parseObject(process.getProcess(), Node.class);
        Node parentNode = NodeUtil.getParentNode(rootNode, nodeId);

        //当前流程
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processNodeRecordParamDto.getProcessInstanceId()).one();
        Node currentProcessRootNode = JSON.parseObject(processInstanceRecord.getProcess(), Node.class);

        //设置executionId
        NodeUtil.handleNodeAddExecutionId(currentProcessRootNode, nodeId, processNodeRecordParamDto.getExecutionId());
        processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));

        if (parentNode != null) {
            if (parentNode.getType().intValue() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue()) {

                //处理排他分支为线性
                NodeUtil.handleExclusiveGatewayAsLine(currentProcessRootNode, parentNode.getId(), nodeId);
                processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));
            }
        }

        log.info("{}-{}上级跳转过来的id:{}", nodeId, processNodeRecordParamDto.getNodeName(), parentNodeId);

        if (StrUtil.isNotBlank(parentNodeId)) {
            //说明是跳转过来的 要重新构建流程树

            Node currentNode = processNodeDataService.getNode(flowId, nodeId).getData();

            NodeUtil.handleChildrenAfterJump(currentProcessRootNode, parentNodeId, currentNode, processNodeRecordParamDto.getExecutionId());
            processInstanceRecord.setProcess(JSON.toJSONString(currentProcessRootNode));

        }
        processInstanceRecordService.updateById(processInstanceRecord);

        return R.success();
    }

    /**
     * 节点结束
     *
     * @param processNodeRecordParamDto
     * @return
     */
    @Override
    public R endNodeEvent(ProcessNodeRecordParamDto processNodeRecordParamDto) {

        String processInstanceId = processNodeRecordParamDto.getProcessInstanceId();

        this.lambdaUpdate()
                .set(ProcessNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .set(ProcessNodeRecord::getEndTime, new Date())
                .set(ProcessNodeRecord::getData, processNodeRecordParamDto.getData())
                .eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId)
                .eq(ProcessNodeRecord::getNodeId, processNodeRecordParamDto.getNodeId())
//                .eq(ProcessNodeRecord::getExecutionId, processNodeRecordParamDto.getExecutionId())
                .update(new ProcessNodeRecord());

        //判断是否是动态路由，如果是动态路由  则要修改节点连线
        //NodeUtil.handleNodeLine(processInstanceId,processNodeRecordParamDto.getNodeId());
        return R.success();
    }
}
