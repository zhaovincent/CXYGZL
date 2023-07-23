package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IOrgService;
import com.cxygzl.common.dto.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : willian fu
 * @date : 2022/6/27
 */
@RestController
@RequestMapping(value = {"org","api/org"})
public class OrgController {

    @Autowired
    private IOrgService orgService;









    /**
     * 查询组织架构树
     * @param deptId 部门id
     * @param showLeave 是否显示离职员工
     * @return 组织架构树数据
     */
    @GetMapping("tree")
    public R getOrgTreeData(@RequestParam(defaultValue ="0") String deptId,
                            String type,
                            @RequestParam(defaultValue = "false") Boolean showLeave){
        return orgService.getOrgTreeData(deptId, type, showLeave);
    }


    /**
     * 查询组织架构树
     * @return 组织架构树数据
     */
    @GetMapping("treeAll")
    public Object getOrgTreeDataAll(String keywords,Integer status){
        return orgService.getOrgTreeDataAll(keywords, status);
    }

    /**
     * 模糊搜索用户
     * @param userName 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    @GetMapping("tree/user/search")
    public Object getOrgTreeUser(@RequestParam String userName){
        return orgService.getOrgTreeUser(userName.trim());
    }


}
