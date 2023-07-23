package com.cxygzl.biz.vo.third;

import com.cxygzl.biz.vo.UserFieldDataVo;
import com.cxygzl.common.dto.third.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class UserVO extends UserDto {

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
