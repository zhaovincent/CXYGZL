package com.cxygzl.biz.service;

import com.cxygzl.biz.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.MessageDto;

/**
 * <p>
 * 通知消息 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-07-25
 */
public interface IMessageService extends IService<Message> {
    /**
     * 查询未读数量
     * @return
     */
    R<Long> queryUnreadNum();

    /**
     * 保存消息
     * @param messageDto
     * @return
     */
    R saveMessage(MessageDto messageDto);

}
