package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.constants.SecurityConstants;
import com.cxygzl.biz.constants.StatusEnum;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.mapper.UserMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.PinYinUtil;
import com.cxygzl.biz.vo.UserListQueryVO;
import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.dto.R;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 登录
     *
     * @param userVO
     * @return
     */
    @Override
    public Object login(UserVO userVO) {

        Object cacheVerifyCode =
                redisTemplate.opsForValue().get(SecurityConstants.VERIFY_CODE_CACHE_PREFIX + userVO.getVerifyCodeKey());
        if (cacheVerifyCode == null) {
            return R.fail("验证码错误");
        } else {
            // 验证码比对
            if (!StrUtil.equals(userVO.getVerifyCode(), Convert.toStr(cacheVerifyCode))) {
                return R.fail("验证码错误");

            }
        }

        String phone = userVO.getPhone();
        String password = userVO.getPassword();

        User u = this.lambdaQuery()
                .eq(User::getPhone, phone)
                .eq(User::getPassword, password)
                .eq(User::getStatus, StatusEnum.ENABLE.getValue())

                .one();
        if (u == null) {
            return R.fail("账号或者密码错误");
        }


        StpUtil.login(u.getId());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return R.success(tokenInfo);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public R logout() {
        StpUtil.logout();
        return R.success();
    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @Override
    public R password(User user) {

        this.lambdaUpdate().set(User::getPassword,user.getPassword()).eq(User::getId,user.getId()).update(new User());

        return R.success();
    }

    /**
     * 修改用户状态
     *
     * @param user
     * @return
     */
    @Override
    public R status(User user) {
        this.lambdaUpdate().set(User::getStatus,user.getStatus()).eq(User::getId,user.getId()).update(new User());

        return R.success();
    }

    /**
     * 获取当前用户详细信息
     *
     * @return
     */
    @Override
    public R getCurrentUserDetail() {
        long userId = StpUtil.getLoginIdAsLong();

        User user = this.getById(userId);

        user.setPassword(null);

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);


        Set<String> roleKeySet = roleService.queryRoleKeyByUserId(userId).getData();


        userVO.setRoles(roleKeySet);
        if(CollUtil.isNotEmpty(roleKeySet)){
            userVO.setPerms(menuService.listRolePerms(roleKeySet).getData());

        }else{
            userVO.setPerms(new HashSet<>());
        }


        return R.success(userVO);
    }

    /**
     * 创建用户
     *
     * @param userVO
     * @return
     */
    @Override
    public Object createUser(UserVO userVO) {

        String phone = userVO.getPhone();
        Long count = this.lambdaQuery().eq(User::getPhone, phone).count();
        if (count > 0) {
            return R.fail(StrUtil.format("手机号[{}]已注册", phone));
        }

        userVO.setNickName(userVO.getName());
        userVO.setPy(PinYinUtil.getFirstPinYin(userVO.getName()));
        userVO.setPinyin(PinYinUtil.getAllPinyin(userVO.getName()));
        this.save(userVO);

        //额外的字段
        Map<String, Object> fieldData = userVO.getFieldData();
        List<UserField> userFieldList = userFieldService.lambdaQuery().ge(UserField::getId, 0).list();
        for (UserField userField : userFieldList) {
            String key = userField.getKey();
            String str = MapUtil.getStr(fieldData, key);
            if (StrUtil.isBlank(str)) {
                continue;
            }

            if (StrUtil.equals(userField.getType(), "MultiSelect")) {
                List list = MapUtil.get(fieldData, key, List.class);
                str = JSON.toJSONString(list);
            }
            UserFieldData userFieldData = new UserFieldData();

            userFieldData.setUserId(userVO.getId());

            userFieldData.setData(str);
            userFieldData.setKey(key);
            userFieldDataService.save(userFieldData);

        }

        //保存角色
        List<Long> roleIds = userVO.getRoleIds();
        for (Long roleId : roleIds) {
            UserRole entity = new UserRole();

            entity.setUserId(userVO.getId());
            entity.setRoleId(roleId);

            userRoleService.save(entity);

        }

        return R.success();
    }

    /**
     * 修改用户
     *
     * @param userVO
     * @return
     */
    @Transactional
    @Override
    public Object editUser(UserVO userVO) {


        String phone = userVO.getPhone();
        Long count = this.lambdaQuery().eq(User::getPhone, phone).ne(User::getId, userVO.getId()).count();
        if (count > 0) {
            return R.fail(StrUtil.format("手机号[{}]已注册", phone));
        }

        userVO.setNickName(userVO.getName());
        userVO.setPy(PinYinUtil.getFirstPinYin(userVO.getName()));
        userVO.setPinyin(PinYinUtil.getAllPinyin(userVO.getName()));
        this.updateById(userVO);

        //删除所有的扩展字段
        LambdaQueryWrapper<UserFieldData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFieldData::getUserId, userVO.getId());
        userFieldDataService.remove(queryWrapper);

        //额外的字段
        Map<String, Object> fieldData = userVO.getFieldData();
        List<UserField> userFieldList = userFieldService.lambdaQuery().ge(UserField::getId, 0).list();
        for (UserField userField : userFieldList) {
            String key = userField.getKey();
            String str = MapUtil.getStr(fieldData, key);
            if (StrUtil.isBlank(str)) {
                continue;
            }

            if (StrUtil.equals(userField.getType(), "MultiSelect")) {
                List list = MapUtil.get(fieldData, key, List.class);
                str = JSON.toJSONString(list);
            }
            UserFieldData userFieldData = new UserFieldData();

            userFieldData.setUserId(userVO.getId());

            userFieldData.setData(str);
            userFieldData.setKey(key);
            userFieldDataService.save(userFieldData);

        }
        //先删除用户角色
        LambdaQueryWrapper<UserRole> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleService.remove(objectLambdaQueryWrapper.eq(UserRole::getUserId,userVO.getId()));
        //保存角色
        List<Long> roleIds = userVO.getRoleIds();
        for (Long roleId : roleIds) {
            UserRole entity = new UserRole();

            entity.setUserId(userVO.getId());
            entity.setRoleId(roleId);

            userRoleService.save(entity);

        }


        return R.success();
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
                .selectAs(Dept::getName, UserVO::getDeptName)
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


        Page<UserVO> objectPage = this.selectJoinListPage(new Page<>(userListQueryVO.getPageNum(),
                userListQueryVO.getPageSize()), UserVO.class, lambdaQueryWrapper);


        return R.success(objectPage);
    }

}
