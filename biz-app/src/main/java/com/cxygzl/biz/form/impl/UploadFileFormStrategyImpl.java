package com.cxygzl.biz.form.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.UploadValue;
import com.cxygzl.common.utils.JsonUtil;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UploadFileFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.UPLOAD_FILE.getType());
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
            String format = StrUtil.format("`{}_url` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "longtext", formItemVO.getName());
            list.add(format);
        }
        {
            String format = StrUtil.format("`{}_name` {} NULL COMMENT '{}'", formItemVO.getId(),
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
            Column column = new Column(StrUtil.format("{}_url", formItemVO.getId()), "longtext", 0);
            column.setNullable(true);
            column.setComment(formItemVO.getName());

            list.add(column);
        }


        {
            Column column = new Column(StrUtil.format("{}_name", formItemVO.getId()), "longtext", 0);
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
                StrUtil.format("{}_url", formItemVO.getId()),
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

        List<UploadValue> uploadValueList = BeanUtil.copyToList(Convert.toList(value), UploadValue.class);

        if (CollUtil.isEmpty(uploadValueList)) {
            return null;
        }
        String url = uploadValueList.stream().map(w -> w.getUrl()).collect(Collectors.joining("||"));
        String name = uploadValueList.stream().map(w -> w.getName()).collect(Collectors.joining("||"));

        return CollUtil.newArrayList(url, name, JsonUtil.toJSONString(uploadValueList));
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
        List<UploadValue> uploadValueList = BeanUtil.copyToList(Convert.toList(value), UploadValue.class);
        if(CollUtil.isEmpty(uploadValueList)){
            return null;
        }

        return StrUtil.format("{}个", uploadValueList.size());
    }

    /**
     * 数据的长度
     *
     * @param s
     * @return
     */
    @Override
    public int length(String s) {
        List<UploadValue> list = JsonUtil.parseArray(s, UploadValue.class);

        return list.size();
    }

    /**
     * 获取excel显示内容
     *
     * @param s
     * @param index
     * @return
     */
    @Override
    public String getExcelShow(String s, int index) {
        List<UploadValue> list = JsonUtil.parseArray(s, UploadValue.class);

        return list.get(index).getUrl();
    }
}
