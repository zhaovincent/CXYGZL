package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.biz.form.FormStrategyFactory;
import com.cxygzl.biz.utils.FormUtil;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.utils.JsonUtil;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LayoutFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.LAYOUT.getType());
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
            String format = StrUtil.format("`{}` {} NULL COMMENT '{}'", formItemVO.getId(),
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
        List<?> list = Convert.toList(value);


        if (CollUtil.isEmpty(list)) {
            return null;
        }

        return CollUtil.newArrayList(JsonUtil.toJSONString(list));
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

        List<JSONObject> jsonArray = JsonUtil.parseArray(JsonUtil.toJSONString(value));

        Object propsValue = formItemVO.getProps().getValue();

        List list=new ArrayList();

        for (JSONObject map : jsonArray) {
            List l=new ArrayList();

            List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, propsValue);
            for (FormItemVO itemVO : subItemList) {

                if (itemVO.getPrintable() != null && !itemVO.getPrintable()) {
                    continue;
                }



                Object value1 = map.get(itemVO.getId());

                FormUtil.handValue(itemVO, value1);


                Dict formItem = Dict.create()
                        .set("formName", itemVO.getName())
                        .set("formType", itemVO.getType())
                        .set("formValue", value1);
                //处理表单显示
                String printShow = FormStrategyFactory.getStrategy(itemVO.getType()).printShow(formItemVO,value1);
                formItem.set("formValueShow",printShow);
                l.add(formItem);

            }
            list.add(l);
        }
        return JsonUtil.toJSONString(list);
    }
}
