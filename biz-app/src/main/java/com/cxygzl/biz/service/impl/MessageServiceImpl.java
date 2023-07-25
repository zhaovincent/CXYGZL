package com.cxygzl.biz.service.impl;

import com.cxygzl.biz.entity.Message;
import com.cxygzl.biz.mapper.MessageMapper;
import com.cxygzl.biz.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知消息 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-07-25
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
