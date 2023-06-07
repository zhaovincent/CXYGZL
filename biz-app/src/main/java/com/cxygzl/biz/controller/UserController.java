package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.service.IOrgService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.vo.UserListQueryVO;
import com.cxygzl.biz.vo.UserVO;
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
    /**
     * 创建用户
     * @param user
     * @return
     */
    @PostMapping("createUser")
    public Object createUser(@RequestBody UserVO user){
        return userService.createUser(user);
    }
    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping("editUser")
    public Object editUser(@RequestBody UserVO user){
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
    public Object login(@RequestBody User user) {

        return userService.login(user);

    }

    /**
     * 获取当前用户详细信息
     *

     * @return
     */
    @SneakyThrows
    @PostMapping("getCurrentUserDetail")
    public Object getCurrentUserDetail() {

        return userService.getCurrentUserDetail();

    }

    /**
     * 获取用户详细信息
     * @param user
     * @return
     */
    @GetMapping("getUserDetail")
    public Object getUserDetail(long userId){
        return orgService.getUserDetail(userId);
    }


    /**
     * 查询用户列表
     * @param userListQueryVO
     * @return
     */
    @PostMapping("queryUserList")
    public Object queryUserList(@RequestBody UserListQueryVO userListQueryVO){
        return userService.queryUserList(userListQueryVO);
    }
    /**
     * 离职
     * @param userListQueryVO
     * @return
     */
    @PostMapping("leave")
    public Object leave(@RequestBody User userListQueryVO){
        return orgService.leave(userListQueryVO);
    }
}
