package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.security.captcha.EasyCaptchaService;
import com.cxygzl.biz.service.IOrgService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.vo.UserListQueryVO;
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
@RequestMapping(value = {"user","api/user"})
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private IOrgService orgService;

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
     * 创建用户
     * @param user
     * @return
     */
    @PostMapping("create")
    public Object create(@RequestBody UserVO user){
        return userService.createUser(user);
    }
    /**
     * 修改用户
     * @param user
     * @return
     */
    @PutMapping("edit")
    public R editUser(@RequestBody UserVO user){
        return userService.editUser(user);
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

        return userService.login(user);

    }
    /**
     * 用户token自动登录
     *
     * @param token
     * @return
     */
    @SneakyThrows
    @GetMapping("loginByToken")
    public R loginByToken ( String token) {

        return userService.loginByToken(token);

    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("logout")
    public R logout() {

        return userService.logout();

    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @PostMapping("password")
    public R password(@RequestBody User user){
        return userService.password(user);
    }
    /**
     * 修改状态
     * @param user
     * @return
     */
    @PostMapping("status")
    public R status(@RequestBody User user){
        return userService.status(user);
    }

    /**
     * 获取当前用户详细信息
     *

     * @return
     */
    @SneakyThrows
    @GetMapping("getCurrentUserDetail")
    public R getCurrentUserDetail() {

        return userService.getCurrentUserDetail();

    }

    /**
     * 获取用户详细信息
     * @param user
     * @return
     */
    @GetMapping("getUserDetail")
    public R getUserDetail(long userId){
        return orgService.getUserDetail(userId);
    }


    /**
     * 查询用户列表
     * @param userListQueryVO
     * @return
     */
    @PostMapping("queryList")
    public Object queryList(@RequestBody UserListQueryVO userListQueryVO){
        return userService.queryList(userListQueryVO);
    }
    /**
     * 删除用户
     * @param userListQueryVO
     * @return
     */
    @DeleteMapping("delete")
    public Object delete(@RequestBody User userListQueryVO){
        return orgService.delete(userListQueryVO);
    }
}
