package com.cxygzl.core.expression.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.common.dto.flow.GroupCondition;
import com.cxygzl.common.dto.flow.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeExpressionStrategyFactory {

    private static final Map<String, NodeConditionStrategy> STRATEGY_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 提供获取策略的方法
     *
     * @param key
     * @return
     */
    public static NodeConditionStrategy getStrategy(String key) {
        NodeConditionStrategy sendService = STRATEGY_CONCURRENT_HASH_MAP.get(key);
        return sendService;
    }

    /**
     * 在Bean属性初始化后执行该方法
     *
     * @param key                  批次类型枚举
     * @param nodeConditionHandler 表达式处理接口
     */
    public static void register(String key, NodeConditionStrategy nodeConditionHandler) {
        STRATEGY_CONCURRENT_HASH_MAP.put(key, nodeConditionHandler);
    }


    public static String handleSingleCondition(Condition nodeConditionDto) {
        NodeConditionStrategy nodeConditionHandler = getStrategy(nodeConditionDto.getKeyType());
        if (nodeConditionHandler == null) {
            return "(1==1)";
        }
        return nodeConditionHandler.handle(nodeConditionDto);
    }

    /**
     * 组内处理表达式
     *
     * @param groupDto
     * @return
     */
    public static String handleGroupCondition(GroupCondition groupDto) {

        List<String> exps = new ArrayList<>();


        for (Condition condition : groupDto.getConditionList()) {
            String singleExpression = handleSingleCondition(condition);
            exps.add(singleExpression);
        }
        Boolean mode = groupDto.getMode();

        if (!mode) {
            String join = CollUtil.join(exps, "||");

            return "(" + join + ")";
        }

        String join = CollUtil.join(exps, "&&");
        return "(" + join + ")";
    }

    /**
     * 处理单个分支表达式
     *
     * @return
     */
    public static String handle(Node node) {

        List<String> exps = new ArrayList<>();


        List<GroupCondition> groups = node.getConditionList();
        if (CollUtil.isEmpty(groups)) {
            return "${1==1}";
        }
        for (GroupCondition group : groups) {
            String s = handleGroupCondition(group);
            exps.add(s);
        }

        if (node.getGroupRelationMode()) {

            if (!node.getMode()) {
                String join = CollUtil.join(exps, "||");
                return "${(" + join + ")}";
            }

            String join = CollUtil.join(exps, "&&");
            return "${(" + join + ")}";
        }
        String groupRelation = node.getGroupRelation();

        groupRelation = StrUtil.replace(groupRelation, "$($", "(");
        groupRelation = StrUtil.replace(groupRelation, "$)$", ")");
        groupRelation = StrUtil.replace(groupRelation, "$或$", "||");
        groupRelation = StrUtil.replace(groupRelation, "$且$", "&&");

        int index=0;
        for (String exp : exps) {
            groupRelation = StrUtil.replace(groupRelation, "$条件组"+(index+1)+"$", exp);

            index++;

        }

        return "${(" + groupRelation + ")}";


    }

    /**
     * 处理分支表达式分支表达式
     *
     * @param branchs      所有分支
     * @param currentIndex
     * @return
     */
    public static String handleDefaultBranch(List<Node> branchs,int currentIndex) {

        List<String> expList = new ArrayList<>();


        int index = 1;
        for (Node branch : branchs) {

            if (index == currentIndex+1) {
                continue;
            }

            String exp = handle(branch);
            String s = StrUtil.subBetween(exp, "${", "}");
            expList.add(StrUtil.format("({})", s));

            index++;
        }
        String join = StrUtil.format("!({})", CollUtil.join(expList, "||"));
        return "${" + join + "}";
    }

}
