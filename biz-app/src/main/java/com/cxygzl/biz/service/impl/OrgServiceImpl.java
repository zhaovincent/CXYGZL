package com.cxygzl.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.User;
import com.cxygzl.biz.mapper.DeptMapper;
import com.cxygzl.biz.service.*;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.utils.DeptUtil;
import com.cxygzl.biz.utils.R;
import com.cxygzl.biz.vo.OrgTreeVo;
import com.cxygzl.biz.vo.UserVO;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.dto.PageResultDto;
import com.cxygzl.common.dto.TaskDto;
import com.cxygzl.common.dto.TaskQueryParamDto;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrgServiceImpl implements IOrgService {


    @Resource
    private IUserService userService;
    @Resource
    private IDeptService deptService;
    @Resource
    private DeptMapper deptMapper;



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
    public Object getOrgTreeData(Integer deptId, String type, Boolean showLeave) {

        List<Dept> deptList = deptService.lambdaQuery()
                .eq(deptId != null, Dept::getParentId, deptId)
                .list();

        //查询所有部门及员工
        List<OrgTreeVo> orgs = new LinkedList<>();
        {
            for (Dept dept : deptList) {
                OrgTreeVo orgTreeVo = new OrgTreeVo();
                orgTreeVo.setId(dept.getId());
                orgTreeVo.setName(dept.getName());
                orgTreeVo.setType(NodeUserTypeEnum.DEPT.getKey());
                orgTreeVo.setSelected(false);
                orgs.add(orgTreeVo);
            }
        }
        if (!StrUtil.equals(type, NodeUserTypeEnum.DEPT.getKey())) {

            List<User> userList = userService.lambdaQuery()
                    .eq(User::getDepId, deptId)
                    .isNull(!(showLeave != null && showLeave), User::getLeaveDate)
                    .list();
            for (User user : userList) {
                OrgTreeVo orgTreeVo = new OrgTreeVo();
                orgTreeVo.setId(user.getId());
                orgTreeVo.setName(user.getName());
                orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
                orgTreeVo.setSelected(false);
                orgTreeVo.setAvatar(user.getAvatarUrl());
                orgs.add(orgTreeVo);

            }
        }
        return R.ok(orgs);
    }


    /**
     * 查询所有的组织架构 并树形显示
     *
     * @return
     */
    @Override
    public Object getOrgTreeDataAll() {

        List<Dept> deptList = deptService.lambdaQuery().list();

        List<TreeNode<Long>> nodeList = CollUtil.newArrayList();

        for (Dept dept : deptList) {

            TreeNode<Long> treeNode = new TreeNode<>(dept.getId(), dept.getParentId(),
                    dept.getName(), 1);
            Long leader = dept.getLeaderUserId();

            User user = userService.getById(leader);

            treeNode.setExtra(Dict.create().set("leaderId", leader)
                    .set("leaderName", user.getName())
                    .set("roodIdList", CollUtil.reverse(DeptUtil.queryRootIdList(dept.getId(), deptList)))
            );
            nodeList.add(treeNode);

        }
        // 0表示最顶层的id是0
        List<Tree<Long>> treeList = TreeUtil.build(nodeList, 0L);

        return treeList;
    }


    /**
     * 模糊搜索用户
     *
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    @Override
    public Object getOrgTreeUser(String userName) {

        List<User> userList = userService.lambdaQuery().isNull(User::getLeaveDate).and(k ->
                k.like(User::getPinyin, userName)
                        .or(w -> w.like(User::getPy, userName))
                        .or(w -> w.like(User::getName, userName))
        ).list();

        List<OrgTreeVo> orgTreeVoList = new ArrayList<>();

        for (User user : userList) {
            OrgTreeVo orgTreeVo = new OrgTreeVo();
            orgTreeVo.setId(user.getId());
            orgTreeVo.setName(user.getName());
            orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
            orgTreeVo.setAvatar(user.getAvatarUrl());

            orgTreeVoList.add(orgTreeVo);

        }

        return R.ok(orgTreeVoList);
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


        List<Dept> deptList = deptMapper.selectChildrenByDept(id);
        Set<Long> depIdSet = deptList.stream().map(w -> w.getId()).collect(Collectors.toSet());

        Long count = userService.lambdaQuery().in(User::getDepId, depIdSet).count();


        if (count > 0) {
            return R.badRequest("当前部门下有用户，不能删除");
        }

        deptService.removeById(id);
        return R.ok("删除成功");
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
                .leftJoin(Dept.class,Dept::getId,User::getDepId)

                .eq(User::getId, userId)
                ;
        UserVO userVO = userService.selectJoinOne(UserVO.class, lambdaQueryWrapper);
        if(userVO!=null){
            List<Dept> deptList = deptService.lambdaQuery().list();

            List<Long> depIdRootList = DeptUtil.queryRootIdList(userVO.getDepId(), deptList);

            userVO.setDepIdList(CollUtil.reverse(depIdRootList));

        }


        return R.ok(userVO);
    }

    /**
     * 用户离职
     *
     * @param user
     * @return
     */
    @Override
    public Object leave(User user) {

        //判断是否有待办任务
        {
            TaskQueryParamDto taskQueryParamDto = new TaskQueryParamDto();
            taskQueryParamDto.setPage(1);
            taskQueryParamDto.setCount(1);
            taskQueryParamDto.setAssign(String.valueOf(user.getId()));

            String post = CoreHttpUtil.queryAssignTask(taskQueryParamDto);

            com.cxygzl.common.dto.R<PageResultDto<TaskDto>> r = JSON.parseObject(post, new TypeReference<com.cxygzl.common.dto.R<PageResultDto<TaskDto>>>() {
            });
            PageResultDto<TaskDto> pageResultDto = r.getData();

            Long total = pageResultDto.getTotal();
            if(total>0){
                return R.badRequest("当前用户仍有待办任务，不能离职");
            }

        }
        //判断是否是流程管理员
        {
            List<com.cxygzl.biz.entity.Process> processList = processService.lambdaQuery().eq(Process::getAdminId, user.getId()).list();
            if(!processList.isEmpty()){
                return R.badRequest(StrUtil.format("当前用户是流程[{}]的管理员，请先修改流程管理员之后才能离职",processList.stream().map(w->w.getFormName()).collect(Collectors.joining(","))));
            }
        }
        //判断是否是部门负责人
        {
            List<Dept> deptList = deptService.lambdaQuery().eq(Dept::getLeaderUserId, user.getId()).list();
            if(!deptList.isEmpty()){
                return R.badRequest(StrUtil.format("当前用户是部门[{}]的负责人，请先修改部门负责人之后才能离职",deptList.stream().map(w->w.getName()).collect(Collectors.joining(","))));
            }
        }



        userService.lambdaUpdate().set(User::getLeaveDate,new Date()).eq(User::getId,user.getId()).update(new User());

        return R.ok("离职成功");
    }
}
