package com.cxygzl.core.listeners;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeHttpDto;
import com.cxygzl.common.dto.process.NodeHttpHeaderDto;
import com.cxygzl.common.dto.process.NodePropDto;
import com.cxygzl.common.utils.NodeUtil;
import com.cxygzl.core.node.INodeDataStoreHandler;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.vo.NodeHttpResultVO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触发器监听器
 */
@Slf4j
public class TriggerListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        TaskEntityImpl task= (TaskEntityImpl) delegateTask;

        String processInstanceId = delegateTask.getProcessInstanceId();

        //processIde16ap:1:5d36c45b-e24e-11ed-8c81-366f243011ad
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        String processId = NodeUtil.getProcessId(processDefinitionId);

        //创建流程模型的时候定义的节点id
        String taskDefinitionKey = task.getTaskDefinitionKey();





        log.debug("触发器开始执行：{} class={} processInstanceId={},processDefinitionId={} nodeId={}", delegateTask.getId(),
                delegateTask.getClass().getCanonicalName(),processInstanceId,processDefinitionId,taskDefinitionKey);

        //获取节点数据
        INodeDataStoreHandler nodeDataStoreHandler= NodeDataStoreFactory.getInstance();
        String data = nodeDataStoreHandler.get(processId, taskDefinitionKey);
        log.debug("获取节点数据：{}",data);
        NodeDto nodeDto= JSON.parseObject(data,NodeDto.class);
        NodePropDto props = nodeDto.getProps();

        String type = props.getType();


        Map<String,Object> variables=new HashMap<>();


        if(StrUtil.equals(type,"WEBHOOK")){
            NodeHttpDto http = props.getHttp();

            List<NodeHttpHeaderDto> headers = http.getHeaders();
            Map<String,List<String>> headerMap=new HashMap<>();
            if(CollUtil.isNotEmpty(headers)){
                for (NodeHttpHeaderDto header : headers) {
                    Boolean isField = header.getIsField();
                    String name = header.getName();
                    String value = header.getValue();
                    if(isField){
                        Object variable = task.getVariable(value);
                        if(variable!=null){
                            headerMap.put(name,CollUtil.newArrayList(Convert.toStr(variable)));
                        }
                    }else{
                        headerMap.put(name,CollUtil.newArrayList(value));
                    }
                }
            }
            List<NodeHttpHeaderDto> params = http.getParams();
            Map<String,String> paramMap=new HashMap<>();
            if(CollUtil.isNotEmpty(params)){
                for (NodeHttpHeaderDto header : params) {
                    Boolean isField = header.getIsField();
                    String name = header.getName();
                    String value = header.getValue();
                    if(isField){
                        Object variable = task.getVariable(value);
                        paramMap.put(name,variable==null?"":variable.toString());
                    }else{
                        paramMap.put(name,value);
                    }
                }
            }
            //流程变量

            //http请求
            try {
                String resultContent = HttpRequest.post(http.getUrl())
                        .header(headerMap)
                        .body(JSON.toJSONString(paramMap))
                        .timeout(10 * 1000)
                        .execute().body();
                log.debug("请求结果：{}",resultContent);
                NodeHttpResultVO nodeHttpResultVO = JSON.parseObject(resultContent, NodeHttpResultVO.class);
                if(nodeHttpResultVO.getOk()){
                    variables.put(StrUtil.format("{}_trigger_gateway_condition",taskDefinitionKey),true);

                }else{
                    //失败
                    variables.put(StrUtil.format("{}_trigger_gateway_condition",taskDefinitionKey),false);
                }
            } catch (Exception e) {
                log.error("Error:",e);
                variables.put(StrUtil.format("{}_trigger_gateway_condition",taskDefinitionKey),false);
            }
        }


        TaskService taskService = SpringUtil.getBean(TaskService.class);

        taskService.complete(delegateTask.getId(),variables);

    }
}
