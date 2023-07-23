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
import com.cxygzl.biz.vo.UserVO;
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
    public Object getOrgTreeData(String deptId, String type, Boolean showLeave) {
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
                    .set("roleList",orgs)
                    .set("childDepartments",orgs)
                    .set("employees",new ArrayList<>());

            return R.success(dict);

        }

        Dict dict = Dict.create()
                .set("titleDepartments",new ArrayList<>())
                .set("roleList",new ArrayList<>())
                .set("employees",new ArrayList<>());


        List<DeptDto> deptList = ApiStrategyFactory.getStrategy().loadAllDept(deptId);

        //查询所有部门及员工
        {
            List deptVoList=new ArrayList();
            for (DeptDto dept : deptList) {
                OrgTreeVo orgTreeVo = new OrgTreeVo();
                orgTreeVo.setId(dept.getId());
                orgTreeVo.setName(dept.getName());
                orgTreeVo.setType(NodeUserTypeEnum.DEPT.getKey());
                orgTreeVo.setSelected(false);
                orgTreeVo.setStatus(dept.getStatus());
                deptVoList.add(orgTreeVo);
            }
            dict.set("childDepartments",deptVoList);
        }
        if (!StrUtil.equals(type, NodeUserTypeEnum.DEPT.getKey())) {

            List userVoList=new ArrayList();



            List<UserDto> userList = ApiStrategyFactory.getStrategy().loadUserByDept(String.valueOf(deptId));

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
            dict.set("employees",userVoList);
        }

        if(StrUtil.isNotBlank(deptId)){
            List<DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
            List<DeptDto> depts = DataUtil.selectParentByDept(deptId, allDept);
            dict.set("titleDepartments",CollUtil.reverse(depts));
        }

        return R.success(dict);
    }


    /**
     * 查询所有的组织架构 并树形显示
     *
     * @return
     */
    @Override
    public R getOrgTreeDataAll(String keywords, Integer status) {
        List<Dept> deptListDb = deptService.lambdaQuery()
                .eq(status!=null,Dept::getStatus,status)
                .like(StrUtil.isNotBlank(keywords),Dept::getName,keywords)
                .list();

        List<DeptDto> deptDtoList = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<DeptDto> deptList = deptDtoList.stream().filter(w -> status != null ? (w.getStatus().intValue() == status) : true)
                .filter(w -> StrUtil.isNotBlank(keywords) ? StrUtil.contains(w.getName(), keywords) : true).collect(Collectors.toList());


        if(StrUtil.isNotBlank(keywords)||status!=null){
            List list=new ArrayList();
            for (DeptDto dept : deptList) {
                String leader = dept.getLeaderUserId();
                UserDto user = ApiStrategyFactory.getStrategy().getUser(leader);

                Dept deptDb =
                        deptListDb.stream().filter(w -> StrUtil.equals(String.valueOf(w.getId()), dept.getId())).findAny().orElse(null);

                Dict set = Dict.create().set("leaderUserId", leader)
                        .set("leaderName", user.getName())
                        .set("leaderAvatar", user.getAvatarUrl())
                        .set("status", dept.getStatus())
                        .set("id", dept.getId())
                        .set("name", dept.getName())
                        .set("sort",deptDb==null?null: deptDb.getSort())
                        .set("roodIdList", CollUtil.reverse(DeptUtil.queryRootIdList(dept.getId(), deptList)));
                list.add(set);
            }
            return  com.cxygzl.common.dto.R.success(list);
        }

        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        for (DeptDto dept : deptList) {
            Dept deptDb =
                    deptListDb.stream().filter(w -> StrUtil.equals(String.valueOf(w.getId()), dept.getId())).findAny().orElse(null);


            TreeNode<String> treeNode = new TreeNode<>(dept.getId(), dept.getParentId(),
                    dept.getName(), 1);
            String leader = dept.getLeaderUserId();

            UserDto user = ApiStrategyFactory.getStrategy().getUser(leader);

            treeNode.setExtra(Dict.create().set("leaderUserId", leader)
                    .set("leaderName", user.getName())
                    .set("leaderAvatar", user.getAvatarUrl())

                    .set("status", dept.getStatus())
                    .set("sort",deptDb==null?null: deptDb.getSort())

                    .set("roodIdList", CollUtil.reverse(DeptUtil.queryRootIdList(dept.getId(), deptList)))
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

        return R.success(orgTreeVoList);
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
            return R.fail("当前部门下有用户，不能删除");
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
    public Object getUserDetail(long userId) {


        MPJLambdaWrapper<User> lambdaQueryWrapper=new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .selectAs(Dept::getName, UserVO::getDeptName)
                .leftJoin(Dept.class,Dept::getId,User::getDeptId)

                .eq(User::getId, userId)
                ;
        UserVO userVO = userService.selectJoinOne(UserVO.class, lambdaQueryWrapper);
        if(userVO!=null){
            List<DeptDto> deptDtoList = ApiStrategyFactory.getStrategy().loadAllDept(null);

            List<String> depIdRootList = DeptUtil.queryRootIdList(String.valueOf(userVO.getDeptId()), deptDtoList);

            userVO.setDepIdList(CollUtil.reverse(depIdRootList));

        }



        List<UserRole> userRoleList = userRoleService.queryListByUserId(userId).getData();
        userVO.setRoleIds(userRoleList.stream().map(w->w.getRoleId()).collect(Collectors.toList()));

        return R.success(userVO);
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

            R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryAssignTask(taskQueryParamDto);


            PageResultDto<TaskDto> pageResultDto = r.getData();

            Long total = pageResultDto.getTotal();
            if(total>0){
                return R.fail("当前用户仍有待办任务，不能离职");
            }

        }
        //判断是否是流程管理员
        {
            List<Process> processList = processService.lambdaQuery().eq(Process::getAdminId, user.getId()).list();
            if(!processList.isEmpty()){
                return R.fail(StrUtil.format("当前用户是流程[{}]的管理员，请先修改流程管理员之后才能离职",processList.stream().map(w->w.getName()).collect(Collectors.joining(","))));
            }
        }
        //判断是否是部门负责人
        {
            List<Dept> deptList = deptService.lambdaQuery().eq(Dept::getLeaderUserId, user.getId()).list();
            if(!deptList.isEmpty()){
                return R.fail(StrUtil.format("当前用户是部门[{}]的负责人，请先修改部门负责人之后才能离职",deptList.stream().map(w->w.getName()).collect(Collectors.joining(","))));
            }
        }



        userService.removeById(user.getId());

        return R.success();
    }
}
