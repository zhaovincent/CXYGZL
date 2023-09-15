package com.cxygzl.biz.form.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.UploadValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UploadFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.UPLOAD_FILE.getType());
        afterPropertiesSet(FormTypeEnum.UPLOAD_IMAGE.getType());
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
     * 查询sql的表字段
     *
     * @param formItemVO
     * @return
     */
    @Override
    public List<String> getInsertField(FormItemVO formItemVO) {
        return CollUtil.newArrayList(
                StrUtil.format("{}_url", formItemVO.getId()),
                StrUtil.format("{}_name",
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

        List<UploadValue> uploadValueList = BeanUtil.copyToList(Convert.toList(value), UploadValue.class);

        if (CollUtil.isEmpty(uploadValueList)) {
            return null;
        }
        String url = uploadValueList.stream().map(w -> w.getUrl()).collect(Collectors.joining("||"));
        String name = uploadValueList.stream().map(w -> w.getName()).collect(Collectors.joining("||"));

        return CollUtil.newArrayList(url,name);
    }
}
