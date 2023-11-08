package com.cxygzl.core.listeners.event_listener_impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.ProcessInstanceRecordParamDto;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.dto.flow.HttpSettingData;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.listeners.EventListenerStrategy;
import com.cxygzl.core.utils.BizHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.*;

/**
 * 流程开始了
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-10-10 10:12
 */
@Slf4j
@Component
public class ProcessStartedEventListener implements EventListenerStrategy, InitializingBean {
    /**
     * 处理数据
     *
     * @param event
     * @return
     */
    @Override
    public void handle(FlowableEvent event) {

        //流程开始了
        FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

        ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
        DelegateExecution execution = flowableProcessStartedEvent.getExecution();
        String flowId = entity.getProcessDefinitionKey();
        //上级实例id
        String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();
        {
            //设置唯一id
            execution.setVariable(FLOW_UNIQUE_ID, IdUtil.fastSimpleUUID());
            execution.setVariable(FLOW_ID, flowId);
//                //判断整体流程默认是通过
//                execution.setVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT),
//                        ProcessInstanceConstant.ApproveResult.OK);
            if (StrUtil.isNotBlank(nestedProcessInstanceId)) {
                //子流程发起人处理表单
                execution.setVariable(SUB_PROCESS_STARTER_NODE, true);
            } else {
                execution.setVariable(SUB_PROCESS_STARTER_NODE, false);
            }
        }

        String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();

        Object variable = execution.getVariable(
                ProcessInstanceConstant.VariableKey.STARTER);
        String startUserId = (JsonUtil.parseArray(JsonUtil.toJSONString(variable), NodeUser.class).get(0).getId());


        Map<String, Object> variables = execution.getVariables();
        log.info("流程开始了变量是：{}", JsonUtil.toJSONString(variables));


        FlowSettingDto flowSettingDto = BizHttpUtil.queryProcessSetting(flowId).getData();
        {


            //流程编号
            String processInstanceBizCode = StrUtil.format("BIZ{}{}", DateUtil.format(new Date(), "yyyyMMddHHmmss"), RandomUtil.randomStringUpper(6));
            FlowSettingDto.CustomRule customRule = flowSettingDto.getCustomRule();
            if (customRule != null && customRule.getEnable()) {
                String prefix = customRule.getPrefix();
                Integer middle = customRule.getMiddle();
                Integer serino = customRule.getSerino();

                String middleS = "";
                if (middle == 2) {
                    middleS = "-";
                }
                if (middle == 3) {
                    middleS = "|";
                }
                if (middle == 4) {
                    middleS = DateUtil.format(new Date(), "yyyyMMdd");
                }

                StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

                Long increment = redisTemplate.opsForValue().increment(StrUtil.format("rediskey-{}-{}",
                      prefix,middleS));

                processInstanceBizCode = StrUtil.format("{}{}{}", prefix, middleS, StrUtil.fillBefore(String.valueOf(increment), '0', serino));

            }
            RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
            runtimeService.setVariable(processInstanceId, PROCESS_INSTANCE_CODE,processInstanceBizCode);

            ProcessInstanceRecordParamDto processInstanceRecordParamDto = new ProcessInstanceRecordParamDto();
            processInstanceRecordParamDto.setUserId(startUserId);
            processInstanceRecordParamDto.setParentProcessInstanceId(nestedProcessInstanceId);
            processInstanceRecordParamDto.setFlowId(flowId);
            processInstanceRecordParamDto.setProcessInstanceId(processInstanceId);
            processInstanceRecordParamDto.setProcessInstanceBizKey(entity.getProcessInstanceBusinessKey());
            processInstanceRecordParamDto.setProcessInstanceBizCode(processInstanceBizCode);
            processInstanceRecordParamDto.setFormData(JsonUtil.toJSONString(variables));
            BizHttpUtil.startProcessEvent(processInstanceRecordParamDto);
        }
        {
            //判断前置事件
            if (flowSettingDto != null) {
                HttpSetting frontNotify = flowSettingDto.getFrontNotify();
                if (frontNotify != null && frontNotify.getEnable()) {

                    String result = HttpUtil.flowExtenstionHttpRequest(frontNotify, variables, flowId, processInstanceId, null, null);


                    if (StrUtil.isNotBlank(result)) {
                        Map<String, Object> resultMap = JsonUtil.parseObject(result, new JsonUtil.TypeReference<Map<String, Object>>() {
                        });
                        List<HttpSettingData> resultSetting = frontNotify.getResult();
                        for (HttpSettingData httpSettingData : resultSetting) {
                            execution.setVariable(httpSettingData.getValue(), resultMap.get(httpSettingData.getField()));
                        }
                    }

                }
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FlowableEngineEventType.PROCESS_STARTED.toString());

    }
}
