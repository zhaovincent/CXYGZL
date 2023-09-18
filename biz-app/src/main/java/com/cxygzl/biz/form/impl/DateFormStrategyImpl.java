package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
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
public class DateFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.DATE.getType());
        afterPropertiesSet(FormTypeEnum.DATE_TIME.getType());
        afterPropertiesSet(FormTypeEnum.TIME.getType());
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
                "datetime", formItemVO.getName());
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
            Column column=new Column(StrUtil.format("{}",formItemVO.getId()),"datetime",0);
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
        if (value == null) {
            return null;
        }

        String type = formItemVO.getType();
        if(StrUtil.equals(type,FormTypeEnum.DATE.getType())){
            return CollUtil.newArrayList(DateUtil.formatDateTime(DateUtil.parseDate(value.toString())));
        }
        if(StrUtil.equals(type,FormTypeEnum.DATE_TIME.getType())){
            return CollUtil.newArrayList(DateUtil.formatDateTime(DateUtil.parseDateTime(value.toString())));
        }

        return CollUtil.newArrayList("2000-01-01 "+value.toString());
    }
}
