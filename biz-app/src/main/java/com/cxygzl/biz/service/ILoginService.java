package com.cxygzl.biz.service;

import com.cxygzl.biz.vo.UserBizVO;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.WeixinMiniAppQueryPhoneDto;

public interface ILoginService {

    /**
     * 登录
     *
     * @param userBizVO
     * @return
     */
    R login(UserBizVO userBizVO);

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

    /**
     * 获取登录地址
     * @return
     */
    R getLoginUrl();

    /**
     * 获取登录参数
     * @return
     */
    R getLoginParam();

    /**
     * 微信小程序--根据code登录
     * @param code
     * @return
     */
    R loginAtWxMiniAppByCode(String code);

    /**
     * 微信小程序获取手机号
     * @param appQueryPhoneDto
     * @return
     */
    R getPhoneDataAndLoginAtWxMiniApp(WeixinMiniAppQueryPhoneDto appQueryPhoneDto);

}
