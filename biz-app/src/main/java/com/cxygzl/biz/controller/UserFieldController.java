package com.cxygzl.biz.controller;

import com.cxygzl.biz.service.IUserFieldService;
import com.cxygzl.biz.vo.UserFieldParamVO;
import com.cxygzl.common.dto.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户字段 前端控制器
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-17
 */
@RestController
@RequestMapping(value = {"userField","api/userField"})
public class UserFieldController {
    @Resource
    private IUserFieldService userFieldService;


    /**
     * 保存变量
     * @param userFieldParamVOList
     * @return
     */
    @PostMapping("save")
    public R save(@RequestBody List<UserFieldParamVO> userFieldParamVOList){
        return userFieldService.save(userFieldParamVOList);
    }


    /**
     * 查询用户属性字段
     * @return
     */
    @GetMapping("queryAll")
    public R queryAll(){
        return userFieldService.queryAll();
    }
}
