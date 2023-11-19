package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.CascadeFormValue;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.utils.JsonUtil;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CascadeFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.CASCADE.getType());
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
        CascadeFormValue areaFormValue = JsonUtil.parseObject(JsonUtil.toJSONString(value), CascadeFormValue.class);
        if (!StrUtil.isAllNotBlank(areaFormValue.getKey(), areaFormValue.getLabel())) {
            return null;
        }

        return CollUtil.newArrayList(areaFormValue.getKey(), areaFormValue.getLabel(), JsonUtil.toJSONString(areaFormValue));
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
        try {
            CascadeFormValue areaFormValue = JsonUtil.parseObject(JsonUtil.toJSONString(value), CascadeFormValue.class);
            return CollUtil.join(areaFormValue.getLabelList(), " / ");
        } catch (Exception e) {
            return "";
        }

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
        try {
            CascadeFormValue areaFormValue = JsonUtil.parseObject(s, CascadeFormValue.class);
            return CollUtil.join(areaFormValue.getLabelList(), " / ");
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 获取流程详情 excel显示
     *
     * @param s
     * @return
     */
    @Override
    public String getProcessInstanceExcelShow(String s) {

        try {
            CascadeFormValue areaFormValue = JsonUtil.parseObject(s, CascadeFormValue.class);
            return CollUtil.join(areaFormValue.getLabelList(), " / ");
        } catch (Exception e) {
            return "";
        }

    }
}
