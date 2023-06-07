package com.cxygzl.common.constants;

public class ProcessInstanceConstant {
    /**
     * 空执行人
     */
    public static final String DEFAULT_EMPTY_ASSIGN="-99999999";
    /**
     * 用户任务没有执行人的情况下如何处理
     * 自动通过
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_PASS ="TO_PASS";
    /**
     * 转交给管理员
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_ADMIN ="TO_ADMIN";
    /**
     * 指定人员
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_USER ="TO_USER";
    /**
     * 结束流程
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_END ="TO_END";
    /**
     * 拒绝之后 结束流程
     */
    public static final String USER_TASK_REFUSE_TYPE_TO_END ="TO_END";
    /**
     * 拒绝之后 到某个节点
     */
    public static final String USER_TASK_REFUSE_TYPE_TO_NODE ="TO_NODE";
}
