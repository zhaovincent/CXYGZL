package com.cxygzl.core.cmd;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 16:44
 */


public class ExpressionCmd implements Command<Boolean>, Serializable {
    protected RuntimeService runtimeService;

    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    protected String processInstanceId;

    protected String exp;

    protected Map<String, Object> variableMap;

    public ExpressionCmd(RuntimeService runtimeService, ProcessEngineConfigurationImpl processEngineConfiguration, String processInstanceId, String exp, Map<String, Object> variableMap) {
        this.runtimeService = runtimeService;
        this.processEngineConfiguration = processEngineConfiguration;
        this.processInstanceId = processInstanceId;
        this.exp = exp;
        this.variableMap = variableMap;
    }

    @Override
    public Boolean execute(CommandContext commandContext) {
        Expression expression = processEngineConfiguration.getExpressionManager().createExpression(this.exp);
        ExecutionEntity executionEntity;
        if(StringUtils.isNotBlank(this.processInstanceId)){
            executionEntity = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(this.processInstanceId).includeProcessVariables().singleResult();
        }else {
            executionEntity = new ExecutionEntityImpl();
            executionEntity.setVariables(variableMap);
        }
        Object value = expression.getValue(executionEntity);
        return value != null && "true".equals(value.toString());
    }
}