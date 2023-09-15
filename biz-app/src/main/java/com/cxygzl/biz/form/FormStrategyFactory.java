package com.cxygzl.biz.form;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.FormItemVO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FormStrategyFactory {

    private static final Map<String, FormStrategy> STRATEGY_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 提供获取策略的方法
     *
     * @param key
     * @return
     */
    public static FormStrategy getStrategy(String key) {
        FormStrategy formStrategy = STRATEGY_CONCURRENT_HASH_MAP.get(key);
        return formStrategy;
    }

    /**
     * 在Bean属性初始化后执行该方法
     *
     * @param key          表单类型
     * @param formStrategy 表单实现类
     */
    public static void register(String key, FormStrategy formStrategy) {
        STRATEGY_CONCURRENT_HASH_MAP.put(key, formStrategy);
    }

    /**
     * 获取创建sql语句
     *
     * @param formItemVOList
     * @param uniqueId
     * @param processName
     * @return
     */
    public static String getDDLSql(List<FormItemVO> formItemVOList, String uniqueId, String processName) {
        StringBuilder tableField = new StringBuilder();
        tableField.append(StrUtil.format("CREATE TABLE `tb_{}` (", uniqueId));
        tableField.append("id bigint not null");
        for (FormItemVO formItemVO : formItemVOList) {
            List<String> createSqlList = getStrategy(formItemVO.getType()).getCreateSql(formItemVO);
            for (String s : createSqlList) {
                tableField.append(",").append(s);

            }
        }
        tableField.append(", `create_time` datetime DEFAULT NULL COMMENT '创建时间'");
        tableField.append(", `process_instance_id` varchar(100) DEFAULT NULL COMMENT '实例id'");
        tableField.append(", `flow_id` varchar(100)  DEFAULT NULL COMMENT '流程id'");
        tableField.append(",PRIMARY KEY (`id`) USING BTREE");
        tableField.append(StrUtil.format(")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  " +
                "COMMENT='{}';", processName));

        return tableField.toString();
    }

    /**
     * 构建插入sql'语句
     *
     * @param formItemVOList
     * @param flowId
     * @param processInstanceId
     * @param paramMap
     * @param uniqueId
     * @return
     */
    public static String buildInsertSql(List<FormItemVO> formItemVOList, String flowId, String processInstanceId,
                                        Map<String, Object> paramMap, String uniqueId) {
        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        for (FormItemVO formItemVO : formItemVOList) {
            Object value = paramMap.get(formItemVO.getId());
            if (value == null || StrUtil.isBlankIfStr(value)) {
                continue;
            }

            List<String> fieldList = FormStrategyFactory.getStrategy(formItemVO.getType()).getInsertField(formItemVO);
            List<String> valueList = FormStrategyFactory.getStrategy(formItemVO.getType()).getInsertValue(formItemVO,
                    value);

            if (CollUtil.isEmpty(valueList)) {
                continue;
            }

            for (String s : fieldList) {
                fieldBuilder.append("`").append(s).append("`,");

            }

            for (String s : valueList) {
                valueBuilder.append("'").append(s).append("',");

            }


        }


        long snowflakeNextId = IdUtil.getSnowflakeNextId();

        //保存到数据库
        String format = StrUtil.format("INSERT INTO  `tb_{}` (`id`,{}" +
                        "`create_time`,`process_instance_id`,`flow_id`) VALUES ('{}',  {} '{}','{}','{}');", uniqueId,
                fieldBuilder.toString(),
                snowflakeNextId, valueBuilder.toString(), DateUtil.formatDateTime(new Date()), processInstanceId, flowId);

        return format;
    }


}
