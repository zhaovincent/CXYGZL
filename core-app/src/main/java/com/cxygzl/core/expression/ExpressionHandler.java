package com.cxygzl.core.expression;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.process.NodeUserDto;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表达式解析
 */
@Component("expressionHandler")
@Slf4j
public class ExpressionHandler {

    private GroupTemplate gt = null;

    @PostConstruct
    public void init() {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = null;
        try {
            cfg = Configuration.defaultConfiguration();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        gt = new GroupTemplate(resourceLoader, cfg);

    }

    public Long getUserId(String key,DelegateExecution execution){
        Object variable = execution.getVariable(key);
        NodeUserDto nodeUserDto = JSON.parseArray(JSON.toJSONString(variable), NodeUserDto.class).get(0);
        return nodeUserDto.getId();
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
    public boolean numberCompare(String key, String symbol, Object param, DelegateExecution execution) {


        Object value = execution.getVariable(key);


        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件 标识:{} 参数：{}", symbol, JSON.toJSONString(param));

        //表单值为空
        if (value == null) {
            return false;
        }


        return compare(StrUtil.format("${key{}{}}", symbol, param), Dict.create().set("key", Convert.toNumber(value)));

    }

    private Boolean compare(String symbol, Dict value) {
        //获取模板
        Template t = gt.getTemplate(symbol);

        t.binding(value);

        //渲染结果
        String result = t.render();
        log.debug("验证结果:{}", result);

        return Convert.toBool(result, false);
    }

    /**
     * 日期类型对比
     *
     * @param key       表单key
     * @param symbol    比较符号
     * @param param     比较参数值
     * @param execution 上下午执行前
     * @param format    日期格式化字符串
     * @return
     */
    public boolean dateCompare(String key, String symbol, Object param, DelegateExecution execution, String format) {


        Object value = execution.getVariable(key);


        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件 标识:{} 参数：{}", symbol, JSON.toJSONString(param));

        //表单值为空
        if (value == null) {
            return false;
        }

        //处理表单值
        DateTime valueDateTime = DateUtil.parse(value.toString(), format);
        log.debug("表单值：{} 格式化显示：{}", valueDateTime.getTime(), DateUtil.formatDateTime(valueDateTime));
        //处理参数值
        DateTime paramDateTime = DateUtil.parse(param.toString(), format);
        log.debug("参数值：{} 格式化显示：{}", paramDateTime.getTime(), DateUtil.formatDateTime(paramDateTime));


        //获取模板
        return compare(StrUtil.format("${key{}{}}", symbol, paramDateTime.getTime()), Dict.create().set("key", valueDateTime.getTime()));

    }

    /**
     * 数字 多符号比较
     * 类似于 1<x<2
     *
     * @param key
     * @param symbol1   符号1
     * @param param1    表单参数1
     * @param symbol2   符号2
     * @param param2    表单参数2
     * @param execution
     * @return
     */
    public boolean numberCompare(String key, String symbol1, Object param1, String symbol2, Object param2,
                                 DelegateExecution execution) {

        Object value = execution.getVariable(key);


        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件 标识1:{} 参数1：{}", symbol1, JSON.toJSONString(param1));
        log.debug("条件 标识2:{} 参数2：{}", symbol2, JSON.toJSONString(param2));

        //表单值为空
        if (value == null) {
            return false;
        }


        //获取模板
        return compare(StrUtil.format("${key{}{}&&key{}{}}", symbol1, param1, symbol2, param2), Dict.create().set("key", value));

    }


    /**
     * 判断数字数组包含
     *
     * @param key   表单key
     * @param array 条件值
     * @return
     */
    public boolean numberContain(String key, DelegateExecution execution, Object... array) {

        Object value = execution.getVariable(key);


        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件 参数：{}", JSON.toJSONString(array));

        if (value == null) {
            return false;
        }

        return numberContain(value, array);
    }

    private static boolean numberContain(Object value, Object[] array) {
        BigDecimal valueBigDecimal = Convert.toBigDecimal(value);

        for (Object aLong : array) {
            if (valueBigDecimal.compareTo(Convert.toBigDecimal(aLong)) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符数组包含
     *
     * @param key   表单key
     * @param array 条件值
     * @return
     */
    public boolean stringContain(String key, DelegateExecution execution, String... array) {
        Object value = execution.getVariable(key);

        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件  参数：{}", JSON.toJSONString(array));
        if (value == null) {
            return false;
        }
        return ArrayUtil.contains(array, value.toString());
    }

    /**
     * 字符串判断相等
     *
     * @param key   表单key
     * @param param 参数
     * @return
     */
    public boolean stringEqual(String key, String param, DelegateExecution execution) {
        Object value = execution.getVariable(key);

        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件  参数：{}", JSON.toJSONString(param));
        if (value == null) {
            return false;
        }
        return StrUtil.equals(value.toString(), param);
    }

    public boolean deptCompare(String key, String param, String symbol, DelegateExecution execution) {
        param = EscapeUtil.unescape(param);


        Object value = execution.getVariable(key);

        String jsonString = JSON.toJSONString(value);
        log.debug("表单值：key={} value={} symbol={}", key, jsonString, symbol);
        log.debug("条件  参数：{}", param);
        if (value == null) {
            return false;
        }

        //表单值
        List<NodeUserDto> nodeUserDtoList = JSON.parseArray(jsonString, NodeUserDto.class);
        if (CollUtil.isEmpty(nodeUserDtoList)) {
            return false;
        }
        NodeUserDto nodeUserDto = nodeUserDtoList.get(0);

        //参数
        List<NodeUserDto> paramDeptList = JSON.parseArray(param, NodeUserDto.class);
        Long depId = nodeUserDto.getId();
        List<Long> deptIdList = paramDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());


        return deptCompare(symbol, depId, deptIdList);

    }

    private static boolean deptCompare(String symbol, Long depId, List<Long> deptIdList) {
        if (StrUtil.equals(symbol, "belong")) {
            //属于
            return deptIdList.contains(depId);
        }
        //父级
        if (StrUtil.equals(symbol, "parent")) {
            String s = CoreHttpUtil.checkDepIsAllParent(depId,deptIdList);
            log.debug("查询到的数据：{}", s);
            if (StrUtil.isBlank(s)) {
                return false;
            }
            R<Boolean> r = JSON.parseObject(s, new TypeReference<R<Boolean>>() {
            });
            if (!r.isOk()) {
                return false;
            }
            return r.getData();
        }

        //子级
        if (StrUtil.equals(symbol, "child")) {
            String s = CoreHttpUtil.checkDepIsAllChild(depId,deptIdList);
            log.debug("查询到的数据：{}", s);
            if (StrUtil.isBlank(s)) {
                return false;
            }
            R<Boolean> r = JSON.parseObject(s, new TypeReference<R<Boolean>>() {
            });
            if (!r.isOk()) {
                return false;
            }
            return r.getData();
        }


        return false;
    }

    /**
     * user判断
     *
     * @param key      表单key
     * @param param    参数
     * @param userKey  比如年龄age
     * @param userType 比如数字类型 Number
     * @return
     */
    public boolean userCompare(String key, String param, String userKey, String userType, String symbol, DelegateExecution execution) {
        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        Map<String, Object> variables = runtimeService.getVariables(execution.getId());
        return userCompare(key,param,userKey,userType,symbol,variables);
    }
    public boolean userCompare(String key, String param, String userKey, String userType, String symbol, Map<String,Object> paramAllMap) {

        param = EscapeUtil.unescape(param);


        Object value = paramAllMap.get(key);

        String jsonString = JSON.toJSONString(value);
        log.debug("表单值：key={} value={} userKey={} userType={} symbol={}", key, jsonString, userKey, userType, symbol);
        log.debug("条件  参数：{}", param);
        if (value == null) {
            return false;
        }

        //表单值
        NodeUserDto formConditionUserVOFormValue = null;
        if (StrUtil.startWith(jsonString, "[")) {
            formConditionUserVOFormValue = JSON.parseArray(jsonString, NodeUserDto.class).get(0);

        } else {
            formConditionUserVOFormValue = JSON.parseObject(jsonString, NodeUserDto.class);
        }
        Long userId = formConditionUserVOFormValue.getId();
        //获取用户数据
        String s = CoreHttpUtil.queryUserAllInfo(userId);
        log.debug("用户{}查询到的数据：{}", userId, s);
        if (StrUtil.isBlank(s)) {
            return false;
        }
        R<Map<String, Object>> r = JSON.parseObject(s, new TypeReference<R<Map<String, Object>>>() {
        });
        if (!r.isOk()) {
            return false;
        }

        Map<String, Object> userData = r.getData();
        Object o = MapUtil.get(userData, userKey, Object.class);
        if (o == null) {
            return false;
        }
        {
            //获取模板
            if (StrUtil.equals(userType, "Number")) {
                List<Number> numbers = JSON.parseArray(param, Number.class);
                Number[] array = ArrayUtil.toArray(numbers, Number.class);
                if (StrUtil.equals(symbol, "IN")) {
                    return numberContain(o, array);
                }
                return compare(StrUtil.format("${key{}{}}", symbol, numbers.get(0)), Dict.create().set("key", Convert.toNumber(o)));

            }
            if (StrUtil.equalsAny(userType, "String", "Select")) {
                List<String> numbers = JSON.parseArray(param, String.class);

                if (StrUtil.equals(symbol, "IN")) {
                    return numbers.contains(o.toString());
                }
                return StrUtil.equals(o.toString(), numbers.get(0));

            }
            if (StrUtil.equals(userType, "Date")) {
                List<String> numbers = JSON.parseArray(param, String.class);

                DateTime dateTime = DateUtil.parseDate(numbers.get(0));
                DateTime value1 = DateUtil.parseDate(o.toString());
                return compare(StrUtil.format("${key{}{}}", symbol, dateTime.getTime()), Dict.create().set("key", value1.getTime()));


            }
            if (StrUtil.equals(userType, "DateTime")) {
                List<String> numbers = JSON.parseArray(param, String.class);

                return compare(StrUtil.format("${key{}{}}", symbol, DateUtil.parseDateTime(numbers.get(0)).getTime()), Dict.create().set("key", DateUtil.parseDateTime(o.toString()).getTime()));


            }
            if (StrUtil.equals(userType, "Time")) {
                List<String> numbers = JSON.parseArray(param, String.class);


                return compare(StrUtil.format("${key{}{}}", symbol, DateUtil.parse(numbers.get(0)).getTime()), Dict.create().set("key", DateUtil.parse(o.toString()).getTime()));


            }
            if (StrUtil.equals(userType, "Dept")) {
                List<NodeUserDto> nodeUserDtoList = JSON.parseArray(param, NodeUserDto.class);

                return deptCompare(symbol,Convert.toLong(o),nodeUserDtoList.stream().map(w->w.getId()).collect(Collectors.toList()));



            }
        }

        //表单参数值
        List<NodeUserDto> formConditionUserVOList = JSON.parseArray(param, NodeUserDto.class);
        //先精确匹配用户
        boolean match = formConditionUserVOList.stream().anyMatch(w -> {
            return w.getId().longValue() == userId && StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey());
        });
        if (match) {
            return true;
        }
        //再匹配部门
        match = formConditionUserVOList.stream().anyMatch(w -> {
            Long depId = MapUtil.getLong(userData, "depId", 0L);
            return w.getId().longValue() == depId && StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey());
        });
        if (match) {
            return true;
        }
        return false;
    }

}
