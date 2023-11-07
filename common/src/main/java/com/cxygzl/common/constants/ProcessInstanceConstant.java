package com.cxygzl.common.constants;

import lombok.Data;

public class ProcessInstanceConstant {
    /**
     * 空执行人
     */
    public static final String DEFAULT_EMPTY_ASSIGN = "-99999999";
    /**
     * 用户任务没有执行人的情况下如何处理
     * 自动通过
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_PASS = "TO_PASS";
    /**
     * 转交给管理员
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_ADMIN = "TO_ADMIN";
    /**
     * 指定人员
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_USER = "TO_USER";
    /**
     * 自动拒绝
     */
    public static final String USER_TASK_NOBODY_HANDLER_TO_REFUSE = "TO_REFUSE";

    /**
     * 会签
     */
    public static final int MULTIPLE_MODE_ALL_SAME = 1;
    /**
     * 或签
     */
    public static final int MULTIPLE_MODE_ONE = 2;
    /**
     * 顺签
     */
    public static final int MULTIPLE_MODE_ALL_SORT = 3;
    /**
     * 聚合网关标识
     */
    public static final String MERGE_GATEWAY_FLAG = "_merge_gateway";

    /**
     * 流程设置 去重的value
     */
    @Data
    public static class ProcessSettingDistinctValueClass {
        //只要有一次审批通过 就去重
        public static final int ALL = 1;
        //仅在连续出现时，自动去重
        public static final int CONTINUED = 2;
    }

    /**
     * 审批人员类型
     */
    public static class AssignedTypeClass {
        //指定用户
        public static final int USER = 1;
        //发起人自己
        public static final int SELF = 5;
        //系统自动拒绝
        public static final int SYSTEM_REFUSE = 11;
        //表单人员
        public static final int FORM_USER = 8;
        //表单部门
        public static final int FORM_DEPT = 9;
        //指定部门主管
        public static final int FIXED_DEPT_LEADER = 10;
        //指定主管
        public static final int LEADER = 2;
        //连续多级主管
        public static final int LEADER_TOP = 7;
        //发起人自选
        public static final int SELF_SELECT = 4;
        //角色
        public static final int ROLE = 3;
    }

    /**
     * 审批人是表单部门时  用户类型
     */
    public static class AssignedTypeFormDeptUserTypeClass {
        /**
         * 部门人员
         */
        public static final String ALL_USER = "allUser";
        /**
         * 主管
         */
        public static final String LEADER = "leader";
    }

    /**
     * 变量key
     */
    public static class VariableKey {
        //系统编码
        public static final String SYS_CODE = "cxygzl";
        //发起人
        public static final String STARTER = "root";
        //流程编号
        public static final String PROCESS_INSTANCE_CODE = "processInstanceCode";
        //结束
        public static final String END = "end";
        //是否撤回
        public static final String CANCEL = "cancel";
        //是否是被驳回到发起人节点
        public static final String REJECT_TO_STARTER_NODE = "rootReject";
        //是否子流程发起人处理发起人数据
        public static final String SUB_PROCESS_STARTER_NODE = "subProcessStartHandle";
        // 支持自动跳过
        public static final String ENABLE_SKIP_EXPRESSION = "_ACTIVITI_SKIP_EXPRESSION_ENABLED";


        //审批结果 boolean
        public static final String APPROVE_RESULT = "approveResult";
        //任务类型
        public static final String TASK_TYPE = "taskType";

        public static final String APPROVE_NODE_RESULT = "approveNodeResult";


        //流程唯一id
        public static final String FLOW_UNIQUE_ID = "flowUniqueId";
        //流程id
        public static final String FLOW_ID = "flowDefinitionId";

        //委派的状态
        public static final String PENDING = "PENDING";

    }



    /**
     * 用户任务---处理人和发起人一致
     */
    public static class UserTaskSameAsStarterHandler {
        //通过
        public static final String TO_PASS = "TO_PASS";
        //继续处理
        public static final String TO_CONTINUE = "TO_CONTINUE";
        //部门负责人
        public static final String TO_DEPT_LEADER = "TO_DEPT_LEADER";
        //流程管理员
        public static final String TO_ADMIN = "TO_ADMIN";
    }

    /**
     * 用户任务---用户拒绝
     */
    public static class UserTaskRefuseHandler {
        //结束
        public static final String TO_END = "TO_END";
        //驳回节点
        public static final String TO_NODE = "TO_NODE";

    }

    /**
     * 表单权限
     */
    public static class FormPermClass {
        //隐藏
        public static final String HIDE = "H";
        //只读
        public static final String READ = "R";
        //编辑
        public static final String EDIT = "E";
    }

    /**
     * 子流程发起人模式
     */
    public static class SubProcessStarterMode {
        //同主流程发起人
        public static final int MAIN = 1;
        //表单内人员
        public static final int FORM = 2;
        //多实例表单单项
        public static final int MULTIPLE_FORM_SINGLE = 3;
    }

    /**
     * 子流程多实例类型
     */
    public static class SubProcessMultipleMode {
        //固定数量
        public static final int FIX = 1;
        //数字表单
        public static final int FORM_NUMBER = 2;
        //多表单
        public static final int FORM_MULTIPLE = 3;
    }

    /**
     * 条件符号
     */
    public static class ConditionSymbol {
        //相等
        public static final String EQUAL = "==";
        //不等
        public static final String NOT_EQUAL = "!=";

        //包含
        public static final String CONTAIN = "contain";

        //不包含
        public static final String NOT_CONTAIN = "notcontain";

        //存在于
        public static final String IN = "in";
        //不存在于
        public static final String NOT_IN = "notin";
        //重合
        public static final String INTERSECTION = "intersection";
        //范围
        public static final String RANGE = "range";
        //角色
        public static final String ROLE = "role";
        //不为空
        public static final String NOT_EMPTY = "notempty";
        //空
        public static final String EMPTY = "empty";

    }
}
