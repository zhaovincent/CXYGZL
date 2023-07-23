package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.mapper.DeptMapper;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.utils.DataUtil;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.DeptDto;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

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


    /**
     * 创建部门
     *
     * @param dept
     * @return
     */
    @Override
    public R create(Dept dept) {

        this.save(dept);

        return R.success();
    }

    @Override
    public R updateDept(Dept dept) {
        List<DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);

        List<DeptDto> deptList = DataUtil.selectChildrenByDept(String.valueOf(dept.getId()), allDept);


        boolean b = deptList.stream().anyMatch(w -> w.getId().equals(dept.getParentId()));
        if(b){
            return com.cxygzl.common.dto.R.fail("当前部门的父级部门不能是当前部门或者当前部门的子级部门");
        }

        this.updateById(dept);
        return com.cxygzl.common.dto.R.success("修改成功");
    }

}
