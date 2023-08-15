package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.constants.SecurityConstants;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.ILoginService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.utils.DingTalkHttpUtil;
import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.constants.LoginPlatEnum;
import com.cxygzl.common.constants.StatusEnum;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate redisTemplate;
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
        StpUtil.login(userId,LoginPlatEnum.ADMIN.getType());

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

        StpUtil.logout(StpUtil.getLoginId(),LoginPlatEnum.ADMIN.getType());
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


        String userId = DingTalkHttpUtil.getUserIdByCode(authCode).getData();


        StpUtil.login(userId,LoginPlatEnum.DING_TALK.getType());

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
}
