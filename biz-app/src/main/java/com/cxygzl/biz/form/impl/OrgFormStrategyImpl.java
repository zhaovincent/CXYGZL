package com.cxygzl.biz.form.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.form.FormStrategy;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.JsonUtil;
import org.anyline.metadata.Column;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrgFormStrategyImpl implements InitializingBean, FormStrategy {
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(FormTypeEnum.SELECT_USER.getType());
        afterPropertiesSet(FormTypeEnum.SELECT_DEPT.getType());
        afterPropertiesSet(FormTypeEnum.SELECT_MULTI_DEPT.getType());
        afterPropertiesSet(FormTypeEnum.SELECT_MULTI_USER.getType());
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
            String format = StrUtil.format("`{}_id` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "longtext", formItemVO.getName());
            list.add(format);
        }
        {
            String format = StrUtil.format("`{}_name` {} NULL COMMENT '{}'", formItemVO.getId(),
                    "longtext", formItemVO.getName());
            list.add(format);
        }
        {
            String format = StrUtil.format("`{}_type` {} NULL COMMENT '{}'", formItemVO.getId(),
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
            Column column = new Column(StrUtil.format("{}_id", formItemVO.getId()), "longtext", 0);
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
            Column column = new Column(StrUtil.format("{}_type", formItemVO.getId()), "longtext", 0);
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
                StrUtil.format("{}_id", formItemVO.getId()),
                StrUtil.format("{}_name", formItemVO.getId()),
                StrUtil.format("{}_type", formItemVO.getId()),
                StrUtil.format("{}",
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

        List<NodeUser> nodeUserList = BeanUtil.copyToList(Convert.toList(value), NodeUser.class);

        if (CollUtil.isEmpty(nodeUserList)) {
            return null;
        }
        String id = nodeUserList.stream().map(w -> w.getId()).collect(Collectors.joining("||"));
        String name = nodeUserList.stream().map(w -> w.getName()).collect(Collectors.joining("||"));
        String type = nodeUserList.stream().map(w -> w.getType()).collect(Collectors.joining("||"));

        return CollUtil.newArrayList(id, name, type, JsonUtil.toJSONString(nodeUserList));
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
        List<NodeUser> nodeUserList = BeanUtil.copyToList(Convert.toList(value), NodeUser.class);
        if(CollUtil.isEmpty(nodeUserList)){
            return null;
        }
        if (nodeUserList.size() < 3) {
            return nodeUserList.stream().map(w -> w.getName()).collect(Collectors.joining(","));
        }
        String collect = nodeUserList.subList(0, 2).stream().map(w -> w.getName()).collect(Collectors.joining(","));
        return StrUtil.format("{} 等{}个",collect,nodeUserList.size());
    }
}
