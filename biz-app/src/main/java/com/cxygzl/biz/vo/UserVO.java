package com.cxygzl.biz.vo;

import com.cxygzl.biz.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class UserVO extends User {

    private String deptName;

    private List<Long> depIdList;
    private Set<String> roles;
    private Set<String> perms;
    //扩展字段
    private List<UserFieldDataVo> userFieldDataList;

    private Map<String,Object> fieldData;

    private String verifyCode;

    private String verifyCodeKey;

    private List<Long> roleIds;


}
