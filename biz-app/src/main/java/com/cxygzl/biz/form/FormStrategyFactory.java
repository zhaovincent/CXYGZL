package com.cxygzl.biz.form;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.flow.FormItemVO;
import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.entity.DataRow;
import org.anyline.entity.OriginalDataRow;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;

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
    public static Table buildDDLSql(List<FormItemVO> formItemVOList, String uniqueId, String processName) {

        Table table = new Table(StrUtil.format("tb_{}", uniqueId));
        table.addColumn("id", "bigint").setPrimaryKey(true).setComment("id主键");
        table.addColumn("create_time", "datetime").setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("update_time", "datetime").setComment("修改时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("del_flag", "tinyint").setComment("是否删除").setPrecision(1).setDefaultValue(0);
        table.addColumn("process_instance_id", "varchar").setComment("流程实例id").setPrecision(100).setNullable(false);
        table.addColumn("flow_id", "varchar").setPrecision(100).setComment("流程id").setNullable(false);


        for (FormItemVO formItemVO : formItemVOList) {
            FormStrategy strategy = getStrategy(formItemVO.getType());
            if (strategy == null) {
                continue;
            }
            List<Column> columnList = strategy.getTableColumn(formItemVO);
            for (Column column : columnList) {
                table.addColumn(column);
            }
        }


        return table;
    }

    /**
     * 构建插入sql'语句
     *
     * @param formItemVOList
     * @param flowId
     * @param processInstanceId
     * @param paramMap
     * @return
     */
    public static DataRow buildInsertSql(List<FormItemVO> formItemVOList, String flowId, String processInstanceId,
                                        Map<String, Object> paramMap) {
        DataRow dataRow = new OriginalDataRow();

        for (FormItemVO formItemVO : formItemVOList) {
            Object value = paramMap.get(formItemVO.getId());
            if (value == null || StrUtil.isBlankIfStr(value)) {
                continue;
            }

            FormStrategy strategy = FormStrategyFactory.getStrategy(formItemVO.getType());
            if (strategy == null) {
                continue;
            }
            List<String> fieldList = strategy.getInsertField(formItemVO);
            List<String> valueList = strategy.getInsertValue(formItemVO,
                    value);

            if (CollUtil.isEmpty(valueList)) {
                continue;
            }

            int index = 0;
            for (String s : fieldList) {
                dataRow.put(s, valueList.get(index));
                index++;
            }


        }


        long snowflakeNextId = IdUtil.getSnowflakeNextId();
        dataRow.put("id",snowflakeNextId);
        dataRow.put("process_instance_id",processInstanceId);
        dataRow.put("flow_id",flowId);


        return dataRow;
    }


}
