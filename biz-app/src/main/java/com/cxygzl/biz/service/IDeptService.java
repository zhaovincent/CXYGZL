package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.Dept;
import com.github.yulichang.base.MPJBaseService;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
public interface IDeptService extends MPJBaseService<Dept> {

    /**
     * 创建部门
     *
     * @param dept
     * @return
     */
    Object create(Dept dept);

    /**
     * 修改部门
     *
     * @param dept
     * @return
     */
    Object updateDept(Dept dept);

}
