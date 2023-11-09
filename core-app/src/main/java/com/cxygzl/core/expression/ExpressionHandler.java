package com.cxygzl.core.expression;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cxygzl.common.constants.ApproveResultEnum;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.flow.*;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserFieldDto;
import com.cxygzl.common.utils.AreaUtil;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.expression.condition.NodeExpressionResultStrategyFactory;
import com.cxygzl.core.node.NodeDataStoreFactory;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表达式解析
 */
@Component("expressionHandler")
@Slf4j
public class ExpressionHandler {


    /**
     * 日期时间比较
     *
     * @param key
     * @param symbol
     * @param param
     * @param execution
     * @param format    时间格式化模式
     * @return
     */
    public boolean dateTimeHandler(String key, String symbol, Object param, DelegateExecution execution, String format) {


        return dateTimeHandler(key, symbol, param, execution.getVariable(key), format);

    }

    public boolean dateTimeHandler(String key, String symbol, Object param, Object value, String format) {


        log.debug("表单值：key={} value={}", key, JsonUtil.toJSONString(value));
        log.debug("条件 标识:{} 参数：{} 格式：{}", symbol, JsonUtil.toJSONString(param), format);

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value);

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value);

        }
        //表单值为空
        if (value == null) {
            return false;
        }

        long valueTime = DateUtil.parse(value.toString(), format).getTime();
        long paramTime = DateUtil.parse(param.toString(), format).getTime();


        return compare(StrUtil.format("${key{}{}}", symbol, paramTime), Dict.create().set("key", valueTime));
    }

    /**
     * 地区处理
     *
     * @param key
     * @param symbol
     * @param param
     * @param execution
     * @return
     */
    public boolean areaHandler(String key, String symbol, Object param, DelegateExecution execution) {

        return areaHandler(key, symbol, param, execution.getVariable(key));


    }

    /**
     * 级联处理
     *
     * @param key
     * @param symbol
     * @param param
     * @param execution
     * @return
     */
    public boolean cascadeHandler(String key, String symbol, Object param, DelegateExecution execution) {

        return cascadeHandler(key, symbol, param, execution.getVariable(key));


    }

    public boolean areaHandler(String key, String symbol, Object param, Object value) {


        String unescape = EscapeUtil.unescape(param.toString());


        log.debug("表单值：key={} value={}", key, JsonUtil.toJSONString(value));
        log.debug("条件 标识:{} 参数：{}", symbol, JsonUtil.toJSONString(param));
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value) || Convert.toMap(Object.class, Object.class, value).size() == 0;

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value) && Convert.toMap(Object.class, Object.class, value).size() > 0;

        }


        //表单值为空
        if (value == null || Convert.toMap(Object.class, Object.class, value).size() == 0) {
            return false;
        }
        AreaFormValue areaFormValueParam = JsonUtil.parseObject(unescape, AreaFormValue.class);
        String paramCode = areaFormValueParam.getCode();

        AreaFormValue areaFormValueValue = BeanUtil.copyProperties(value, AreaFormValue.class);
        String valueCode = areaFormValueValue.getCode();

        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.EQUAL, symbol)) {
            return StrUtil.equals(paramCode, valueCode);
        }

        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.NOT_EQUAL, symbol)) {
            return !StrUtil.equals(paramCode, valueCode);
        }


        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.IN, symbol)) {
            return AreaUtil.contain(paramCode, valueCode);
        }


        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.NOT_IN, symbol)) {
            return !AreaUtil.contain(paramCode, valueCode);

        }

        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.CONTAIN, symbol)) {
            return AreaUtil.contain(valueCode, paramCode);

        }


        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.NOT_CONTAIN, symbol)) {
            return !AreaUtil.contain(valueCode, paramCode);

        }


        return false;

    }

    public boolean cascadeHandler(String key, String symbol, Object param, Object value) {


        log.debug("表单值：key={} value={}", key, JsonUtil.toJSONString(value));
        log.debug("条件 标识:{} 参数：{}", symbol, JsonUtil.toJSONString(param));
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value) || Convert.toMap(Object.class, Object.class, value).size() == 0;

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value) && Convert.toMap(Object.class, Object.class, value).size() > 0;

        }



        //表单值为空
        if (value == null || Convert.toMap(Object.class, Object.class, value).size() == 0) {
            return false;
        }
        String unescape = EscapeUtil.unescape(param.toString());

        CascadeFormValue areaFormValueParam = JsonUtil.parseObject(unescape, CascadeFormValue.class);
        String paramCode = areaFormValueParam.getKey();

        CascadeFormValue areaFormValueValue = BeanUtil.copyProperties(value, CascadeFormValue.class);
        String valueCode = areaFormValueValue.getKey();

        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.EQUAL, symbol)) {
            return StrUtil.equals(paramCode, valueCode);
        }

        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.NOT_EQUAL, symbol)) {
            return !StrUtil.equals(paramCode, valueCode);
        }




        return false;

    }

    /**
     * 数字类型比较
     *
     * @param key       表单key
     * @param symbol    比较符号
     * @param param     表单参数
     * @param execution
     * @return
     */
    public boolean numberHandler(String key, String symbol, Object param, DelegateExecution execution) {

        return numberHandler(key, symbol, param, execution.getVariable(key));


    }

    public boolean numberHandler(String key, String symbol, Object param, Object value) {


        log.debug("表单值：key={} value={}", key, JsonUtil.toJSONString(value));
        log.debug("条件 标识:{} 参数：{}", symbol, JsonUtil.toJSONString(param));

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value);

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value);

        }

        //表单值为空
        if (value == null) {
            return false;
        }


        return compare(StrUtil.format("${key{}{}}", symbol, param), Dict.create().set("key", Convert.toNumber(value)));

    }


    private Boolean compare(String symbol, Dict value) {
        return DataUtil.expression(symbol, value);
    }


    /**
     * 判断字符数组包含
     *
     * @param key 表单key
     * @return
     */
    public boolean selectHandler(String key, DelegateExecution execution, String param, String symbol) {
        return selectHandler(key, execution.getVariables(), param, symbol);
    }

    public boolean selectHandler(String key, Map<String, Object> execution, String param, String symbol) {
        List<SelectValue> paramObjList = JsonUtil.parseArray(EscapeUtil.unescape(param), SelectValue.class);
        List<String> paramList = paramObjList.stream().map(w -> w.getKey()).collect(Collectors.toList());
        Object value = execution.get(key);


        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value) || Convert.toList(value).size() == 0;

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value) && Convert.toList(value).size() > 0;

        }

        if (value == null) {
            return false;
        }
        List<SelectValue> list = Convert.toList(SelectValue.class, value);
        return selectHandler(list.stream().map(w -> w.getKey()).collect(Collectors.toList()), paramList, symbol);
    }

    public boolean selectHandler(Object value, List<String> paramList, String symbol) {


        log.debug("表单值：value={}  symbol={}", JsonUtil.toJSONString(value), symbol);
        log.debug("条件  参数：{}", JsonUtil.toJSONString(paramList));

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value) || Convert.toList(value).size() == 0;

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value) && Convert.toList(value).size() > 0;

        }

        if (value == null || Convert.toList(value).size() == 0) {
            return false;
        }

        List<String> valueList = Convert.toList(String.class, value);

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EQUAL)) {
            //等于
            if(valueList.size()!=paramList.size()){
                return false;
            }
            paramList.removeAll(valueList);
            return paramList.isEmpty();


        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EQUAL)) {
            //不等于
            if(valueList.size()!=paramList.size()){
                return true;
            }
            paramList.removeAll(valueList);
            return !paramList.isEmpty();
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.IN)) {
            //属于
            if(valueList.size()>paramList.size()){
                return false;
            }


            valueList.removeAll(paramList);

            return valueList.isEmpty();
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_IN)) {
            //不属于

            int size = valueList.size();

            valueList.removeAll(paramList);

            return !valueList.isEmpty();
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.CONTAIN)) {
            //包含

            paramList.removeAll(valueList);

            return paramList.isEmpty();
        }


        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_CONTAIN)) {
            //不包含
            int size = paramList.size();
            paramList.removeAll(valueList);

            return paramList.size()==size;
        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.INTERSECTION)) {
            int size = paramList.size();
            paramList.removeAll(valueList);
            return paramList.size()<size;
        }
        return false;
    }

    /**
     * 处理表达式
     *
     * @param execution
     * @param uniqueId
     * @return
     */
    public boolean handle(DelegateExecution execution, String uniqueId) {
        String s = NodeDataStoreFactory.getInstance().get(uniqueId, uniqueId);
        Condition condition = JsonUtil.parseObject(s, Condition.class);
        Map<String, Object> variables = execution.getVariables();
        return NodeExpressionResultStrategyFactory.handleSingleConditionResult(condition, variables);
    }

    /**
     * 字符串判断
     *
     * @param key   表单key
     * @param param 参数
     * @return
     */
    public boolean stringHandler(String key, String param, DelegateExecution execution, String symbol) {

        return stringHandler(key, param, execution.getVariable(key), symbol);

    }

    /**
     * 判断所有的变量是否为null
     *
     * @param execution
     * @param keyArr
     * @return
     */
    public boolean isAllNull(DelegateExecution execution, String... keyArr) {
        Map<String, Object> variables = execution.getVariables(ListUtil.of(keyArr));
        for (String s : keyArr) {
            Object o = variables.get(s);
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断所有的变量是否为false
     *
     * @param execution
     * @param keyArr
     * @return
     */
    public boolean isAllFalse(DelegateExecution execution, String... keyArr) {
        Map<String, Object> variables = execution.getVariables(ListUtil.of(keyArr));
        for (String s : keyArr) {

            Boolean bool = MapUtil.getBool(variables, s);
            if (bool == null) {
                return false;
            }

            if (bool) {
                return false;
            }
        }
        return true;
    }

    /**
     * 不是所有的都是true
     *
     * @param execution
     * @param keyArr
     * @return
     */
    public boolean isNotAllTrue(DelegateExecution execution, String... keyArr) {
        Map<String, Object> variables = execution.getVariables(ListUtil.of(keyArr));
        for (String s : keyArr) {

            Boolean bool = MapUtil.getBool(variables, s);
            if (bool == null) {
                return true;
            }

            if (!bool) {
                return true;
            }
        }
        return false;
    }

    /**
     * 没有一个是true
     *
     * @param execution
     * @param keyArr
     * @return
     */
    public boolean isAllNotTrue(DelegateExecution execution, String... keyArr) {
        Map<String, Object> variables = execution.getVariables(ListUtil.of(keyArr));
        for (String s : keyArr) {

            Boolean bool = MapUtil.getBool(variables, s);
            if (bool == null) {
                continue;
            }

            if (bool) {
                return false;
            }
        }
        return true;
    }

    public boolean stringHandler(String key, String param, Object value, String symbol) {

        log.debug("表单值：key={} value={}", key, JsonUtil.toJSONString(value));
        log.debug("条件  参数：{}", JsonUtil.toJSONString(param));

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value);

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value);

        }
        if (value == null) {
            return false;
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EQUAL)) {

            return StrUtil.equals(value.toString(), param);

        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EQUAL)) {

            return !StrUtil.equals(value.toString(), param);

        }


        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.CONTAIN)) {

            return StrUtil.contains(value.toString(), param);

        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_CONTAIN)) {

            return !StrUtil.contains(value.toString(), param);

        }


        return false;
    }


    public boolean deptCompare(String key, String param, String symbol, DelegateExecution execution) {
        return deptCompare(key, param, symbol, execution.getVariables());
    }

    public boolean deptCompare(String key, String param, String symbol, Map<String, Object> paramMap) {
        param = EscapeUtil.unescape(param);


        Object value = paramMap.get(key);

        String jsonString = JsonUtil.toJSONString(value);
        log.debug("表单值：key={} value={} symbol={}", key, jsonString, symbol);
        log.debug("条件  参数：{}", param);

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value) || Convert.toList(value).size() == 0;

        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value) && Convert.toList(value).size() > 0;

        }

        if (value == null || Convert.toList(value).size() == 0) {
            return false;
        }

        //表单值
        List<NodeUser> nodeUserDtoList = JsonUtil.parseArray(jsonString, NodeUser.class);
        if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
            return false;
        }
        NodeUser nodeUserDto = nodeUserDtoList.get(0);

        //参数
        List<NodeUser> paramDeptList = JsonUtil.parseArray(param, NodeUser.class);
        List<String> deptIdList = paramDeptList.stream().map(w -> (w.getId())).collect(Collectors.toList());

        String deptId = nodeUserDto.getId();
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.IN)) {
            //属于
            List<DeptDto> deptDtoList = BizHttpUtil.queryParentDepList(deptId).getData();
            List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());

            int oldSize = deptIdList.size();
            deptIdList.removeAll(collect);

            return deptIdList.size()<oldSize;
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_IN)) {
            //不属于
            List<DeptDto> deptDtoList = BizHttpUtil.queryParentDepList(deptId).getData();
            List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());

            int oldSize = deptIdList.size();
            deptIdList.removeAll(collect);
            return deptIdList.size()>=oldSize;
        }


        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EQUAL)) {
            //==
            return deptIdList.contains(deptId)&&deptIdList.size()==1;
        }



        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EQUAL)) {
            //!==
            return !deptIdList.contains(deptId)||deptIdList.size()>1;
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.CONTAIN)) {
            //包含
            List<DeptDto> deptDtoList = BizHttpUtil.queryChildDeptList(deptId).getData();
            List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());

            deptIdList.removeAll(collect);
            return deptIdList.isEmpty();
        }

        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_CONTAIN)) {
            //不包含
            List<DeptDto> deptDtoList = BizHttpUtil.queryChildDeptList(deptId).getData();
            List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());

            int size = deptIdList.size();

            deptIdList.removeAll(collect);
            return deptIdList.size()==size;
        }


        return false;

    }



    /**
     * user判断
     *
     * @param key     表单key
     * @param param1  参数
     * @param userKey 比如年龄age
     * @return
     */
    public boolean userCompare(String key, String param1, String symbol, DelegateExecution execution, String userKey) {
        return userCompare(key, param1, symbol, execution.getVariables(), userKey);
    }

    public boolean userCompare(String key, String param1, String symbol, Map<String, Object> execution, String userKey) {

        param1 = EscapeUtil.unescape(param1);
        Object o = JsonUtil.parseArray(param1, Object.class).get(0);


        Object value = execution.get(key);

        String jsonString = JsonUtil.toJSONString(value);
        log.debug("表单值：key={} value={}   symbol={} userKey={} ", key, jsonString, symbol, userKey);
        log.debug("条件  参数：{}", o);


        if (StrUtil.equals(userKey, ProcessInstanceConstant.ConditionSymbol.EMPTY)) {

            return value == null || StrUtil.isBlankIfStr(value) || Convert.toList(value).size() == 0;

        }
        if (StrUtil.equals(userKey, ProcessInstanceConstant.ConditionSymbol.NOT_EMPTY)) {

            return value != null && !StrUtil.isBlankIfStr(value) && Convert.toList(value).size() > 0;

        }

        if (value == null || Convert.toList(value).size() == 0) {
            return false;
        }
        if (value == null) {
            return false;
        }

        //表单值
        List<NodeUser> nodeUserDtoList = JsonUtil.parseArray(jsonString, NodeUser.class);
        if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
            return false;
        }
        NodeUser nodeUserDto = nodeUserDtoList.get(0);
        //获取用户值


        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.RANGE, userKey)) {
            //参数
            List<NodeUser> paramDeptList = JsonUtil.parseArray(JsonUtil.toJSONString(o), NodeUser.class);

            List<String> deptIdList = paramDeptList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> (w.getId())).collect(Collectors.toList());
            List<String> userIdList = paramDeptList.stream().filter(w -> StrUtil.equals(w.getType(),
                    NodeUserTypeEnum.USER.getKey())).map(w -> (w.getId())).collect(Collectors.toList());

            if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.EQUAL) || CollUtil.isEmpty(deptIdList)) {
                //==
                if (userIdList.contains(nodeUserDto.getId())) {
                    return true;
                }
                if (CollUtil.isEmpty(deptIdList)) {
                    return false;
                }
                Map<String, Object> map = BizHttpUtil.queryUserInfo(nodeUserDto.getId()).getData();


                String str = MapUtil.getStr(map, "deptId");
                if (deptIdList.contains(str)) {
                    return true;
                }
                return false;

            }
            if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_EQUAL) || CollUtil.isEmpty(deptIdList)) {
                //!==
                if (userIdList.contains(nodeUserDto.getId())) {
                    return false;
                }
                if (CollUtil.isEmpty(deptIdList)) {
                    return true;
                }
                Map<String, Object> map = BizHttpUtil.queryUserInfo(nodeUserDto.getId()).getData();


                String str = MapUtil.getStr(map, "deptId");
                if (deptIdList.contains(str)) {
                    return false;
                }
                return true;
            }
            if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.CONTAIN)) {
                //包含
                if (userIdList.size() > 1) {
                    return false;
                }
                if (userIdList.size()==1&&!userIdList.contains(nodeUserDto.getId())) {
                    return false;
                }
                if (CollUtil.isEmpty(deptIdList)) {
                    return true;
                }

                //查询人员的下级部门
                List<DeptDto> deptDtoList = BizHttpUtil.queryChildDeptListByUserId(nodeUserDto.getId()).getData();
                List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());
                deptIdList.removeAll(collect);
                if (deptIdList.isEmpty()) {
                    return true;
                }
                return false;
            }

            if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_CONTAIN)) {
                //不包含

                if (userIdList.contains(nodeUserDto.getId())) {
                    return false;
                }
                if (CollUtil.isEmpty(deptIdList)) {
                    return true;
                }

                //查询人员的下级部门
                int oldSize = deptIdList.size();
                List<DeptDto> deptDtoList = BizHttpUtil.queryChildDeptListByUserId(nodeUserDto.getId()).getData();
                List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());
                deptIdList.removeAll(collect);
                if (deptIdList.size() == oldSize) {
                    return true;
                }
                return false;
            }

            if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.IN)) {
                //属于

                if (userIdList.contains(nodeUserDto.getId())) {
                    return true;
                }
                if (CollUtil.isEmpty(deptIdList)) {
                    return false;
                }

                //查询人员的上级部门
                int oldSize = deptIdList.size();
                List<DeptDto> deptDtoList = BizHttpUtil.queryParentDepListByUserId(nodeUserDto.getId()).getData();
                List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());


                deptIdList.removeAll(collect);
                if (deptIdList.size() < oldSize) {
                    return true;
                }
                return false;
            }


            if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_IN)) {
                //不属于

                if (userIdList.contains(nodeUserDto.getId())) {
                    return false;
                }
                if (CollUtil.isEmpty(deptIdList)) {
                    return true;
                }

                //查询人员的上级部门
                int oldSize = deptIdList.size();
                List<DeptDto> deptDtoList = BizHttpUtil.queryParentDepListByUserId(nodeUserDto.getId()).getData();
                List<String> collect = deptDtoList.stream().map(w -> w.getId()).collect(Collectors.toList());


                deptIdList.removeAll(collect);
                if (deptIdList.isEmpty()) {
                    return false;
                }
                return true;
            }

            return false;
        }
        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.ROLE, userKey)) {
            // 角色

            //参数
            List<NodeUser> paramList = JsonUtil.parseArray(JsonUtil.toJSONString(o), NodeUser.class);
            //当前用户
            String userId = nodeUserDto.getId();

            List<String> roleIdList = BizHttpUtil.queryRoleIdListByUserId(userId);

            return selectHandler(roleIdList, paramList.stream().map(w -> w.getId()).collect(Collectors.toList()), symbol);

        }
        Map<String, Object> userInfo = BizHttpUtil.queryUserInfo(nodeUserDto.getId()).getData();
        log.debug("查询到的用户信息:{}", JSONUtil.toJsonStr(userInfo));
        //查询变量属性
        List<UserFieldDto> userFieldDtoList = BizHttpUtil.queryUseField().getData();
        UserFieldDto userFieldDto = userFieldDtoList.stream().filter(w -> StrUtil.equals(w.getKey(), userKey)).findFirst().get();
        Object userValue = userInfo.get(userKey);
        if (StrUtil.equalsAny(userFieldDto.getType(), FormTypeEnum.INPUT.getType(), FormTypeEnum.TEXTAREA.getType())) {
            return stringHandler(userKey, Convert.toStr(o), userValue, symbol);
        }
        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.NUMBER.getType())) {
            return numberHandler(userKey, symbol, Convert.toStr(o), userValue);
        }
        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.FORMULA.getType())) {
            return numberHandler(userKey, symbol, Convert.toStr(o), userValue);
        }

        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.DATE.getType())) {
            return dateTimeHandler(userKey, symbol, Convert.toStr(o), userValue, "yyyy-MM-dd");
        }


        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.DATE_TIME.getType())) {
            return dateTimeHandler(userKey, symbol, Convert.toStr(o), userValue, "yyyy-MM-dd HH:mm:ss");
        }


        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.TIME.getType())) {
            return dateTimeHandler(userKey, symbol, Convert.toStr(o), userValue, "HH-mm-ss");
        }

        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.SINGLE_SELECT.getType())) {
            List<SelectValue> selectValueList = BeanUtil.copyToList(Convert.toList(o), SelectValue.class);

            return selectHandler((userValue == null || StrUtil.isBlankIfStr(userValue)) ? new ArrayList<>() :
                            CollUtil.newArrayList(userValue),
                    CollUtil.isEmpty(selectValueList) ? null :
                            selectValueList.stream().map(w -> w.getKey()).collect(Collectors.toList()), symbol);
        }


        return false;

    }

    /**
     * 调用活动 处理变量输入
     *
     * @param key
     * @param execution
     * @return
     */
    public Object callActivityVariables(String key, DelegateExecution execution, int flag) {
        if (flag == 1) {
            //处理发起人
            Object variable = execution.getVariable(key);
            return CollUtil.newArrayList(variable);

        }
        if (flag == 4) {
            //处理多实例表单主子流程参数传递
            Object variable = execution.getVariable(key);
            return CollUtil.newArrayList(variable);

        }
        if (flag == 5) {
            //子流程默认传递审批结果是通过
            return ApproveResultEnum.PASS.getValue();

        }
        if (flag == 6) {
            //子流程默认传递流程唯一id
            return IdUtil.fastSimpleUUID();

        }

        //子流程发起人需要执行表单任务
        if (StrUtil.equals(key, ProcessInstanceConstant.VariableKey.SUB_PROCESS_STARTER_NODE)) {
            return true;
        }

        Object variable = execution.getVariable(key);
        log.info("调用活动 变量key:{} value:{}", key, variable);
        if (variable == null) {
            return "";
        }
        return variable;
    }
}
