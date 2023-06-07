package com.cxygzl.biz.controller;

import com.cxygzl.biz.entity.Dept;
import com.cxygzl.biz.service.IDeptService;
import com.cxygzl.biz.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = {"dept","api/dept"})
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @Resource
    private IOrgService orgService;








    /**
     * 创建部门
     * @param oaDepartments 部门对象
     * @return
     */
    @PostMapping("create")
    public Object create(@RequestBody Dept dept){
        return deptService.create(dept);
    }

    /**
     * 修改部门
     * @param dept 部门对象
     * @return
     */
    @PutMapping("update")
    public Object update(@RequestBody Dept dept){
        return deptService.updateDept(dept);
    }


    /**
     * 创建部门
     *
     * @param dept 部门对象
     * @return
     */
    @DeleteMapping("delete")
    public Object delete(@RequestBody Dept dept){
        return orgService.delete(dept);
    }


}
