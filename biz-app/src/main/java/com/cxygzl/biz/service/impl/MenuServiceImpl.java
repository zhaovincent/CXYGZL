package com.cxygzl.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cxygzl.biz.bo.RouteBO;
import com.cxygzl.biz.constants.MenuTypeEnum;
import com.cxygzl.biz.constants.StatusEnum;
import com.cxygzl.biz.constants.SystemConstants;
import com.cxygzl.biz.entity.Menu;
import com.cxygzl.biz.entity.Role;
import com.cxygzl.biz.entity.RoleMenu;
import com.cxygzl.biz.mapper.MenuMapper;
import com.cxygzl.biz.service.IMenuService;
import com.cxygzl.biz.service.IRoleMenuService;
import com.cxygzl.biz.vo.MenuVO;
import com.cxygzl.biz.vo.RouteVO;
import com.cxygzl.common.dto.R;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-06-10
 */
@Service
public class MenuServiceImpl extends MPJBaseServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Resource
    private IRoleMenuService roleMenuService;

    /**
     * 路由列表
     *
     * @return
     */
    @Override
    public R routes() {

        MPJLambdaWrapper<Menu> lambdaQueryWrapper=new MPJLambdaWrapper<Menu>()
                .select(
                        Menu::getId,
                        Menu::getName,
                        Menu::getParentId,
                        Menu::getPath,
                        Menu::getComponent,
                        Menu::getIcon,
                        Menu::getVisible,
                        Menu::getRedirect,
                        Menu::getType
                        )
                .select(Role::getKey)
                .leftJoin(RoleMenu.class,RoleMenu::getMenuId,Menu::getId)
                .leftJoin(Role.class,Role::getId,RoleMenu::getRoleId)
                .ne(Menu::getType, MenuTypeEnum.BUTTON.getValue())
                .orderByAsc(Menu::getSort)
                ;

        List<RouteBO> tempMenuList = this.selectJoinList(RouteBO.class,lambdaQueryWrapper);

        Set<Long> collect = tempMenuList.stream().map(w -> w.getId()).collect(Collectors.toSet());

        List<RouteBO> menuList=new ArrayList<>();
        for (Long aLong : collect) {
            List<RouteBO> collect1 = tempMenuList.stream().filter(w -> w.getId().longValue() == aLong).collect(Collectors.toList());
            RouteBO routeBO = collect1.get(0);
            List<String> collect2 = collect1.stream().map(w -> w.getKey()).collect(Collectors.toList());
            routeBO.setRoles(collect2);
            menuList.add(routeBO);
        }

        List<RouteVO> routeVOS = recurRoutes(SystemConstants.ROOT_NODE_ID, menuList);
        return R.success(routeVOS);
    }

    /**
     * 根据角色id集合查询权限集合
     *
     * @param roleKeySet
     * @return
     */
    @Override
    public R<Set<String>> listRolePerms(Set<String> roleKeySet) {

        MPJLambdaWrapper<Menu> lambdaQueryWrapper=new MPJLambdaWrapper<Menu>()
                .distinct()
                .select(
                        Menu::getPerm
                )
                .leftJoin(RoleMenu.class,RoleMenu::getMenuId,Menu::getId)
                .leftJoin(Role.class,Role::getId,RoleMenu::getRoleId)
                .eq(Menu::getType, MenuTypeEnum.BUTTON.getValue())
                .isNotNull(Menu::getPerm)
                .in(Role::getKey,roleKeySet)
                ;
        List<String> strings = this.selectJoinList(String.class, lambdaQueryWrapper);
        return R.success(CollUtil.newHashSet(strings));
    }

    /**
     * 树形显示菜单
     *
     * @return
     */
    @Override
    public R treeAll(String keywords, Integer status) {

        List<Menu> menus = this.list(new LambdaQueryWrapper<Menu>()
                .like(StrUtil.isNotBlank(keywords), Menu::getName,keywords)
                .orderByAsc(Menu::getSort)
        );

        Set<Long> parentIds = menus.stream()
                .map(Menu::getParentId)
                .collect(Collectors.toSet());

        Set<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toSet());

        List<Long> rootIds = CollectionUtil.subtractToList(parentIds, menuIds); // 求差集，得到 parentIds 中 menuIds 没有的元素

        List<MenuVO> list = new ArrayList<>();
        for (Long rootId : rootIds) {
            list.addAll(recurMenus(rootId, menus)); // 递归
        }
        return R.success(list);
    }

    /**
     * 创建菜单
     *
     * @param menuVO
     * @return
     */
    @Override
    public R create(MenuVO menuVO) {

        //权限标识
        if(StrUtil.isNotBlank(menuVO.getPerm())){
            Long count = this.lambdaQuery().eq(Menu::getPerm, menuVO.getPerm()).count();
            if(count>0){
                return R.fail("权限标识已存在");

            }
        }

        //路由路径
        if(StrUtil.isNotBlank(menuVO.getPath())){
            Long count = this.lambdaQuery().eq(Menu::getPath, menuVO.getPath()).count();
            if(count>0){
                return R.fail("路由路径已存在");

            }
        }

        //组件路径
        if(StrUtil.isNotBlank(menuVO.getComponent())){
            Long count = this.lambdaQuery().ne(Menu::getType,MenuTypeEnum.CATALOG.getValue()).eq(Menu::getComponent, menuVO.getComponent()).count();
            if(count>0){
                return R.fail("组件路径已存在");

            }
        }

        Menu menu = BeanUtil.copyProperties(menuVO, Menu.class, "type");
        menu.setType(menuVO.getType().getValue());
        if(menu.getType()==MenuTypeEnum.CATALOG.getValue().intValue()){
            if (NumberUtil.equals(menu.getParentId(), 0) && !menu.getPath().startsWith("/")) {
                menu.setPath("/" + menu.getPath()); // 一级目录需以 / 开头
            }
            menu.setComponent("Layout");
        }
        if(menu.getType()==MenuTypeEnum.EXTLINK.getValue().intValue()){
            menu.setComponent(null);

        }


        String treePath = generateMenuTreePath(menu.getParentId());
        menu.setTreePath(treePath);
        this.save(menu);
        return R.success();
    }
    /**
     * 部门路径生成
     *
     * @param parentId 父ID
     * @return 父节点路径以英文逗号(, )分割，eg: 1,2,3
     */
    private String generateMenuTreePath(Long parentId) {
        String treePath = null;
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            treePath = String.valueOf(parentId);
        } else {
            Menu parent = this.getById(parentId);
            if (parent != null) {
                treePath = parent.getTreePath() + "," + parent.getId();
            }
        }
        return treePath;
    }
    /**
     * 编辑菜单
     *
     * @param menuVO
     * @return
     */
    @Override
    public R edit(MenuVO menuVO) {

        if(menuVO.getId().longValue()==menuVO.getParentId()){
            return R.fail("不能修改上级为自己");
        }

        List<Menu> menuList = this.baseMapper.selectChildrenByMenu(menuVO.getId());
        boolean match = menuList.stream().anyMatch(w -> w.getId().longValue() == menuVO.getParentId());
        if(match){
            return R.fail("上级菜单不能为自己的子级菜单");

        }
        //权限标识
        if(StrUtil.isNotBlank(menuVO.getPerm())){
            Long count = this.lambdaQuery().eq(Menu::getPerm, menuVO.getPerm()).ne(Menu::getId, menuVO.getId()).count();
            if(count>0){
                return R.fail("权限标识已存在");

            }
        }

        //路由路径
        if(StrUtil.isNotBlank(menuVO.getPath())){
            Long count = this.lambdaQuery().eq(Menu::getPath, menuVO.getPath()).ne(Menu::getId, menuVO.getId()).count();
            if(count>0){
                return R.fail("路由路径已存在");

            }
        }

        //组件路径
        if(StrUtil.isNotBlank(menuVO.getComponent())){
            Long count = this.lambdaQuery().ne(Menu::getType,MenuTypeEnum.CATALOG.getValue()).eq(Menu::getComponent, menuVO.getComponent()).ne(Menu::getId, menuVO.getId()).count();
            if(count>0){
                return R.fail("组件路径已存在");

            }
        }


        Menu menu = BeanUtil.copyProperties(menuVO, Menu.class, "type");
        menu.setType(menuVO.getType().getValue());
        String treePath = generateMenuTreePath(menu.getParentId());
        menu.setTreePath(treePath);
        this.updateById(menu);
        return R.success();
    }

    /**
     * 删除菜单
     *
     * @param menuVO
     * @return
     */
    @Transactional
    @Override
    public R delete(MenuVO menuVO) {
        List<Menu> menuList = this.baseMapper.selectChildrenByMenu(menuVO.getId());
        Set<Long> menuIdSet = menuList.stream().map(w -> w.getId()).collect(Collectors.toSet());
        this.removeBatchByIds(menuIdSet);

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleMenu::getMenuId,menuIdSet);
        roleMenuService.remove(queryWrapper);

        return R.success();
    }

    /**
     * 递归生成菜单列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<MenuVO> recurMenus(Long parentId, List<Menu> menuList) {
        return CollectionUtil.emptyIfNull(menuList)
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    MenuVO menuVO = BeanUtil.copyProperties(entity, MenuVO.class);
                    List<MenuVO> children = recurMenus(entity.getId(), menuList);
                    menuVO.setChildren(children);
                    return menuVO;
                }).collect(Collectors.toList());
    }
    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<RouteVO> recurRoutes(Long parentId, List<RouteBO> menuList) {
        return CollectionUtil.emptyIfNull(menuList).stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> {
                    RouteVO routeVO = new RouteVO();
                    MenuTypeEnum menuTypeEnum = menu.getType();
                    if (MenuTypeEnum.MENU.equals(menuTypeEnum)) {
                        String routeName = StringUtils.capitalize(StrUtil.toCamelCase(menu.getPath(), '-')); // 路由 name 需要驼峰，首字母大写
                        routeVO.setName(routeName); //  根据name路由跳转 this.$router.push({name:xxx})
                    }
                    routeVO.setPath(menu.getPath()); // 根据path路由跳转 this.$router.push({path:xxx})
                    routeVO.setRedirect(menu.getRedirect());
                    routeVO.setComponent(menu.getComponent());

                    RouteVO.Meta meta = new RouteVO.Meta();
                    meta.setTitle(menu.getName());
                    meta.setIcon(menu.getIcon());
                    meta.setRoles(menu.getRoles());
                    meta.setHidden(StatusEnum.DISABLE.getValue().equals(menu.getVisible()));
                    meta.setKeepAlive(true);
                    routeVO.setMeta(meta);

                    List<RouteVO> children = recurRoutes(menu.getId(), menuList);
                    routeVO.setChildren(children);
                    return routeVO;
                }).collect(Collectors.toList());
    }

}
