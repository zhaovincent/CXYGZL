package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.mapper.DeptMapper;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.utils.R;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
@Service
public class DeptServiceImpl extends MPJBaseServiceImpl<DeptMapper, Dept> implements IDeptService {


    @Resource
    private DeptMapper deptMapper;
    /**
     * 创建部门
     *
     * @param dept
     * @return
     */
    @Override
    public Object create(Dept dept) {

        this.save(dept);

        return R.ok("创建成功");
    }

    @Override
    public Object updateDept(Dept dept) {

        List<Dept> deptList = deptMapper.selectChildrenByDept(dept.getId());
        boolean b = deptList.stream().anyMatch(w -> w.getId().longValue() == dept.getParentId().longValue());
        if(b){
            return R.badRequest("当前部门的父级部门不能是当前部门或者当前部门的子级部门");
        }

        this.updateById(dept);
        return R.ok("修改成功");
    }

}
