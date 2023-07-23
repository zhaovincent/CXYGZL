package com.cxygzl.biz.utils;

import cn.hutool.core.collection.CollUtil;
import com.cxygzl.biz.entity.Menu;
import com.cxygzl.common.dto.third.DeptDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtil {

    /**
     * 根据当前部门id，向上查找所有的部门
     *
     * @param deptId
     * @param allDeptList
     * @return 返回包括自己部门在内的所有父级部门
     */
    public static List<DeptDto> selectParentByDept(String deptId, List<DeptDto> allDeptList) {
        List<DeptDto> list=new ArrayList<>();
        DeptDto dept = allDeptList.stream().filter(w -> w.getId().equals(deptId)).findAny().orElse(null);
        if (dept == null || dept.getParentId() == null) {
            return list;
        }
        String parentId = dept.getParentId();
        List<DeptDto> depts = selectParentByDept(parentId, allDeptList);
        list.add(dept);
        list.addAll(depts);
        return list;

    }

    /**
     * 根据当前部门id，向下查找所有的部门
     *
     * @param deptId
     * @param allDeptList
     * @return 返回包括自己部门在内的所有子级部门
     */
    public static List<DeptDto> selectChildrenByDept(String deptId, List<DeptDto> allDeptList) {
        List<DeptDto> list=new ArrayList<>();
        list.add(allDeptList.stream().filter(w->w.getId().equals(deptId)).findFirst().get());

        List<DeptDto> collect =
                allDeptList.stream().filter(w -> w.getParentId().equals(deptId)).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return list;
        }
        for (DeptDto dept : collect) {
            List<DeptDto> depts = selectChildrenByDept(dept.getId(), allDeptList);
            list.addAll(depts);
        }
        return list;

    }
    /**
     * 根据当前菜单id，向下查找所有的菜单
     *
     * @param menuId
     * @param allMenuList
     * @return 返回包括自己在内的所有子级菜单
     */
    public static List<Menu> selectChildrenByMenu(long menuId, List<Menu> allMenuList) {
        List<Menu> list=new ArrayList<>();
        list.add(allMenuList.stream().filter(w->w.getId().longValue()==menuId).findFirst().get());

        List<Menu> collect = allMenuList.stream().filter(w -> w.getParentId().longValue() == menuId).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return list;
        }
        for (Menu dept : collect) {
            List<Menu> depts = selectChildrenByMenu(dept.getId(), allMenuList);
            list.addAll(depts);
        }
        return list;

    }

}
