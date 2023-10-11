package com.cxygzl.biz.form.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.SelectValue;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SelectFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.SINGLE_SELECT.getType());
        afterPropertiesSet(FormTypeEnum.MULTI_SELECT.getType());
    }

    /**
     * 创建sql
     *
     * @param formItemVO
     * @return
     */
    @Override
    public List<String> getCreateSql(FormItemVO formItemVO) {
        List<String> list = new ArrayList<>();
        {
            String format = StrUtil.format("`{}_key` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "longtext", formItemVO.getName());
            list.add(format);
        }
        {
            String format = StrUtil.format("`{}_value` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "longtext", formItemVO.getName());
            list.add(format);
        }
        {
            String format = StrUtil.format("`{}_data` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "longtext", formItemVO.getName());
            list.add(format);
        }

        return list;
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
            Column column = new Column(StrUtil.format("{}_key", formItemVO.getId()), "longtext", 0);
            column.setNullable(true);
            column.setComment(formItemVO.getName());

            list.add(column);
        }


        {
            Column column = new Column(StrUtil.format("{}_value", formItemVO.getId()), "longtext", 0);
            column.setNullable(true);
            column.setComment(formItemVO.getName());

            list.add(column);
        }


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
        return CollUtil.newArrayList(
                StrUtil.format("{}_key", formItemVO.getId()),

                StrUtil.format("{}_value",
                        formItemVO.getId()),
                StrUtil.format("{}",
                        formItemVO.getId())
        );
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
        List<SelectValue> selectValueList = BeanUtil.copyToList(Convert.toList(value), SelectValue.class);

        if (CollUtil.isEmpty(selectValueList)) {
            return null;
        }
        String id = selectValueList.stream().map(w -> w.getKey()).collect(Collectors.joining("||"));
        String name = selectValueList.stream().map(w -> w.getValue()).collect(Collectors.joining("||"));

        return CollUtil.newArrayList(id, name, JSON.toJSONString(selectValueList));
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
        if (value == null) {
            return null;
        }
        List<SelectValue> selectValueList = BeanUtil.copyToList(Convert.toList(value), SelectValue.class);

        if (CollUtil.isEmpty(selectValueList)) {
            return null;
        }
        if (selectValueList.size() < 4) {
            return selectValueList.stream().map(w -> w.getKey()).collect(Collectors.joining(","));
        }
        String collect = selectValueList.subList(0, 3).stream().map(w -> w.getValue()).collect(Collectors.joining(","));
        return StrUtil.format("{}等{}个",collect,selectValueList.size());
    }
}
