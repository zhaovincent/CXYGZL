package com.cxygzl.core.expression.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.Condition;
import com.cxygzl.common.dto.flow.GroupCondition;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.core.node.NodeDataStoreFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class NodeExpressionStrategyFactory {



    /**
     * 返回表达式
     *
     * @param nodeConditionDto
     * @return
     */
    public static String handleSingleConditionExpression(Condition nodeConditionDto) {
        String s = IdUtil.fastSimpleUUID();
        NodeDataStoreFactory.getInstance().saveAll(s, s, nodeConditionDto);

        return StrUtil.format("(expressionHandler.handle(execution,\"{}\"))", s);

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


        String finalExp = (currentIndex + 1 == branchs.size()) ? "1==1" : StrUtil.subBetween(handle(branchs.get(currentIndex)), "${", "}");

        String exp = StrUtil.format("${!({})&&({})}", CollUtil.join(expList, "||"), finalExp);
        log.info(" 参数索引：{}  表达式：{}", currentIndex, exp);
        return exp;
    }

}
