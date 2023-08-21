package com.cxygzl.core.expression.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.common.dto.flow.GroupCondition;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
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

    /**
     * 返回表达式
     *
     * @param nodeConditionDto
     * @return
     */
    public static String handleSingleCondition(Condition nodeConditionDto) {
        NodeConditionStrategy nodeConditionHandler = getStrategy(nodeConditionDto.getKeyType());
        if (nodeConditionHandler == null) {
            return "(1==1)";
        }
        return nodeConditionHandler.handleExpression(nodeConditionDto);
    }

    /**
     * 返回表达式
     *
     * @param nodeConditionDto
     * @return
     */
    public static String handleSingleConditionExpression(Condition nodeConditionDto) {
        NodeConditionStrategy nodeConditionHandler = getStrategy(nodeConditionDto.getKeyType());
        if (nodeConditionHandler == null) {
            return "(1==1)";
        }
        String s = IdUtil.fastSimpleUUID();
        NodeDataStoreFactory.getInstance().saveAll(s, s, nodeConditionDto);

        return StrUtil.format("(expressionHandler.handle(execution,\"{}\"))", s);

    }


    /**
     * 表达式结果处理
     *
     * @param condition 表达式对象
     * @param paramMap  所有的参数
     * @return
     */
    public static boolean handleSingleConditionResult(Condition condition, Map<String, Object> paramMap) {

        NodeConditionStrategy nodeConditionHandler = getStrategy(condition.getKeyType());

        return nodeConditionHandler.handleResult(condition, paramMap);
    }

    /**
     * 组内处理表达式结果
     *
     * @param groupDto
     * @return
     */
    public static boolean handleGroupConditionResult(GroupCondition groupDto, Map<String, Object> paramMap) {

        List<Boolean> exps = new ArrayList<>();


        for (Condition condition : groupDto.getConditionList()) {
            boolean b = handleSingleConditionResult(condition, paramMap);
            exps.add(b);
        }
        Boolean mode = groupDto.getMode();

        if (!mode) {
//或
            return exps.stream().anyMatch(w -> w);
        }

        return !exps.stream().anyMatch(w -> !w);
    }

    /**
     * 组内处理表达式
     *
     * @param groupDto
     * @return
     */
    public static String handleGroupConditionExpression(GroupCondition groupDto) {

        List<String> exps = new ArrayList<>();


        for (Condition condition : groupDto.getConditionList()) {
            String singleExpression = handleSingleConditionExpression(condition);
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


    public static boolean handle(Node node, Map<String, Object> paramMap) {
        List<Boolean> exps = new ArrayList<>();


        List<GroupCondition> groups = node.getConditionList();
        if (CollUtil.isEmpty(groups)) {
            return true;
        }
        for (GroupCondition group : groups) {
            boolean s = handleGroupConditionResult(group,paramMap);
            exps.add(s);
        }

        if (node.getGroupRelationMode()) {

            if (!node.getMode()) {
                String join = CollUtil.join(exps, "||");
               // return "${(" + join + ")}";

                return DataUtil.expression("${(" + join + ")}", Dict.create());
            }

            String join = CollUtil.join(exps, "&&");
//            return "${(" + join + ")}";

            return DataUtil.expression("${(" + join + ")}", Dict.create());

        }
        Object groupRelation = node.getGroupRelation();


        List<Map> mapList = Convert.toList(Map.class, groupRelation);

        StringBuilder expStr = new StringBuilder();
        for (Map map : mapList) {
            String str = MapUtil.getStr(map, "exp");

            expStr.append(str);

        }


        String expStrString = expStr.toString();

        int index = 0;
        for (boolean exp : exps) {
            expStrString = StrUtil.replace(expStrString, "c" + (index + 1), Convert.toStr(exp));

            index++;

        }

//        return "${(" + expStrString + ")}";

        return DataUtil.expression("${(" + expStrString + ")}", Dict.create());


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
            String s = handleGroupConditionExpression(group);
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
        Object groupRelation = node.getGroupRelation();


        List<Map> mapList = Convert.toList(Map.class, groupRelation);

        StringBuilder expStr = new StringBuilder();
        for (Map map : mapList) {
            String str = MapUtil.getStr(map, "exp");

            expStr.append(str);

        }


        String expStrString = expStr.toString();

        int index = 0;
        for (String exp : exps) {
            expStrString = StrUtil.replace(expStrString, "c" + (index + 1), exp);

            index++;

        }

        return "${(" + expStrString + ")}";


    }

    /**
     * 处理分支表达式分支表达式
     *
     * @param branchs      所有分支
     * @param currentIndex
     * @return
     */
    public static String handleDefaultBranch(List<Node> branchs, int currentIndex) {

        List<String> expList = new ArrayList<>();


        int index = 1;
        for (Node branch : branchs) {

            if (index == currentIndex + 1) {
                break;
            }

            String exp = handle(branch);
            String s = StrUtil.subBetween(exp, "${", "}");
            expList.add(StrUtil.format("({})", s));

            index++;
        }

//        String join = StrUtil.format("!({})", CollUtil.join(expList, "||"));

        String finalExp = (currentIndex + 1 == branchs.size()) ? "1==1" : StrUtil.subBetween(handle(branchs.get(currentIndex)), "${", "}");

        String exp = StrUtil.format("${!({})&&({})}", CollUtil.join(expList, "||"), finalExp);
        log.info(" 参数索引：{}  表达式：{}", currentIndex, exp);
        return exp;
    }

}
