package com.cxygzl.core.expression;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.core.utils.CoreHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
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

    public Long getUserId(String key, DelegateExecution execution) {
        Object variable = execution.getVariable(key);
        NodeUser nodeUserDto = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class).get(0);
        return Long.valueOf(nodeUserDto.getId());
    }

    /**
     * 日期时间比较
     * @param key
     * @param symbol
     * @param param
     * @param execution
     * @param format 时间格式化模式
     * @return
     */
    public boolean dateTimeCompare(String key, String symbol, Object param, DelegateExecution execution,String format) {


        Object value = execution.getVariable(key);


        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件 标识:{} 参数：{} 格式：{}", symbol, JSON.toJSONString(param),format);

        //表单值为空
        if (value == null) {
            return false;
        }

        long valueTime = DateUtil.parse(value.toString(), format).getTime();
        long paramTime = DateUtil.parse(param.toString(), format).getTime();


        return compare(StrUtil.format("${key{}{}}", symbol, paramTime), Dict.create().set("key", valueTime));
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
    public boolean stringArrayContain(String key, DelegateExecution execution, String... array) {
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

    /**
     * 字符串判断包含
     *
     * @param key   表单key
     * @param param 参数
     * @return
     */
    public boolean stringContain(String key, String param, DelegateExecution execution) {
        Object value = execution.getVariable(key);

        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件  参数：{}", JSON.toJSONString(param));
        if (value == null) {
            return false;
        }
        return StrUtil.contains(value.toString(), param);
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
        List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
        if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
            return false;
        }
        NodeUser nodeUserDto = nodeUserDtoList.get(0);

        //参数
        List<NodeUser> paramDeptList = JSON.parseArray(param, NodeUser.class);
        Long deptId = Long.valueOf(nodeUserDto.getId());
        List<Long> deptIdList = paramDeptList.stream().map(w -> Long.parseLong(w.getId())).collect(Collectors.toList());


        return inCompare(symbol, deptId, deptIdList);

    }

    private static boolean inCompare(String symbol, Long deptId, List<Long> deptIdList) {
        if (StrUtil.equals(symbol, "in")) {
            //属于
            return deptIdList.contains(deptId);
        }
        if (StrUtil.equals(symbol, "notin")) {
            //属于
            return !deptIdList.contains(deptId);
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
    public boolean userCompare(String key, String param, String symbol, DelegateExecution execution) {

        param = EscapeUtil.unescape(param);


        Object value = execution.getVariable(key);

        String jsonString = JSON.toJSONString(value);
        log.debug("表单值：key={} value={}   symbol={} ", key, jsonString, symbol);
        log.debug("条件  参数：{}", param);
        if (value == null) {
            return false;
        }

        //表单值
        List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
        if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
            return false;
        }
        NodeUser nodeUserDto = nodeUserDtoList.get(0);

        //参数
        List<NodeUser> paramDeptList = JSON.parseArray(param, NodeUser.class);

        List<String> deptIdList = paramDeptList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> (w.getId())).collect(Collectors.toList());
        List<Long> userIdList = paramDeptList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Long.parseLong(w.getId())).collect(Collectors.toList());


        if(CollUtil.isNotEmpty(deptIdList)) {
            R<List<String>> r = CoreHttpUtil.queryUserIdListByDepIdList(deptIdList);
            List<String> data = r.getData();
            for (String datum : data) {
                Long aLong = Convert.toLong(datum);
                if(!userIdList.contains(aLong)){
                    userIdList.add(aLong);
                }
            }
        }


        return inCompare(symbol, Convert.toLong(nodeUserDto.getId()),userIdList);
    }

}
