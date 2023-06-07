package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cxygzl.biz.entity.ProcessNodeData;
import com.cxygzl.biz.mapper.ProcessNodeDataMapper;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程节点数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
public class ProcessNodeDataServiceImpl extends ServiceImpl<ProcessNodeDataMapper, ProcessNodeData> implements IProcessNodeDataService {
    /**
     * 保存流程节点数据
     *
     * @param processNodeDataDto
     * @return
     */
    @Override
    public R saveNodeData(ProcessNodeDataDto processNodeDataDto) {

        ProcessNodeData processNodeData= BeanUtil.copyProperties(processNodeDataDto,ProcessNodeData.class);
        this.save(processNodeData);



        return R.success();
    }

    /***
     * 获取节点数据
     * @param processId
     * @param nodeId
     * @return
     */
    @Override
    public R<String> getNodeData(String processId, String nodeId) {
        ProcessNodeData processNodeData = this.lambdaQuery().eq(ProcessNodeData::getProcessId, processId).eq(ProcessNodeData::getNodeId, nodeId).one();
        return R.success(processNodeData==null?null:processNodeData.getData());
    }
}
