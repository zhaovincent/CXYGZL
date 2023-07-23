package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IMenuService;
import com.cxygzl.biz.vo.MenuVO;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author Vincent
 * @since 2023-06-10
 */
@RestController
@RequestMapping(value = {"menu","api/menu"})
public class MenuController {

    @Resource
    private IMenuService menuService;


    /**
     * 路由列表
     *
     * @return
     */
    @GetMapping("routes")
    public R routes() {
        return menuService.routes();
    }
    /**
     * 菜单树形列表
     *
     * @return
     */
    @GetMapping("treeAll")
    public R treeAll(String keywords,Integer status) {
        return menuService.treeAll(keywords, status);
    }
    /**
     * 创建菜单
     * @param menuVO
     * @return
     */
    @PostMapping("create")
    public R create(@RequestBody MenuVO menuVO){
        return menuService.create(menuVO);
    }
    /**
     * 编辑菜单
     * @param menuVO
     * @return
     */
    @PutMapping("edit")
    public R edit(@RequestBody MenuVO menuVO){
        return menuService.edit(menuVO);
    }
    /**
     * 删除菜单
     * @param menuVO
     * @return
     */
    @DeleteMapping("delete")
    public R delete(@RequestBody MenuVO menuVO){
        return menuService.delete(menuVO);
    }
}
