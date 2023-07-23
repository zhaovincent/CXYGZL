package com.cxygzl.common.config;

import java.lang.annotation.*;

/**
 * Vincent 创建于 2019/7/15.
 */

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotWriteLogAnno {

    boolean exclude() default false;


    //所有的字段都不记录日志
     boolean all() default true;
     //排除记录日志的字段
     String[] paramsExclude() default {};

    /**
     * 是否打印结果日志
     * @return
     */
     boolean printResultLog() default  true;
}
