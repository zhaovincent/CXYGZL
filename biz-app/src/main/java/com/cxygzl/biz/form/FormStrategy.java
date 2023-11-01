package com.cxygzl.biz.form;

import com.cxygzl.common.dto.flow.FormItemVO;
import org.anyline.metadata.Column;

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
     * 获取创建表的列
     * @param formItemVO
     * @return
     */
    List<Column> getTableColumn(FormItemVO formItemVO);

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

    /**
     * 打印显示内容
     * @param formItemVO
     * @param value
     * @return
     */
    String printShow(FormItemVO formItemVO,Object value);

    /**
     * 数据的长度
     * @param s
     * @return
     */
    int length(String s);

    /**
     * 获取excel显示内容
     * @param s
     * @param index
     * @return
     */
    String getExcelShow(String s,int index);


}
