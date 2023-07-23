package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.User;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface IOrgService {




    /**
     * 查询组织架构树
     *
     * @param deptId    部门id
     * @param type      只查询部门架构
     * @param showLeave 是否显示离职员工
     * @return 组织架构树数据
     */
    Object getOrgTreeData(String deptId, String type, Boolean showLeave);


    /**
     * 查询所有的组织架构 并树形显示
     * @return
     */
    Object getOrgTreeDataAll(String keywords,Integer status);

    /**
     * 模糊搜索用户
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    Object getOrgTreeUser(String userName);


    /**
     * 删除部门
     *
     * @param dept
     * @return
     */
    Object delete(Dept dept);
    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    Object getUserDetail(long userId);


    /**
     * 用户离职
     * @param user
     * @return
     */
    Object delete(User user);

}
