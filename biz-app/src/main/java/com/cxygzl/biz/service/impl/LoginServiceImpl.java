package com.cxygzl.biz.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.SecurityConstants;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.entity.WeixinUser;
import com.cxygzl.biz.service.ILoginService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.service.IWeixinUserService;
import com.cxygzl.biz.utils.DingTalkHttpUtil;
import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.constants.LoginPlatEnum;
import com.cxygzl.common.constants.StatusEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.WeixinMiniAppQueryPhoneDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Resource
    private WxMaService wxMaService;
    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private IWeixinUserService weixinUserService;

    /**
     * 登录
     *
     * @param userVO
     * @return
     */
    @Override
    public R login(UserVO userVO) {

        Object cacheVerifyCode =
                redisTemplate.opsForValue().get(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + userVO.getVerifyCodeKey());
        if (cacheVerifyCode == null) {
            return com.cxygzl.common.dto.R.fail("验证码错误");
        } else {
            // 验证码比对
            if (!StrUtil.equals(userVO.getVerifyCode(), Convert.toStr(cacheVerifyCode))) {
                return com.cxygzl.common.dto.R.fail("验证码错误");

            }
        }

        String phone = userVO.getPhone();
        String password = userVO.getPassword();

        User u = userService.lambdaQuery()
                .eq(User::getPhone, phone)
                .eq(User::getPassword, password)
                .eq(User::getStatus, StatusEnum.ENABLE.getValue())

                .one();
        if (u == null) {
            return R.fail("账号或者密码错误");
        }


        StpUtil.login(u.getId(), LoginPlatEnum.ADMIN.getType());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return com.cxygzl.common.dto.R.success(tokenInfo);
    }

    /**
     * token登录
     *
     * @param token
     * @return
     */
    @Override
    public R loginByToken(String token) {
        String userId = ApiStrategyFactory.getStrategy().getUserIdByToken(token);
        StpUtil.login(userId, LoginPlatEnum.ADMIN.getType());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return com.cxygzl.common.dto.R.success(tokenInfo);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R logout() {

        StpUtil.logout(StpUtil.getLoginId(), LoginPlatEnum.ADMIN.getType());
        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 钉钉登录
     *
     * @param authCode
     * @return
     */
    @Override
    public R loginAtDingTalk(String authCode) {


        String userId = DingTalkHttpUtil.getUserIdByCodeAtMiniApp(authCode).getData();


        StpUtil.login(userId, LoginPlatEnum.DING_TALK.getType());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return com.cxygzl.common.dto.R.success(tokenInfo);
    }

    /**
     * 获取登录地址
     *
     * @return
     */
    @Override
    public R getLoginUrl() {
        return R.success(ApiStrategyFactory.getStrategy().getLoginUrl());
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    @Override
    public R getLoginParam() {
        return R.success(ApiStrategyFactory.getStrategy().getLoginParam());

    }

    /**
     * 微信小程序--根据code登录
     *
     * @param code
     * @return
     */
    @SneakyThrows
    @Override
    public R loginAtWxMiniAppByCode(String code) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String sessionKey = session.getSessionKey();
            String openid = session.getOpenid();
            String unionid = session.getUnionid();

            WeixinUser weixinUser = weixinUserService.lambdaQuery()
                    .eq(WeixinUser::getOpenId, openid)
                    .eq(StrUtil.isNotBlank(unionid), WeixinUser::getUnionId, unionid)
                    .one();
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set("weixin-session-openid-" + sessionKey, openid, 7, TimeUnit.DAYS);
            if (StrUtil.isNotBlank(unionid)) {
                valueOperations.set("weixin-session-unionid-" + sessionKey, unionid, 7, TimeUnit.DAYS);
            }
            if (weixinUser != null && StrUtil.isBlank(weixinUser.getUserId())) {
                String phone = weixinUser.getPhone();
                User user = userService.lambdaQuery().eq(User::getPhone, phone).one();
                if (user == null) {
//                    return R.fail("用户未注册，请联系管理员");
                    Dict set = Dict.create().set("loginResult", 3);
                    return R.success(set);
                }
                weixinUser.setUserId(String.valueOf(user.getId()));
                weixinUserService.updateById(weixinUser);
                StpUtil.login(weixinUser.getUserId(), LoginPlatEnum.WX_MIN_APP.getType());

                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

                Dict set = Dict.create().set("tokenInfo", tokenInfo).set("loginResult", 2);
                return R.success(set);
            }
            if (weixinUser == null || StrUtil.isBlank(weixinUser.getUserId())) {
                Dict set = Dict.create().set("sessionKey", sessionKey).set("loginResult", 1);
                return R.success(set);
            }
            StpUtil.login(weixinUser.getUserId(), LoginPlatEnum.WX_MIN_APP.getType());

            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

            Dict set = Dict.create().set("tokenInfo", tokenInfo).set("loginResult", 2);
            return R.success(set);
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * 微信小程序获取手机号
     *
     * @param appQueryPhoneDto
     * @return
     */
    @SneakyThrows
    @Override
    public R getPhoneDataAndLoginAtWxMiniApp(WeixinMiniAppQueryPhoneDto appQueryPhoneDto) {
        // 用户信息校验
        try {


            ValueOperations valueOperations = redisTemplate.opsForValue();
            Object openIdObj = valueOperations.get("weixin-session-openid-" + appQueryPhoneDto.getSessionKey());
            Object unionIdObj = valueOperations.get("weixin-session-unionid-" + appQueryPhoneDto.getSessionKey());
            String unionId = Convert.toStr(unionIdObj);

            WeixinUser weixinUser = weixinUserService.lambdaQuery()
                    .eq(WeixinUser::getOpenId, Convert.toStr(openIdObj))
                    .eq(StrUtil.isNotBlank(unionId), WeixinUser::getUnionId, unionId)
                    .one();
            if (weixinUser != null && StrUtil.isNotBlank(weixinUser.getUserId())) {

                StpUtil.login(weixinUser.getUserId(), LoginPlatEnum.WX_MIN_APP.getType());

                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

                return R.success(tokenInfo);
            } else if (weixinUser != null) {

                String phone = weixinUser.getPhone();
                User user = userService.lambdaQuery().eq(User::getPhone, phone).one();
                if (user == null) {
                    return R.fail("用户未注册，请联系管理员");

                }
                weixinUser.setUserId(String.valueOf(user.getId()));
                weixinUserService.updateById(weixinUser);
                StpUtil.login(weixinUser.getUserId(), LoginPlatEnum.WX_MIN_APP.getType());

                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

                return R.success(tokenInfo);
            }


            // 解密

            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(appQueryPhoneDto.getCode());


            String phoneNumber = phoneNoInfo.getPurePhoneNumber();

            User user = userService.lambdaQuery().eq(User::getPhone, phoneNumber).one();

            if (weixinUser != null) {
                if (user != null) {
                    weixinUser.setUserId(String.valueOf(user.getId()));
                    weixinUserService.updateById(weixinUser);
                }

            } else {
                WeixinUser wu = new WeixinUser();
                wu.setUserId(user == null ? "" : String.valueOf(user.getId()));
                wu.setUnionId(unionId);
                wu.setOpenId(openIdObj.toString());
                wu.setPhone(phoneNumber);
                weixinUserService.save(wu);

            }


            if (user == null) {
                return R.fail("用户未注册，请联系管理员");
            }
            StpUtil.login(user.getId(), LoginPlatEnum.WX_MIN_APP.getType());

            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

            return R.success(tokenInfo);
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }
}
