package com.cxygzl.core.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.*;
import com.cxygzl.common.dto.flow.HttpSetting;
import com.cxygzl.common.utils.HttpUtil;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.service.IFlowService;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static com.cxygzl.common.constants.ProcessInstanceConstant.VariableKey.ENABLE_SKIP_EXPRESSION;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 16:40
 */
@Component
@Slf4j
public class FlowServiceImpl implements IFlowService {
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;


    @Resource
    private RuntimeService runtimeService;


    /**
     * 创建流程模型
     *
     * @param createFlowDto
     * @return
     */
    @Transactional
    @Override
    public R create(CreateFlowDto createFlowDto) {
        String flowId = "p" + RandomUtil.randomString(9) + StrUtil.fillBefore(createFlowDto.getUserId(), '0', 10);


        log.info("flowId={}", flowId);
        BpmnModel bpmnModel = ModelUtil.buildBpmnModel(createFlowDto.getNode(), createFlowDto.getProcessName(), flowId);
        {
            byte[] bpmnBytess = new BpmnXMLConverter().convertToXML(bpmnModel);
            String filename = "/tmp/flowable-deployment/" + flowId + ".bpmn20.xml";
            log.debug("部署时的模型文件：{}", filename);
            FileUtil.writeBytes(bpmnBytess, filename);
        }
        repositoryService.createDeployment()
                .addBpmnModel(StrUtil.format("{}.bpmn20.xml", flowId), bpmnModel).deploy();


        return R.success(flowId);
    }

    /**
     * 发起流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @Transactional
    @Override
    public R start(ProcessInstanceParamDto processInstanceParamDto) {
        String flowId = processInstanceParamDto.getFlowId();
        {
            //前置检查
            R r = frontCheck(processInstanceParamDto);
            if (!r.isOk()) {
                return r;
            }
        }
        Authentication.setAuthenticatedUserId(processInstanceParamDto.getStartUserId());
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        //支持自动跳过
        paramMap.put(ENABLE_SKIP_EXPRESSION, true);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowId,processInstanceParamDto.getBizKey(),
                paramMap);

        String processInstanceId = processInstance.getProcessInstanceId();
        return R.success(processInstanceId);
    }

    /**
     * 前置检查
     *
     * @param processInstanceParamDto
     * @return
     */
    private R frontCheck(ProcessInstanceParamDto processInstanceParamDto) {
        String flowId = processInstanceParamDto.getFlowId();
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        //前置检查
        FlowSettingDto flowSettingDto = BizHttpUtil.queryProcessSetting(flowId).getData();

        if (flowSettingDto != null) {
            HttpSetting frontCheck = flowSettingDto.getFrontCheck();
            if (frontCheck != null && frontCheck.getEnable()) {

                String result = HttpUtil.flowExtenstionHttpRequest(frontCheck, paramMap, flowId, null, null);


                if (StrUtil.isNotBlank(result)) {
                    R r = JsonUtil.parseObject(result, new JsonUtil.TypeReference<R>() {
                    });
                    return r;
                } else {
                    return R.fail("网络连接异常");
                }

            }
        }
        return R.success();
    }

    /**
     * 消息唤醒异步触发器
     *
     * @param messageDto
     * @return
     */
    @Transactional
    @Override
    public R notifyMsg(NotifyMessageDto messageDto) {
        String msgId = NodeDataStoreFactory.getInstance().get("msgId", messageDto.getMessageNotifyId());
        String nodeId = NodeDataStoreFactory.getInstance().get("nodeId", messageDto.getMessageNotifyId());


        Execution execution = runtimeService.createExecutionQuery()
                .activityId(nodeId)
                .processInstanceId(messageDto.getProcessInstanceId())
                .singleResult();

        runtimeService.messageEventReceived(msgId, execution.getId());
        return R.success();
    }


}
