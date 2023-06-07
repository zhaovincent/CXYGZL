package com.cxygzl.biz.vo;

import com.cxygzl.biz.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserVO extends User {

    private String deptName;

    private List<Long> depIdList;


}
