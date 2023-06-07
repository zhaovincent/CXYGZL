package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.mapper.UserMapper;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.service.IUserService;
import com.cxygzl.biz.utils.PinYinUtil;
import com.cxygzl.biz.utils.R;
import com.cxygzl.biz.vo.UserListQueryVO;
import com.cxygzl.biz.vo.UserVO;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
    private IProcessService processService;


    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Override
    public Object login(User user) {
        String phone = user.getPhone();
        String password = user.getPassword();

        User u = this.lambdaQuery()
                .eq(User::getPhone, phone)
                .eq(User::getPassword, password)
                .isNull(User::getLeaveDate).one();
        if (u == null) {
            return R.badRequest("账号或者密码错误");
        }

        StpUtil.login(u.getId());

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        return R.ok(tokenInfo);
    }

    /**
     * 获取当前用户详细信息
     *
     * @return
     */
    @Override
    public Object getCurrentUserDetail() {
        long userId = StpUtil.getLoginIdAsLong();

        User user = this.getById(userId);

        user.setPassword(null);

        return R.ok(user);
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
        if(count>0){
            return R.badRequest(StrUtil.format("手机号[{}]已注册",phone));
        }

        userVO.setNickName(userVO.getName());
        userVO.setPy(PinYinUtil.getFirstPinYin(userVO.getName()));
        userVO.setPinyin(PinYinUtil.getAllPinyin(userVO.getName()));
        this.save(userVO);



        return R.ok("添加成功");
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
        if(count>0){
            return R.badRequest(StrUtil.format("手机号[{}]已注册",phone));
        }

        userVO.setNickName(userVO.getName());
        userVO.setPy(PinYinUtil.getFirstPinYin(userVO.getName()));
        userVO.setPinyin(PinYinUtil.getAllPinyin(userVO.getName()));
        this.updateById(userVO);




        return R.ok("修改成功");
    }




    /**
     * 用户管理 查询用户列表
     * @param userListQueryVO
     * @return
     */
    @Override
    public Object queryUserList(UserListQueryVO userListQueryVO) {

        MPJLambdaWrapper<User> lambdaQueryWrapper=new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .selectAs(Dept::getName,UserVO::getDeptName)
                .leftJoin(Dept.class,Dept::getId,User::getDepId)
                .like(StrUtil.isNotBlank(userListQueryVO.getName()),User::getName,userListQueryVO.getName())
                .isNull(userListQueryVO.getStatus()==1,User::getLeaveDate)
                .isNotNull(userListQueryVO.getStatus()==2,User::getLeaveDate)
                .in(CollUtil.isNotEmpty(userListQueryVO.getDepIdList()),User::getDepId,userListQueryVO.getDepIdList())
                .orderByDesc(User::getCreateTime)
                ;



        Page<UserVO> objectPage = this.selectJoinListPage(new Page<>(userListQueryVO.getPage(), userListQueryVO.getCount()), UserVO.class, lambdaQueryWrapper);


        return R.ok(objectPage);
    }

}
