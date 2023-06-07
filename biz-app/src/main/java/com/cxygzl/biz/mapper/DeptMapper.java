package com.cxygzl.biz.mapper;

import com.cxygzl.biz.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
public interface DeptMapper extends MPJBaseMapper<Dept> {

    List<Dept> selectChildrenByDept(@Param("deptId") long deptId);

    List<Dept> selectParentByDept(@Param("deptId") long deptId);

}
