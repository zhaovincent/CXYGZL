package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.dto.R;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
public interface ILoginService  {
    /**
     * 登录
     *
     * @param userVO
     * @return
     */
    R login(UserVO userVO);

    /**
     * token登录
     *
     * @param token
     * @return
     */
    R loginByToken(String token);

    /**
     * 退出登录
     * @return
     */
    R logout();


}
