package com.cxygzl.core.expression;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.UserFieldDto;
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




        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件 标识:{} 参数：{} 格式：{}", symbol, JSON.toJSONString(param), format);

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
    public boolean numberHandler(String key, String symbol, Object param, DelegateExecution execution) {

        return numberHandler(key, symbol, param, execution.getVariable(key));


    }

    public boolean numberHandler(String key, String symbol, Object param, Object value) {




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
     * 判断字符数组包含
     *
     * @param key   表单key
     * @return
     */
    public boolean selectHandler(String key, DelegateExecution execution, String param, String symbol) {

        return selectHandler(key, execution.getVariable(key), param, symbol);
    }

    public boolean selectHandler(String key,Object value, String param, String symbol) {


        List<String> list = JSON.parseArray(param, String.class);



        log.debug("表单值：key={} value={} param={} symbol={}", key, JSON.toJSONString(value), param, symbol);
        log.debug("条件  参数：{}", JSON.toJSONString(list));
        if (value == null) {
            return false;
        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.IN)) {
            return list.contains(value.toString());
        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_IN)) {
            return !list.contains(value.toString());
        }
        return false;
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

    public boolean stringHandler(String key, String param, Object value, String symbol) {

        log.debug("表单值：key={} value={}", key, JSON.toJSONString(value));
        log.debug("条件  参数：{}", JSON.toJSONString(param));
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
        List<String> deptIdList = paramDeptList.stream().map(w -> (w.getId())).collect(Collectors.toList());


        return inCompare(symbol, nodeUserDto.getId(), deptIdList);

    }

    private boolean inCompare(String symbol, String deptId, List<String> deptIdList) {
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.IN)) {
            //属于
            return deptIdList.contains(deptId);
        }
        if (StrUtil.equals(symbol, ProcessInstanceConstant.ConditionSymbol.NOT_IN)) {
            //属于
            return !deptIdList.contains(deptId);
        }


        return false;
    }

    /**
     * user判断
     *
     * @param key      表单key
     * @param param1    参数
     * @param userKey  比如年龄age
     * @return
     */
    public boolean userCompare(String key, String param1, String symbol, DelegateExecution execution, String userKey) {

        param1 = EscapeUtil.unescape(param1);
        Object o = JSON.parseArray(param1, Object.class).get(0);


        Object value = execution.getVariable(key);

        String jsonString = JSON.toJSONString(value);
        log.debug("表单值：key={} value={}   symbol={} userKey={} ", key, jsonString, symbol, userKey);
        log.debug("条件  参数：{}", o);
        if (value == null) {
            return false;
        }

        //表单值
        List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
        if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
            return false;
        }
        NodeUser nodeUserDto = nodeUserDtoList.get(0);
        //获取用户值


        if (StrUtil.equals(ProcessInstanceConstant.ConditionSymbol.RANGE, userKey)) {
            //参数
            List<NodeUser> paramDeptList =  JSON.parseArray(JSON.toJSONString(o), NodeUser.class);

            List<String> deptIdList = paramDeptList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> (w.getId())).collect(Collectors.toList());
            List<String> userIdList = paramDeptList.stream().filter(w -> StrUtil.equals(w.getType(),
                    NodeUserTypeEnum.USER.getKey())).map(w -> (w.getId())).collect(Collectors.toList());


            if (CollUtil.isNotEmpty(deptIdList)) {
                R<List<String>> r = CoreHttpUtil.queryUserIdListByDepIdList(deptIdList);
                List<String> data = r.getData();
                for (String datum : data) {
                    if (!userIdList.contains(datum)) {
                        userIdList.add(datum);
                    }
                }
            }


            return inCompare(symbol, (nodeUserDto.getId()), userIdList);
        }
        Map<String, Object> userInfo = CoreHttpUtil.queryUserInfo(nodeUserDto.getId()).getData();
        log.debug("查询到的用户信息:{}", JSONUtil.toJsonStr(userInfo));
        //查询变量属性
        List<UserFieldDto> userFieldDtoList = CoreHttpUtil.queryUseField().getData();
        UserFieldDto userFieldDto = userFieldDtoList.stream().filter(w -> StrUtil.equals(w.getKey(), userKey)).findFirst().get();
        if (StrUtil.equalsAny(userFieldDto.getType(), FormTypeEnum.INPUT.getType(), FormTypeEnum.TEXTAREA.getType())) {
            return stringHandler(userKey, Convert.toStr(o), userInfo.get(userKey), symbol);
        }
        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.NUMBER.getType())) {
            return numberHandler(userKey,symbol, Convert.toStr(o), userInfo.get(userKey));
        }

        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.DATE.getType())) {
            return dateTimeHandler(userKey,symbol, Convert.toStr(o), userInfo.get(userKey),"yyyy-MM-dd");
        }


        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.DATE_TIME.getType())) {
            return dateTimeHandler(userKey,symbol, Convert.toStr(o), userInfo.get(userKey),"yyyy-MM-dd HH:mm:ss");
        }


        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.TIME.getType())) {
            return dateTimeHandler(userKey,symbol, Convert.toStr(o), userInfo.get(userKey),"HH-mm-ss");
        }

        if (StrUtil.equals(userFieldDto.getType(), FormTypeEnum.SINGLE_SELECT.getType())) {
            return selectHandler(userKey, userInfo.get(userKey),Convert.toStr(o), symbol);
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
