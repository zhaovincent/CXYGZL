package com.cxygzl.core.expression.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.process.NodeConditionDto;
import com.cxygzl.common.dto.process.NodeDto;
import com.cxygzl.common.dto.process.NodeGroupDto;
import com.cxygzl.common.dto.process.NodePropDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuhengzhen
 * @date 2022/11/04 18:00
 **/

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
     * @param key 批次类型枚举
     * @param nodeConditionHandler  表达式处理接口
     */
    public static void register(String key, NodeConditionStrategy nodeConditionHandler) {
        STRATEGY_CONCURRENT_HASH_MAP.put(key, nodeConditionHandler);
    }



    /**
     * 获取单个表达式
     *
     * @param nodeConditionDto
     * @return
     */
    public static String handleSingleCondition(NodeConditionDto nodeConditionDto) {
        NodeConditionStrategy nodeConditionHandler =getStrategy(nodeConditionDto.getValueType());
        if (nodeConditionHandler == null) {
            return "(1==1)";
        }
        return nodeConditionHandler.handle(nodeConditionDto);
    }

    /**
     * 组内处理表达式
     * @param groupDto
     * @return
     */

    public static String handleGroupCondition(NodeGroupDto groupDto) {

        List<String> exps = new ArrayList<>();

        for (NodeConditionDto condition : groupDto.getConditions()) {
            String singleExpression = handleSingleCondition(condition);
            exps.add(singleExpression);
        }

        String groupType = groupDto.getGroupType();
        if(StrUtil.equals(groupType, "OR")){
            String join = CollUtil.join(exps, "||");

            return "("+join+")";
        }

        String join = CollUtil.join(exps, "&&");
        return "("+join+")";
    }

    /**
     * 处理单个分支表达式
     * @param nodePropDto 单个分支条件对象
     * @return
     */
    public static String handle(NodePropDto nodePropDto) {

        List<String> exps = new ArrayList<>();


        List<NodeGroupDto> groups = nodePropDto.getGroups();
        if(groups==null){
            return "${1==1}";
        }
        for (NodeGroupDto group : groups) {
            String s = handleGroupCondition(group);
            exps.add(s);
        }

        String groupType = nodePropDto.getGroupType();
        if(StrUtil.equals(groupType, "OR")){
            String join = CollUtil.join(exps, "||");

            return "${("+join+")}";
        }

        String join = CollUtil.join(exps, "&&");
        return "${("+join+")}";
    }

    /**
     * 处理默认分支表达式
     * @param branchs 所有分支
     * @return
     */
    public static String handleDefaultBranch(List<NodeDto> branchs) {

        List<String> expList=new ArrayList<>();

        int size = branchs.size();

        int index=1;
        for (NodeDto branch : branchs) {

            if(index==size){
                continue;
            }
            NodePropDto props = branch.getProps();
            String exp = handle(props);
            String s = StrUtil.subBetween(exp, "${", "}");
            expList.add(StrUtil.format("({})",s));

            index++;
        }
        String join =StrUtil.format("!({})", CollUtil.join(expList, "||"));
        return "${"+join+"}";
    }

}
