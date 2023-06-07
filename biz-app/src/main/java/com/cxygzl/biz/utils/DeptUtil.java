package com.cxygzl.biz.utils;

import com.cxygzl.biz.entity.Dept;

import java.util.ArrayList;
import java.util.List;

public class DeptUtil {

    public static List<Long> queryRootIdList(long depId, List<Dept> deptList){
        if(depId<=0){
            return new ArrayList<>();
        }
        List<Long> list=new ArrayList<>();
        Dept oaDepartments = deptList.stream().filter(w -> w.getId().intValue() == depId).findFirst().get();
        Long parentId = oaDepartments.getParentId();
        list.add(depId);
        List<Long> integers = queryRootIdList(parentId, deptList);
        list.addAll(integers);
        return list;
    }

}
