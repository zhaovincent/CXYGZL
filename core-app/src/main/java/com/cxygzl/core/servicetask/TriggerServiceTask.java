package com.cxygzl.core.servicetask;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.List;
import java.util.Map;

/**
 * 触发器任务处理器--java服务任务
 */
@Slf4j
public class TriggerServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String nodeId = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();
        String processInstanceId = entity.getProcessInstanceId();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);


        Map<String, Object> variables = execution.getVariables();


        //判断后置事件

        HttpSetting backNotify = node.getHttpSetting();

        String result = null;
        try {

            result =  HttpUtil.flowExtenstionHttpRequest(backNotify, variables, flowId, processInstanceId, null);

            log.info(" 返回值:{}", result);
        } catch (Exception e) {
            log.error("触发器异常", e);
        }

        if (StrUtil.isNotBlank(result)) {
            Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
            });
            List<HttpSettingData> resultSetting = backNotify.getResult();
            for (HttpSettingData httpSettingData : resultSetting) {
                String value = httpSettingData.getValue();
                if (StrUtil.isNotBlank(value)) {
                    execution.setVariable(value, resultMap.get(httpSettingData.getField()));
                }
            }
        }


    }
}
