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
     * 会签
     */
    public static final int MULTIPLE_MODE_AL_SAME=1;
    /**
     * 或签
     */
    public static final int MULTIPLE_MODE_ONE=2;
    /**
     * 顺签
     */
    public static final int MULTIPLE_MODE_ALL_SORT=3;

    public static class AssignedTypeClass{
        //指定用户
        public static final int USER=1;
        //发起人自己
        public static final int SELF=5;
        //表单人员
        public static final int FORM_USER=8;
        //表单部门
        public static final int FORM_DEPT=9;
        //指定主管
        public static final int LEADER=2;
        //连续多级主管
        public static final int LEADER_TOP=7;
        //发起人自选
        public static final int SELF_SELECT=4;
        //角色
        public static final int ROLE=3;
    }

    /**
     * 变量key
     */
    public static class VariableKey{
        //发起人
        public static final String STARTER="root";
        //结束
        public static final String END="end";
        //是否撤回
        public static final String CANCEL="cancel";
        //是否是被驳回到发起人节点
        public static final String REJECT_TO_STARTER_NODE ="rootReject";
        //是否子流程发起人处理发起人数据
        public static final String SUB_PROCESS_STARTER_NODE ="subProcessStartHandle";
        //审批描述
        public static final String APPROVE_DESC ="approveDesc";

    }

    /**
     * 任务类型
     */
    public static class TaskType{
        //通过
        public static final String PASS ="pass";
        //拒绝
        public static final String REFUSE="refuse";
    }
    /**
     * 用户任务---处理人和发起人一致
     */
    public static class UserTaskSameAsStarterHandler{
        //通过
        public static final String TO_PASS ="TO_PASS";
        //继续处理
        public static final String TO_CONTINUE ="TO_CONTINUE";
        //部门负责人
        public static final String TO_DEPT_LEADER="TO_DEPT_LEADER";
        //流程管理员
        public static final String TO_ADMIN="TO_ADMIN";
    }

    /**
     * 表单权限
     */
    public static class FormPermClass{
        //隐藏
        public static final String HIDE="H";
        //只读
        public static final String READ="R";
        //编辑
        public static final String EDIT="E";
    }

    /**
     * 子流程发起人模式
     */
    public static class SubProcessStarterMode{
        //同主流程发起人
        public static final Integer MAIN=1;
        //表单内人员
        public static final Integer FORM=2;
        //多实例表单单项
        public static final Integer MULTIPLE_FORM_SINGLE=3;
    }

    /**
     * 子流程多实例类型
     */
    public static class SubProcessMultipleMode{
        //固定数量
        public static final int FIX=1;
        //数字表单
        public static final int FORM_NUMBER=2;
        //多表单
        public static final int FORM_MULTIPLE=3;
    }

    /**
     * 条件符号
     */
    public static class ConditionSymbol{
        //相等
        public static final String EQUAL="==";
        //不等
        public static final String NOT_EQUAL="!=";

        //包含
        public static final String CONTAIN="contain";

        //不包含
        public static final String NOT_CONTAIN="notcontain";

        //存在于
        public static final String IN="in";
        //不存在于
        public static final String NOT_IN="notin";
        //重合
        public static final String INTERSECTION="intersection";
        //范围
        public static final String RANGE="range";
        //不为空
        public static final String NOT_EMPTY="notempty";
        //空
        public static final String EMPTY="empty";

    }
}
