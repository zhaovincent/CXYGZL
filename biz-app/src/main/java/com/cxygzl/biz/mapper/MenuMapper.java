package com.cxygzl.biz.mapper;

import com.cxygzl.biz.entity.Menu;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author Vincent
 * @since 2023-06-10
 */
public interface MenuMapper extends MPJBaseMapper<Menu> {
    List<Menu> selectChildrenByMenu(@Param("menuId") long menuId);

    List<Menu> selectParentByMenu(@Param("menuId") long menuId);
}
