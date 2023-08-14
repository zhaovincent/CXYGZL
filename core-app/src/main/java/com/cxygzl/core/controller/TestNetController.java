package com.cxygzl.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.RoleDto;
import com.cxygzl.common.dto.third.UserDto;
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

    private List<UserDto> userList = new ArrayList<>();
    private List<DeptDto> deptList = new ArrayList<>();
    private List<RoleDto> roleList = new ArrayList<>();
    private List<UserRole> userRoleList = new ArrayList<>();

    @PostConstruct
    public void init() {

        if (FileUtil.exist("/tmp/user.json")) {
            {
                String s = FileUtil.readUtf8String("/tmp/user.json");
                userList.addAll(JSON.parseArray(s, UserDto.class));
            }
            {
                String s = FileUtil.readUtf8String("/tmp/dept.json");
                deptList.addAll(JSON.parseArray(s, DeptDto.class));
            }
            {
                String s = FileUtil.readUtf8String("/tmp/role.json");
                roleList.addAll(JSON.parseArray(s, RoleDto.class));
            }
            {
                String s = FileUtil.readUtf8String("/tmp/userrole.json");
                userRoleList.addAll(JSON.parseArray(s, UserRole.class));
            }

            return;
        }

        {
            RoleDto d = RoleDto.builder().id("role1").name("角色1").status(1).build();
            roleList.add(d);
        }
        {
            RoleDto d = RoleDto.builder().id("role2").name("角色2").status(1).build();
            roleList.add(d);
        }
        {
            RoleDto d = RoleDto.builder().id("role3").name("角色3").status(1).build();
            roleList.add(d);
        }
        {
            DeptDto d = DeptDto.builder().id("dept1").name("部门1").parentId("0").leaderUserIdList(CollUtil.newArrayList("user1")).status(1).build();
            deptList.add(d);
        }
        {
            DeptDto d =
                    DeptDto.builder().id("dept2").name("部门2").parentId("dept1").leaderUserIdList(CollUtil.newArrayList("user11")).status(1).build();
            deptList.add(d);
        }
        {
            DeptDto d =
                    DeptDto.builder().id("dept3").name("部门3").parentId("dept1").leaderUserIdList(CollUtil.newArrayList("user15")).status(1).build();
            deptList.add(d);
        }
        {
            DeptDto d =
                    DeptDto.builder().id("dept4").name("部门4").parentId("dept2").leaderUserIdList(CollUtil.newArrayList("user21")).status(1).build();
            deptList.add(d);
        }
        {
            DeptDto d =
                    DeptDto.builder().id("dept5").name("部门5").parentId("dept3").leaderUserIdList(CollUtil.newArrayList("user45")).status(1).build();
            deptList.add(d);
        }
        for (long k = 1; k <= 100; k++) {
            long deptId = RandomUtil.randomLong(0, deptList.size()) + 1;
            long roleId = RandomUtil.randomLong(0, roleList.size()) + 1;
            {
                UserDto u =
                        UserDto.builder().id("user"+k).name("用户" + k).token(IdUtil.fastSimpleUUID()).avatarUrl("https" +
                                "://f" +
                                ".ittool.cc/pic/m" +
                                ".jpg").deptId("dept"+deptId).status(1).build();
                userList.add(u);
            }
            {
                UserRole userRole = UserRole.builder().roleId("role"+roleId).userId("user"+k).build();
                userRoleList.add(userRole);
            }
        }

        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(userList), "/tmp/user.json");
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(deptList), "/tmp/dept.json");
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(roleList), "/tmp/role.json");
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(userRoleList), "/tmp/userrole.json");

    }

    @PostMapping("loadUserIdListByRoleIdList")
    public List<String> loadUserIdListByRoleIdList(@RequestBody List<String> roleIdList) {
        return userRoleList.stream().filter(w -> roleIdList.contains(w.getRoleId())).map(w -> w.getUserId()).collect(Collectors.toList());
    }

    @PostMapping("loadUserIdListByDeptIdList")
    public List<String> loadUserIdListByDeptIdList(@RequestBody List<String> deptIdList) {
        return userList.stream().filter(w -> deptIdList.contains(w.getDeptId())).map(w -> w.getId()).collect(Collectors.toList());
    }


    @GetMapping("loadUserByDept")
    public List<UserDto> loadUserByDept(String deptId) {
        return userList.stream().filter(w -> deptId.equals(w.getDeptId())).collect(Collectors.toList());
    }
    @GetMapping("searchUser")
    public List<UserDto> searchUser(String name) {
        return userList.stream().filter(w -> w.getName().contains(name)).collect(Collectors.toList());
    }
    @GetMapping("getUser")
    public UserDto getUser(String userId) {
        return userList.stream().filter(w -> userId.equals(w.getId())).findFirst().get();
    }

    @GetMapping("getUserIdByToken")
    public String getUserIdByToken(String token) {
        return userList.stream().filter(w -> StrUtil.equals(w.getToken(),token)).findFirst().get().getId();
    }

    @GetMapping("loadAllRole")
    public List<RoleDto> loadAllRole() {
        return roleList;
    }

    @GetMapping("loadAllDept")
    public List<DeptDto> loadAllDept(String parentDeptId) {

        if (StrUtil.isBlank(parentDeptId)) {
            return deptList;
        }

        return deptList.stream().filter(w -> w.getParentId().equals(parentDeptId)).collect(Collectors.toList());
    }


    @Data
    @Builder
    private static class UserRole {
        private String userId;
        private String roleId;


    }

}
