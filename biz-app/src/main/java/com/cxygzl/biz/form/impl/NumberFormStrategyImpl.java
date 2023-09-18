package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

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
        return value == null ? null : CollUtil.newArrayList(value.toString());
    }
}
