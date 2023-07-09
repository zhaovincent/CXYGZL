package com.cxygzl.biz.api;

import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.entity.User;

import java.util.List;

/**
 * 节点单个条件处理器
 */
public interface ApiStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        ApiStrategyFactory.register(key, this);
    }


    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     * @param roleIdList 角色id集合
     * @return
     */
    List<Long> loadUserIdListByRoleIdList(List<Long> roleIdList);


    /**
     * 获取所有的角色
     * @return
     */
    List<Role> loadAllRole();
    /**
     * 根据部门id集合获取该部门下的用户id集合
     * 注意：直属部门，不包括子级及以下部门
     * @param deptIdList 部门id集合
     * @return
     */
    List<Long> loadUserIdListByDeptIdList(List<Long> deptIdList);

    /**
     * 根据父级id获取所有的部门
     * 若父级id为空 则获取所有的部门
     * @param parentDeptId 父级id
     * @return
     */
    List<Dept> loadAllDept(Long parentDeptId);

    /**
     * 根据部门获取部门下的用户集合
     * @param deptId 部门id
     * @return
     */
    List<User> loadUserByDept(long deptId);

    /**
     * 根据用户id获取用户
     * @param userId
     * @return
     */
    User getUser(long userId);

    /**
     * 根据名字搜索用户
     * @param name
     * @return
     */
    List<User> searchUser(String name);

    /**
     * 根据token获取用户id
     * 处理登录接口请求的
     * @param token
     * @return
     */
    Long getUserIdByToken(String token);

}
