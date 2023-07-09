package com.cxygzl.core.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 15:27
 */
@RestController
@RequestMapping("test/net")
public class TestNetController {

    private List<User> userList = new ArrayList<>();
    private List<Dept> deptList = new ArrayList<>();
    private List<Role> roleList = new ArrayList<>();
    private List<UserRole> userRoleList = new ArrayList<>();

    @PostConstruct
    public void init() {

        if (FileUtil.exist("/tmp/user.json")) {
            {
                String s = FileUtil.readUtf8String("/tmp/user.json");
                userList.addAll(JSON.parseArray(s, User.class));
            }
            {
                String s = FileUtil.readUtf8String("/tmp/dept.json");
                deptList.addAll(JSON.parseArray(s, Dept.class));
            }
            {
                String s = FileUtil.readUtf8String("/tmp/role.json");
                roleList.addAll(JSON.parseArray(s, Role.class));
            }
            {
                String s = FileUtil.readUtf8String("/tmp/userrole.json");
                userRoleList.addAll(JSON.parseArray(s, UserRole.class));
            }

            return;
        }

        {
            Role d = Role.builder().id(1L).name("角色1").status(1).build();
            roleList.add(d);
        }
        {
            Role d = Role.builder().id(2L).name("角色2").status(1).build();
            roleList.add(d);
        }
        {
            Role d = Role.builder().id(3L).name("角色3").status(1).build();
            roleList.add(d);
        }
        {
            Dept d = Dept.builder().id(1L).name("部门1").parentId(0L).leaderUserId(1L).status(1).build();
            deptList.add(d);
        }
        {
            Dept d = Dept.builder().id(2L).name("部门2").parentId(1L).leaderUserId(11L).status(1).build();
            deptList.add(d);
        }
        {
            Dept d = Dept.builder().id(3L).name("部门3").parentId(1L).leaderUserId(15L).status(1).build();
            deptList.add(d);
        }
        {
            Dept d = Dept.builder().id(4L).name("部门4").parentId(2L).leaderUserId(21L).status(1).build();
            deptList.add(d);
        }
        {
            Dept d = Dept.builder().id(5L).name("部门5").parentId(3L).leaderUserId(45L).status(1).build();
            deptList.add(d);
        }
        for (long k = 1; k <= 100; k++) {
            long deptId = RandomUtil.randomLong(0, deptList.size()) + 1;
            long roleId = RandomUtil.randomLong(0, roleList.size()) + 1;
            {
                User u =
                        User.builder().id(k).name("用户" + k).token(IdUtil.fastSimpleUUID()).avatarUrl("https://f.ittool.cc/pic/m" +
                                ".jpg").deptId(deptId).status(1).build();
                userList.add(u);
            }
            {
                UserRole userRole = UserRole.builder().roleId(roleId).userId(k).build();
                userRoleList.add(userRole);
            }
        }

        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(userList), "/tmp/user.json");
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(deptList), "/tmp/dept.json");
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(roleList), "/tmp/role.json");
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(userRoleList), "/tmp/userrole.json");

    }

    @PostMapping("loadUserIdListByRoleIdList")
    public List<Long> loadUserIdListByRoleIdList(@RequestBody List<Long> roleIdList) {
        return userRoleList.stream().filter(w -> roleIdList.contains(w.getRoleId())).map(w -> w.getUserId()).collect(Collectors.toList());
    }

    @PostMapping("loadUserIdListByDeptIdList")
    public List<Long> loadUserIdListByDeptIdList(@RequestBody List<Long> deptIdList) {
        return userList.stream().filter(w -> deptIdList.contains(w.getDeptId())).map(w -> w.getId()).collect(Collectors.toList());
    }


    @GetMapping("loadUserByDept")
    public List<User> loadUserByDept(long deptId) {
        return userList.stream().filter(w -> deptId==(w.getDeptId())).collect(Collectors.toList());
    }
    @GetMapping("searchUser")
    public List<User> searchUser(String name) {
        return userList.stream().filter(w -> w.getName().contains(name)).collect(Collectors.toList());
    }
    @GetMapping("getUser")
    public User getUser(long userId) {
        return userList.stream().filter(w -> userId==(w.getId())).findFirst().get();
    }

    @GetMapping("getUserIdByToken")
    public Long getUserIdByToken(String token) {
        return userList.stream().filter(w -> StrUtil.equals(w.getToken(),token)).findFirst().get().getId();
    }

    @GetMapping("loadAllRole")
    public List<Role> loadAllRole() {
        return roleList;
    }

    @GetMapping("loadAllDept")
    public List<Dept> loadAllDept(Long parentDeptId) {

        if (parentDeptId == null) {
            return deptList;
        }

        return deptList.stream().filter(w -> w.getParentId().longValue() == parentDeptId).collect(Collectors.toList());
    }

    @Data
    @Builder
    private static class User {
        /**
         * 用户id 不能为空
         */
        private Long id;
        /**
         * 用户姓名 不能为空
         */
        private String name;
        /**
         * 用户头像 不能为空
         */
        private String avatarUrl;
        /**
         * 用户所属部门id 不能为空
         */
        private Long deptId;
        /**
         * 用户状态 0禁用 1启用
         */
        private Integer status;

        //用来测试登录用户的
        private String token;
    }

    @Data
    @Builder
    private static class UserRole {
        private Long userId;
        private Long roleId;


    }

    @Data
    @Builder
    private static class Role {
        /**
         * 角色id 不能为空
         */
        private Long id;
        /**
         * 角色名字 不能为空
         */
        private String name;
        /**
         * 角色key  例如admin:edit user:create等 不能为空
         */
        private String key;
        /**
         * 角色状态 0 禁用 1启用
         */
        private Integer status;
    }

    @Data
    @Builder
    private static class Dept {
        /**
         * 部门id 不能为空
         */
        private Long id;
        /**
         * 部门名字 不能为空
         */
        private String name;
        /**
         * 部门上级id 不能为空 若为顶级 则是0
         */
        private Long parentId;
        /**
         * 部门主管的userId 不能为空
         */
        private Long leaderUserId;
        /**
         * 部门状态 0 禁用 1启用
         */
        private Integer status;
    }
}
