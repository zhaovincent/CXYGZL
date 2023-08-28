package com.cxygzl.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;

public class NodeUtil {

    public static String getFlowId(String processDefinitionId) {
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);


        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();


        return processDefinition.getKey();
    }

}
