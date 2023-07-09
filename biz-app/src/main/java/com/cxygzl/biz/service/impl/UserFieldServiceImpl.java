package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.UserField;
import com.cxygzl.biz.entity.UserFieldData;
import com.cxygzl.biz.mapper.UserFieldMapper;
import com.cxygzl.biz.service.IUserFieldDataService;
import com.cxygzl.biz.service.IUserFieldService;
import com.cxygzl.biz.vo.UserFieldParamVO;
import com.cxygzl.common.dto.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户字段 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-17
 */
@Service
public class UserFieldServiceImpl extends ServiceImpl<UserFieldMapper, UserField> implements IUserFieldService {

    @Resource
    private IUserFieldDataService userFieldDataService;

    /**
     * 保存变量
     *
     * @param userFieldParamVOList
     * @return
     */
    @Override
    public R save(List<UserFieldParamVO> userFieldParamVOList) {

        {
            //先查询所有的
            List<UserField> userFieldListDb = this.lambdaQuery().ge(UserField::getId, 0).list();
            //判断是否删除了
            for (UserField userField : userFieldListDb) {
                boolean b = userFieldParamVOList.stream().anyMatch(w -> StrUtil.equals(w.getKey(), userField.getKey()));
                if(!b){
                    Long count = userFieldDataService.lambdaQuery().eq(UserFieldData::getKey, userField.getKey()).count();
                    if(count>0){
                        return com.cxygzl.common.dto.R.fail(StrUtil.format("字段[{}]已被使用，不能删除",userField.getName()));
                    }
                }
            }
        }
        LambdaQueryWrapper<UserField> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(UserField::getId,0);
        this.remove(queryWrapper);
        for (UserFieldParamVO userFieldParamVO : userFieldParamVOList) {
            UserField userField = BeanUtil.copyProperties(userFieldParamVO, UserField.class);
            if(StrUtil.isBlank(userField.getKey())){
                userField.setKey(IdUtil.fastSimpleUUID());
            }
            this.save(userField);
        }
        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 查询用户属性字段
     *
     * @return
     */
    @Override
    public R queryAll() {
        List<UserField> userFieldList = this.lambdaQuery().ge(UserField::getId, 0).list();

        return R.success(userFieldList);
    }
}
