package com.cxygzl.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.cxygzl.core")
public class CoreApp {
    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class, args);
        log.info("=====================Core APP  Start========================");
    }

    @PostConstruct
    public void init(){
        String template = "#{#user>2}";//设置文字模板,其中#{}表示表达式的起止，#user是表达式字符串，表示引用一个变量。
        ExpressionParser paser = new SpelExpressionParser();//创建表达式解析器

        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user",1);

        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        Expression expression = paser.parseExpression(template,new TemplateParserContext());

        //使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
        System.out.println(expression.getValue(context,String.class));
    }
}
