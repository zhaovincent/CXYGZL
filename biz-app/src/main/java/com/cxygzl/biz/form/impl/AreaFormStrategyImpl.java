package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.AreaFormValue;
import com.cxygzl.common.dto.flow.FormItemVO;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AreaFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.AREA.getType());
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
            String format = StrUtil.format("`{}_code` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "varchar(20)", formItemVO.getName());
            list.add(format);
        }
        {
            String format = StrUtil.format("`{}_name` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "varchar(100)", formItemVO.getName());
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
            Column column = new Column(StrUtil.format("{}_code", formItemVO.getId()), "varchar", 20);
            column.setNullable(true);
            column.setComment(formItemVO.getName());
            list.add(column);
        }
        {
            Column column = new Column(StrUtil.format("{}_name", formItemVO.getId()), "varchar", 100);
            column.setNullable(true);
            column.setComment(formItemVO.getName());
            list.add(column);
        }
        {
            Column column = new Column(StrUtil.format("{}", formItemVO.getId()), "varchar", 1000);
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
                StrUtil.format("{}_code", formItemVO.getId()),
                StrUtil.format("{}_name", formItemVO.getId()),
                StrUtil.format("{}", formItemVO.getId())
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
        if (value == null) {
            return null;
        }
        AreaFormValue areaFormValue = JSON.parseObject(JSON.toJSONString(value), AreaFormValue.class);
        if (!StrUtil.isAllNotBlank(areaFormValue.getCode(), areaFormValue.getName())) {
            return null;
        }

        return CollUtil.newArrayList(areaFormValue.getCode(), areaFormValue.getName(), JSON.toJSONString(areaFormValue));
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
        AreaFormValue areaFormValue = JSON.parseObject(JSON.toJSONString(value), AreaFormValue.class);
        return areaFormValue.getName();
    }
}
