package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.mapper.UserMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.PinYinUtil;
import com.cxygzl.biz.vo.UserBizVO;
import com.cxygzl.biz.vo.UserListQueryVO;
import com.cxygzl.biz.vo.third.UserDtoExtension;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.JsonUtil;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-05
 */
@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private IUserFieldDataService userFieldDataService;
    @Resource
    private IUserFieldService userFieldService;
    @Lazy
    @Resource
    private IRoleService roleService;
    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IMenuService menuService;



    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R password(User user) {

        this.lambdaUpdate().set(User::getPassword,user.getPassword()).eq(User::getId,user.getId()).update(new User());

        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 修改用户状态
     *
     * @param user
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R status(User user) {
        this.lambdaUpdate().set(User::getStatus,user.getStatus()).eq(User::getId,user.getId()).update(new User());

        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 获取当前用户详细信息
     *
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R getCurrentUserDetail() {
        String userId = StpUtil.getLoginIdAsString();

        //User user = this.getById(userId);
        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);



        UserDtoExtension userDtoExtension = BeanUtil.copyProperties(user, UserDtoExtension.class);


//        Set<String> roleKeySet = roleService.queryRoleKeyByUserId(userId).getData();
        Set<String> roleKeySet = roleService.list().stream().map(w->w.getKey()).collect(Collectors.toSet());;


        userDtoExtension.setRoles(roleKeySet);
        if(CollUtil.isNotEmpty(roleKeySet)){
            userDtoExtension.setPerms(menuService.listRolePerms(roleKeySet).getData());

        }else{
            userDtoExtension.setPerms(new HashSet<>());
        }


        return com.cxygzl.common.dto.R.success(userDtoExtension);
    }

    /**
     * 创建用户
     *
     * @param userBizVO
     * @return
     */
    @Override
    public Object createUser(UserBizVO userBizVO) {

        String phone = userBizVO.getPhone();
        Long count = this.lambdaQuery().eq(User::getPhone, phone).count();
        if (count > 0) {
            return com.cxygzl.common.dto.R.fail(StrUtil.format("手机号[{}]已注册", phone));
        }

        userBizVO.setNickName(userBizVO.getName());
        userBizVO.setPy(PinYinUtil.getFirstPinYin(userBizVO.getName()));
        userBizVO.setPinyin(PinYinUtil.getAllPinyin(userBizVO.getName()));
        this.save(userBizVO);

        //额外的字段
        Map<String, Object> fieldData = userBizVO.getFieldData();
        List<UserField> userFieldList = userFieldService.lambdaQuery().list();
        for (UserField userField : userFieldList) {
            String key = userField.getKey();
            String str = MapUtil.getStr(fieldData, key);
            if (StrUtil.isBlank(str)) {
                continue;
            }

            if (StrUtil.equals(userField.getType(), FormTypeEnum.MULTI_SELECT.getType())) {
                List list = MapUtil.get(fieldData, key, List.class);
                str = JsonUtil.toJSONString(list);
            }
            UserFieldData userFieldData = new UserFieldData();

            userFieldData.setUserId(userBizVO.getId());

            userFieldData.setData(str);
            userFieldData.setKey(key);
            userFieldDataService.save(userFieldData);

        }

        //保存角色
        List<Long> roleIds = userBizVO.getRoleIds();
        for (Long roleId : roleIds) {
            UserRole entity = new UserRole();

            entity.setUserId(userBizVO.getId());
            entity.setRoleId(roleId);

            userRoleService.save(entity);

        }

        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 修改用户
     *
     * @param userBizVO
     * @return
     */
    @Transactional
    @Override
    public R editUser(UserBizVO userBizVO) {


        String phone = userBizVO.getPhone();
        Long count = this.lambdaQuery().eq(User::getPhone, phone).ne(User::getId, userBizVO.getId()).count();
        if (count > 0) {
            return com.cxygzl.common.dto.R.fail(StrUtil.format("手机号[{}]已注册", phone));
        }

        userBizVO.setNickName(userBizVO.getName());
        userBizVO.setPy(PinYinUtil.getFirstPinYin(userBizVO.getName()));
        userBizVO.setPinyin(PinYinUtil.getAllPinyin(userBizVO.getName()));
        this.updateById(userBizVO);

        //删除所有的扩展字段
        LambdaQueryWrapper<UserFieldData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFieldData::getUserId, userBizVO.getId());
        userFieldDataService.remove(queryWrapper);

        //额外的字段
        Map<String, Object> fieldData = userBizVO.getFieldData();
        List<UserField> userFieldList = userFieldService.lambdaQuery().list();
        for (UserField userField : userFieldList) {
            String key = userField.getKey();
            String str = MapUtil.getStr(fieldData, key);
            if (StrUtil.isBlank(str)) {
                continue;
            }

            if (StrUtil.equals(userField.getType(), FormTypeEnum.MULTI_SELECT.getType())) {
                List list = MapUtil.get(fieldData, key, List.class);
                str = JsonUtil.toJSONString(list);
            }
            UserFieldData userFieldData = new UserFieldData();

            userFieldData.setUserId(userBizVO.getId());

            userFieldData.setData(str);
            userFieldData.setKey(key);
            userFieldDataService.save(userFieldData);

        }
        //先删除用户角色
        LambdaQueryWrapper<UserRole> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleService.remove(objectLambdaQueryWrapper.eq(UserRole::getUserId, userBizVO.getId()));
        //保存角色
        List<Long> roleIds = userBizVO.getRoleIds();
        for (Long roleId : roleIds) {
            UserRole entity = new UserRole();

            entity.setUserId(userBizVO.getId());
            entity.setRoleId(roleId);

            userRoleService.save(entity);

        }


        return com.cxygzl.common.dto.R.success();
    }


    /**
     * 用户管理 查询用户列表
     *
     * @param userListQueryVO
     * @return
     */
    @Override
    public Object queryList(UserListQueryVO userListQueryVO) {

        MPJLambdaWrapper<User> lambdaQueryWrapper = new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .selectAs(Dept::getName, UserBizVO::getDeptName)
                .leftJoin(Dept.class, Dept::getId, User::getDeptId)
                .like(StrUtil.isNotBlank(userListQueryVO.getName()), User::getName, userListQueryVO.getName())
                .in(CollUtil.isNotEmpty(userListQueryVO.getDepIdList()), User::getDeptId, userListQueryVO.getDepIdList())
                .eq(userListQueryVO.getDeptId()!=null,User::getDeptId,userListQueryVO.getDeptId())
                .eq(userListQueryVO.getStatus()!=null,User::getStatus,userListQueryVO.getStatus())
                .and(StrUtil.isNotBlank(userListQueryVO.getKeywords()),
                        k->k.like(User::getName,userListQueryVO.getKeywords()).or().like(User::getPhone,
                                userListQueryVO.getKeywords())
                        )
                .orderByDesc(User::getCreateTime);


        Page<UserBizVO> objectPage = this.selectJoinListPage(new Page<>(userListQueryVO.getPageNum(),
                userListQueryVO.getPageSize()), UserBizVO.class, lambdaQueryWrapper);


        return com.cxygzl.common.dto.R.success(objectPage);
    }

}
