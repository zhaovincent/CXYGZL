package com.cxygzl.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.WeixinUser;
import com.cxygzl.biz.mapper.WeixinUserMapper;
import com.cxygzl.biz.service.IWeixinUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeixinUserServiceImpl extends ServiceImpl<WeixinUserMapper,WeixinUser> implements IWeixinUserService {
}
