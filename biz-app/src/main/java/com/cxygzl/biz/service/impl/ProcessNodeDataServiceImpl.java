package com.cxygzl.biz.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.ProcessNodeData;
import com.cxygzl.biz.mapper.ProcessNodeDataMapper;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.ProcessNodeDataDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程节点数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Slf4j
@Service
public class ProcessNodeDataServiceImpl extends ServiceImpl<ProcessNodeDataMapper, ProcessNodeData> implements IProcessNodeDataService {

    private LRUCache<String, String> cache = CacheUtil.newLRUCache(1000);


    /**
     * 保存流程节点数据
     *
     * @param processNodeDataDto
     * @return
     */
    @Override
    public R saveNodeData(ProcessNodeDataDto processNodeDataDto) {

        ProcessNodeData processNodeData = BeanUtil.copyProperties(processNodeDataDto, ProcessNodeData.class);
        this.save(processNodeData);


        return R.success();
    }

    /***
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    @Override
    public R<String> getNodeData(String flowId, String nodeId) {

        String o = cache.get(StrUtil.format("{}||{}", flowId, nodeId));
        if (StrUtil.isNotBlank(o)) {
            log.debug("从缓存获取到数据 :{}  {} {}", flowId, nodeId, o);
            return R.success(o);
        }


        //发起人用户任务
        if (StrUtil.startWith(nodeId, ProcessInstanceConstant.VariableKey.STARTER)) {
            nodeId = ProcessInstanceConstant.VariableKey.STARTER;
        }

        ProcessNodeData processNodeData = this.lambdaQuery().eq(ProcessNodeData::getFlowId, flowId).eq(ProcessNodeData::getNodeId, nodeId).one();

        if (processNodeData != null) {

            cache.put(StrUtil.format("{}||{}", flowId, nodeId), processNodeData.getData());

        }
        if (processNodeData == null) {
            return R.fail("数据不存在");
        }

        return R.success(processNodeData == null ? null : processNodeData.getData());
    }

    @Override
    public R<Node> getNode(String flowId, String nodeId) {
        String data = getNodeData(flowId, nodeId).getData();
        return R.success(JsonUtil.parseObject(data, Node.class));
    }
}
