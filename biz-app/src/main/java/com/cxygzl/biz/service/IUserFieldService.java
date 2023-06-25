package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.UserField;
import com.cxygzl.biz.vo.UserFieldParamVO;
import com.cxygzl.common.dto.R;

import java.util.List;

/**
 * <p>
 * 用户字段 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-17
 */
public interface IUserFieldService extends IService<UserField> {
    /**
     * 保存变量
     *
     * @param userFieldParamVOList
     * @return
     */
     R save(List<UserFieldParamVO> userFieldParamVOList);

    /**
     * 查询用户属性字段
     *
     * @return
     */
     R queryAll();

}
