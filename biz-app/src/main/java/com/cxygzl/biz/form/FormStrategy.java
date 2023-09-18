package com.cxygzl.biz.form;

import com.cxygzl.common.dto.flow.FormItemVO;

import java.util.List;

/**
 * 表单接口
 */
public interface FormStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        FormStrategyFactory.register(key, this);
    }

    /**
     * 创建sql
     *
     * @param formItemVO
     * @return
     */
    List<String> getCreateSql(FormItemVO formItemVO);

    /**
     * 查询sql的表字段
     * @param formItemVO
     * @return
     */
    List<String> getInsertField(FormItemVO formItemVO);

    /**
     * 插入sql的值
     *
     * @param formItemVO
     * @param value
     * @return
     */
    List<String> getInsertValue(FormItemVO formItemVO, Object value);


}
