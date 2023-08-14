package com.cxygzl.biz.controller;

import com.cxygzl.biz.security.captcha.EasyCaptchaService;
import com.cxygzl.biz.service.ILoginService;
import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.config.NotWriteLogAnno;
import com.cxygzl.common.dto.R;
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
    public R login(@RequestBody UserVO user) {

        return loginService.login(user);

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

}
