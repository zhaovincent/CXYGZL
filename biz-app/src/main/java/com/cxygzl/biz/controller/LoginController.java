package com.cxygzl.biz.controller;

import com.cxygzl.biz.security.captcha.EasyCaptchaService;
import com.cxygzl.biz.service.ILoginService;
import com.cxygzl.biz.vo.UserBizVO;
import com.cxygzl.common.config.NotWriteLogAnno;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.WeixinMiniAppQueryPhoneDto;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户
 */
@RestController
@RequestMapping(value = {"login", "api/login"})
public class LoginController {

    @Resource
    private ILoginService loginService;

    @Resource
    private EasyCaptchaService captchaService;

    /**
     * 获取验证码
     *
     * @return
     */
    @GetMapping("/captcha")
    @NotWriteLogAnno(printResultLog = false)
    public R getCaptcha() {
        return captchaService.getCaptcha();
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @SneakyThrows
    @PostMapping("login")
    public R login(@RequestBody UserBizVO user) {

        return loginService.login(user);

    }

    /**
     * h5用户登录
     *
     * @param user
     * @return
     */
    @SneakyThrows
    @PostMapping("loginAtH5")
    public R loginAtH5(@RequestBody UserBizVO user) {

        return loginService.loginAtH5(user);

    }

    /**
     * 用户token自动登录
     *
     * @param token
     * @return
     */
    @SneakyThrows
    @GetMapping("loginByToken")
    public R loginByToken(String token) {

        return loginService.loginByToken(token);

    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("logout")
    public R logout() {
        return loginService.logout();
    }

    /**
     * 钉钉登录
     *
     * @param authCode
     * @return
     */
    @GetMapping("/loginAtDingTalk")
    public R loginAtDingTalk(String authCode) {
        return loginService.loginAtDingTalk(authCode);
    }

    /**
     * 获取登录地址
     *
     * @return
     */
    @GetMapping("/getLoginUrl")
    public R getLoginUrl() {
        return loginService.getLoginUrl();
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    @GetMapping("/getLoginParam")
    public R getLoginParam() {
        return loginService.getLoginParam();
    }

    /**
     * 微信小程序--根据code登录
     *
     * @param code
     * @return
     */
    @GetMapping("loginAtWxMiniAppByCode")
    public R loginAtWxMiniAppByCode(String code) {
        return loginService.loginAtWxMiniAppByCode(code);
    }
    /**
     * 微信小程序获取手机号登录
     * @param appQueryPhoneDto
     * @return
     */
    @PostMapping("getPhoneDataAndLoginAtWxMiniApp")
   public R getPhoneDataAndLoginAtWxMiniApp(@RequestBody WeixinMiniAppQueryPhoneDto appQueryPhoneDto){
        return loginService.getPhoneDataAndLoginAtWxMiniApp(appQueryPhoneDto);
    }


}
