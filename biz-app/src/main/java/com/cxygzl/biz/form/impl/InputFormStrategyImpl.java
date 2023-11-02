package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InputFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.INPUT.getType());
        afterPropertiesSet(FormTypeEnum.TEXTAREA.getType());
        afterPropertiesSet(FormTypeEnum.DESCRIPTION.getType());
        afterPropertiesSet(FormTypeEnum.SIGNATURE.getType());
        afterPropertiesSet(FormTypeEnum.FORMULA.getType());

    }

    /**
     * 创建sql
     *
     * @param formItemVO
     * @return
     */
    @Override
    public List<String> getCreateSql(FormItemVO formItemVO) {
        String format = StrUtil.format("`{}` {} NULL COMMENT '{}'", formItemVO.getId(),
                "longtext", formItemVO.getName());
        return CollUtil.newArrayList(format);
    }

    /**
     * 获取创建表的列
     *
     * @param formItemVO
     * @return
     */
    @Override
    public List<Column> getTableColumn(FormItemVO formItemVO) {
        List<Column> list = new ArrayList<>();

        {
            Column column = new Column(StrUtil.format("{}", formItemVO.getId()), "longtext", 0);
            column.setNullable(true);
            column.setComment(formItemVO.getName());
            list.add(column);
        }

        return list;
    }

    /**
     * 查询sql的表字段
     *
     * @param formItemVO
     * @return
     */
    @Override
    public List<String> getInsertField(FormItemVO formItemVO) {
        return CollUtil.newArrayList(StrUtil.format("{}", formItemVO.getId()));
    }

    /**
     * 插入sql的值
     *
     * @param formItemVO
     * @param value
     * @return
     */
    @Override
    public List<String> getInsertValue(FormItemVO formItemVO, Object value) {
        return value == null ? CollUtil.newArrayList("") : CollUtil.newArrayList(value.toString());
    }

    /**
     * 打印显示内容
     *
     * @param formItemVO
     * @param value
     * @return
     */
    @Override
    public String printShow(FormItemVO formItemVO, Object value) {
        return value == null ? null : value.toString();
    }

    /**
     * 数据的长度
     *
     * @param s
     * @return
     */
    @Override
    public int length(String s) {
        return 1;
    }

    /**
     * 获取excel显示内容
     *
     * @param s
     * @param index
     * @return
     */
    @Override
    public String getExcelDataShow(String s, int index) {
        return s;
    }

    /**
     * 获取流程详情 excel显示
     *
     * @param s
     * @return
     */
    @Override
    public String getProcessInstanceExcelShow(String s) {
        return s;
    }
}
