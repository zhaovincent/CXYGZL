package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.vo.UserListQueryVO;
import com.cxygzl.biz.vo.UserVO;
import com.github.yulichang.base.MPJBaseService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
public interface IUserService extends MPJBaseService<User> {
    /**
     * 登录
     * @param user
     * @return
     */
    Object login(User user);

    /**
     * 获取当前用户详细信息
     * @return
     */
    Object getCurrentUserDetail();
    /**
     * 创建用户
     * @param userVO
     * @return
     */
    Object createUser(UserVO userVO);

    /**
     * 修改用户
     *
     * @param userVO
     * @return
     */
    Object editUser(UserVO userVO);




    /**
     * 查询用户列表
     * @param userListQueryVO
     * @return
     */
    Object queryUserList(UserListQueryVO userListQueryVO);


}
