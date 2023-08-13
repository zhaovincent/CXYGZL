package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.dto.R;

public interface ILoginService {

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

    /**
     * 钉钉登录
     * @param authCode
     * @return
     */
    R loginAtDingTalk(String authCode);

}
