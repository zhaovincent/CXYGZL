package com.cxygzl.biz.utils;

import com.cxygzl.biz.entity.Dept;

import java.util.ArrayList;
import java.util.List;

public class DeptUtil {

    public static List<Long> queryRootIdList(long deptId, List<Dept> deptList){
        if(deptId<=0){
            return new ArrayList<>();
        }
        List<Long> list=new ArrayList<>();
        Dept oaDepartments = deptList.stream().filter(w -> w.getId().intValue() == deptId).findFirst().get();
        Long parentId = oaDepartments.getParentId();
        list.add(deptId);
        List<Long> integers = queryRootIdList(parentId, deptList);
        list.addAll(integers);
        return list;
    }

}
