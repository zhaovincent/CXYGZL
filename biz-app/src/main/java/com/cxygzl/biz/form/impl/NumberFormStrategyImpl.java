package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
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
public class NumberFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.NUMBER.getType());
        afterPropertiesSet(FormTypeEnum.MONEY.getType());
        afterPropertiesSet(FormTypeEnum.SCORE.getType());
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
                "decimal(30,10)", formItemVO.getName());
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
        List<Column> list=new ArrayList<>();

        {
            Column column=new Column(StrUtil.format("{}_db",formItemVO.getId()),"decimal",30);
            column.setNullable(true);
            column.setComment(formItemVO.getName());
            column.setScale(10);
            list.add(column);
        }

        {
            Column column=new Column(StrUtil.format("{}",formItemVO.getId()),"varchar",50);
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
        return CollUtil.newArrayList(StrUtil.format("{}_db", formItemVO.getId()),StrUtil.format("{}",
                formItemVO.getId()));
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
        return value == null ? null : CollUtil.newArrayList(value.toString(),value.toString());
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
        return Convert.toStr(value);
    }
}
