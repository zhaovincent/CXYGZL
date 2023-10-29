package com.cxygzl.biz.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.*;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.DataUtil;
import com.cxygzl.biz.utils.DeptUtil;
import com.cxygzl.biz.vo.OrgTreeVo;
import com.cxygzl.biz.vo.UserFieldDataVo;
import com.cxygzl.biz.vo.UserBizVO;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.PageResultDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.TaskDto;
import com.cxygzl.common.dto.TaskQueryParamDto;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.RoleDto;
import com.cxygzl.common.dto.third.UserDto;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrgServiceImpl implements IOrgService {


    @Resource
    private IUserService userService;
    @Resource
    private IDeptService deptService;

    @Resource
    private IUserRoleService userRoleService;


    @Resource
    private IUserFieldDataService userFieldDataService;
    @Resource
    private IUserFieldService userFieldService;

    @Resource
    private IProcessService processService;

    /**
     * 查询组织架构树
     *
     * @param deptId    部门id
     * @param type      只查询部门架构
     * @param showLeave 是否显示离职员工
     * @return 组织架构树数据
     */
    @Override
    public R getOrgTreeData(String deptId, String type, Boolean showLeave) {
        List<OrgTreeVo> orgs = new LinkedList<>();

        if (StrUtil.equals(type, NodeUserTypeEnum.ROLE.getKey())) {
            //角色

            List<RoleDto> roleList = ApiStrategyFactory.getStrategy().loadAllRole();


            for (RoleDto role : roleList) {
                OrgTreeVo orgTreeVo = new OrgTreeVo();
                orgTreeVo.setId(role.getId());
                orgTreeVo.setName(role.getName());
                orgTreeVo.setStatus(role.getStatus());
                orgTreeVo.setType(NodeUserTypeEnum.ROLE.getKey());
                orgTreeVo.setSelected(false);
                orgs.add(orgTreeVo);
            }

            Dict dict = Dict.create()
                    .set("roleList", orgs)
                    .set("childDepartments", orgs)
                    .set("employees", new ArrayList<>());

            return com.cxygzl.common.dto.R.success(dict);

        }

        Dict dict = Dict.create()
                .set("titleDepartments", new ArrayList<>())
                .set("roleList", new ArrayList<>())
                .set("employees", new ArrayList<>());


        List<DeptDto> deptList = ApiStrategyFactory.getStrategy().loadAllDept(deptId);

        //查询所有部门及员工
        {
            List deptVoList = new ArrayList();
            for (DeptDto dept : deptList) {
                OrgTreeVo orgTreeVo = new OrgTreeVo();
                orgTreeVo.setId(dept.getId());
                orgTreeVo.setName(dept.getName());
                orgTreeVo.setType(NodeUserTypeEnum.DEPT.getKey());
                orgTreeVo.setSelected(false);
                orgTreeVo.setStatus(dept.getStatus());
                deptVoList.add(orgTreeVo);
            }
            dict.set("childDepartments", deptVoList);
        }
        if (!StrUtil.equals(type, NodeUserTypeEnum.DEPT.getKey())) {

            List userVoList = new ArrayList();


            List<UserDto> userList = ApiStrategyFactory.getStrategy().loadUserByDept((deptId));

            for (UserDto user : userList) {
                OrgTreeVo orgTreeVo = new OrgTreeVo();
                orgTreeVo.setId(user.getId());
                orgTreeVo.setName(user.getName());
                orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
                orgTreeVo.setSelected(false);
                orgTreeVo.setStatus(user.getStatus());
                orgTreeVo.setAvatar(user.getAvatarUrl());
                userVoList.add(orgTreeVo);

            }
            dict.set("employees", userVoList);
        }

        if (StrUtil.isNotBlank(deptId)) {
            List<DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
            List<DeptDto> depts = DataUtil.selectParentByDept(deptId, allDept);
            dict.set("titleDepartments", CollUtil.reverse(depts));
        }

        return com.cxygzl.common.dto.R.success(dict);
    }


    /**
     * 查询所有的组织架构 并树形显示
     *
     * @return
     */
    @Override
    public com.cxygzl.common.dto.R getOrgTreeDataAll(String keywords, Integer status) {

        List<Dept> deptListDb = deptService.lambdaQuery()
                .eq(status != null, Dept::getStatus, status)
                .like(StrUtil.isNotBlank(keywords), Dept::getName, keywords)
                .list();


        if (StrUtil.isNotBlank(keywords) || status != null) {
            List list = new ArrayList();
            for (Dept dept : deptListDb) {

                User user = userService.getById(dept.getLeaderUserId());


                Dict set = Dict.create().set("leaderUserId", dept.getLeaderUserId())
                        .set("leaderName", user.getName())
                        .set("leaderAvatar", user.getAvatarUrl())
                        .set("status", dept.getStatus())
                        .set("id", String.valueOf(dept.getId()))
                        .set("name", dept.getName())
                        .set("sort", dept == null ? null : dept.getSort())
                        .set("roodIdList", CollUtil.reverse(DeptUtil.queryRootIdList(String.valueOf(dept.getId()),
                                BeanUtil.copyToList(deptListDb, DeptDto.class))));
                list.add(set);
            }
            return com.cxygzl.common.dto.R.success(list);
        }

        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        for (Dept dept : deptListDb) {


            TreeNode<String> treeNode = new TreeNode<>(String.valueOf(dept.getId()), String.valueOf(dept.getParentId()),
                    dept.getName(), 1);
            Long leader = dept.getLeaderUserId();

            User user = userService.getById(dept.getLeaderUserId());


            treeNode.setExtra(Dict.create().set("leaderUserId", leader)
                    .set("leaderName", user.getName())
                    .set("leaderAvatar", user.getAvatarUrl())

                    .set("status", dept.getStatus())
                    .set("sort", dept.getSort())
                    .set("roodIdList", CollUtil.reverse(DeptUtil.queryRootIdList(String.valueOf(dept.getId()),
                            BeanUtil.copyToList(deptListDb, DeptDto.class))))
            );
            nodeList.add(treeNode);

        }
        // 0表示最顶层的id是0
        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");

        return com.cxygzl.common.dto.R.success(treeList);
    }


    /**
     * 模糊搜索用户
     *
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    @Override
    public Object getOrgTreeUser(String userName) {

        List<UserDto> userList = ApiStrategyFactory.getStrategy().searchUser(userName);

        List<OrgTreeVo> orgTreeVoList = new ArrayList<>();

        for (UserDto user : userList) {
            OrgTreeVo orgTreeVo = new OrgTreeVo();
            orgTreeVo.setId(user.getId());
            orgTreeVo.setName(user.getName());
            orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
            orgTreeVo.setAvatar(user.getAvatarUrl());

            orgTreeVo.setStatus(user.getStatus());
            orgTreeVoList.add(orgTreeVo);

        }

        return com.cxygzl.common.dto.R.success(orgTreeVoList);
    }


    /**
     * 删除部门
     *
     * @param dept
     * @return
     */
    @Override
    public Object delete(Dept dept) {
        long id = dept.getId();

        List<DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<DeptDto> deptList = DataUtil.selectChildrenByDept(String.valueOf(id), allDept);


        Set<String> depIdSet = deptList.stream().map(w -> w.getId()).collect(Collectors.toSet());

        Long count = userService.lambdaQuery().in(User::getDeptId, depIdSet).count();


        if (count > 0) {
            return com.cxygzl.common.dto.R.fail("当前部门下有用户，不能删除");
        }

        deptService.removeById(id);
        return R.success();
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public R getUserDetail(long userId) {


        MPJLambdaWrapper<User> lambdaQueryWrapper = new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .selectAs(Dept::getName, UserBizVO::getDeptName)
                .leftJoin(Dept.class, Dept::getId, User::getDeptId)

                .eq(User::getId, userId);
        UserBizVO userBizVO = userService.selectJoinOne(UserBizVO.class, lambdaQueryWrapper);
        if (userBizVO != null) {
            List<DeptDto> deptDtoList = ApiStrategyFactory.getStrategy().loadAllDept(null);

            List<String> depIdRootList = DeptUtil.queryRootIdList(String.valueOf(userBizVO.getDeptId()), deptDtoList);

            userBizVO.setDepIdList(CollUtil.reverse(depIdRootList));

        }
        //添加扩展字段
        List<UserFieldData> userFieldDataList = userFieldDataService.lambdaQuery().eq(UserFieldData::getUserId, userId).list();
        //判断有没有新的扩展字段
        List<UserField> userFieldList = userFieldService.lambdaQuery().list();
        List<UserFieldDataVo> userFieldDataVos = BeanUtil.copyToList(userFieldList, UserFieldDataVo.class);
        for (UserFieldDataVo userFieldDataVo : userFieldDataVos) {
            UserFieldData userFieldData = userFieldDataList.stream().filter(w -> StrUtil.equals(w.getKey(), userFieldDataVo.getKey())).findAny().orElse(null);
            if (userFieldData != null) {
                userFieldDataVo.setData(userFieldData.getData());
            }
        }


        userBizVO.setUserFieldDataList(userFieldDataVos);
        List<UserRole> userRoleList = userRoleService.queryListByUserId(String.valueOf(userId)).getData();
        userBizVO.setRoleIds(userRoleList.stream().map(w -> w.getRoleId()).collect(Collectors.toList()));

        return com.cxygzl.common.dto.R.success(userBizVO);
    }

    /**
     * 用户离职
     *
     * @param user
     * @return
     */
    @Override
    public Object delete(User user) {

        //判断是否有待办任务
        {
            TaskQueryParamDto taskQueryParamDto = new TaskQueryParamDto();
            taskQueryParamDto.setPageNum(1);
            taskQueryParamDto.setPageSize(1);
            taskQueryParamDto.setAssign(String.valueOf(user.getId()));

            R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryTodoTask(taskQueryParamDto);


            PageResultDto<TaskDto> pageResultDto = r.getData();

            Long total = pageResultDto.getTotal();
            if (total > 0) {
                return com.cxygzl.common.dto.R.fail("当前用户仍有待办任务，不能离职");
            }

        }
        //判断是否是流程管理员
        {
            List<com.cxygzl.biz.entity.Process> processList = processService.lambdaQuery().eq(Process::getAdminId, user.getId()).list();
            if (!processList.isEmpty()) {
                return com.cxygzl.common.dto.R.fail(StrUtil.format("当前用户是流程[{}]的管理员，请先修改流程管理员之后才能离职", processList.stream().map(w -> w.getName()).collect(Collectors.joining(","))));
            }
        }
        //判断是否是部门负责人
        {
            List<Dept> deptList = deptService.lambdaQuery().eq(Dept::getLeaderUserId, user.getId()).list();
            if (!deptList.isEmpty()) {
                return com.cxygzl.common.dto.R.fail(StrUtil.format("当前用户是部门[{}]的负责人，请先修改部门负责人之后才能离职", deptList.stream().map(w -> w.getName()).collect(Collectors.joining(","))));
            }
        }


        userService.removeById(user.getId());

        return com.cxygzl.common.dto.R.success();
    }
}
