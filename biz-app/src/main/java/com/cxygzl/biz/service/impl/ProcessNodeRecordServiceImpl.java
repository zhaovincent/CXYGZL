package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.constants.NodeStatusEnum;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessNodeRecord;
import com.cxygzl.biz.mapper.ProcessNodeRecordMapper;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.biz.service.IProcessNodeRecordService;
import com.cxygzl.common.dto.ProcessNodeRecordParamDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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

    /**
     * 节点开始
     *
     * @param processNodeRecordParamDto
     * @return
     */
    @Override
    public R start(ProcessNodeRecordParamDto processNodeRecordParamDto) {

        ProcessNodeRecord processNodeRecord = BeanUtil.copyProperties(processNodeRecordParamDto, ProcessNodeRecord.class);
        processNodeRecord.setStartTime(new Date());
        processNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());

        this.save(processNodeRecord);

        String parentNodeId = processNodeRecordParamDto.getParentNodeId();
        String nodeId = processNodeRecordParamDto.getNodeId();
        if(StrUtil.isNotBlank(parentNodeId)){

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processNodeRecordParamDto.getProcessInstanceId()).one();
            String data = processNodeDataService.getNodeData(processNodeRecordParamDto.getFlowId(), nodeId).getData();

            Node node = JSON.parseObject(processInstanceRecord.getProcess(), Node.class);
            NodeUtil.handleChildrenAfterJump(node,parentNodeId, JSON.parseObject(data, Node.class));

            log.info("node={}", com.alibaba.fastjson2.JSON.toJSONString(node));

        }

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
                .set(ProcessNodeRecord::getStatus,NodeStatusEnum.YJS.getCode())
                .set(ProcessNodeRecord::getEndTime,new Date())
                .set(ProcessNodeRecord::getData,processNodeRecordParamDto.getData())
                .eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId)
                .eq(ProcessNodeRecord::getNodeId, processNodeRecordParamDto.getNodeId())
//                .eq(ProcessNodeRecord::getExecutionId, processNodeRecordParamDto.getExecutionId())
                .update(new ProcessNodeRecord());

        //判断是否是动态路由，如果是动态路由  则要修改节点连线
        //NodeUtil.handleNodeLine(processInstanceId,processNodeRecordParamDto.getNodeId());
        return R.success();
    }
}
