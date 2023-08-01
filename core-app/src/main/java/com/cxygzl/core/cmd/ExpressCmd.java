package com.cxygzl.core.cmd;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.util.CommandContextUtil;

import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-01 14:23
 */
public class ExpressCmd implements Command<Object> {

    private String conditionExpression;
    private Map<String, Object> param;

    public ExpressCmd(String conditionExpression, Map<String, Object> param) {
        this.conditionExpression = conditionExpression;
        this.param = param;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        ExpressionManager expressionManager = CommandContextUtil.getProcessEngineConfiguration().getExpressionManager();
        Expression expression = expressionManager.createExpression(conditionExpression);

        DelegateExecution delegateExecution = new ExecutionEntityImpl();
        delegateExecution.setTransientVariables(param);

        return expression.getValue(delegateExecution);
    }
}
