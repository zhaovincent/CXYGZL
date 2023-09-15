package com.cxygzl.biz.form.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.AreaFormValue;
import com.cxygzl.common.dto.flow.FormItemVO;
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
     * 查询sql的表字段
     *
     * @param formItemVO
     * @return
     */
    @Override
    public List<String> getInsertField(FormItemVO formItemVO) {
        return CollUtil.newArrayList(
                StrUtil.format("{}_code", formItemVO.getId()),
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
        if (value == null) {
            return null;
        }
        AreaFormValue areaFormValue = JSON.parseObject(JSON.toJSONString(value), AreaFormValue.class);
        if (!StrUtil.isAllNotBlank(areaFormValue.getCode(), areaFormValue.getName())) {
            return null;
        }

        return CollUtil.newArrayList(areaFormValue.getCode(), areaFormValue.getName());
    }
}
