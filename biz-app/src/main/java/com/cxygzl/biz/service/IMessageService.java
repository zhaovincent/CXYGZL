package com.cxygzl.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxygzl.biz.entity.Message;
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

    /**
     * 查询列表
     * @param pageDto
     * @return
     */
    R queryList(com.cxygzl.common.dto.MessageDto pageDto);

    /**
     * 删除消息
     * @param messageDto
     * @return
     */
    R delete(com.cxygzl.common.dto.MessageDto messageDto );

    /**
     * 置为已读
     * @param messageDto
     * @return
     */
    R read(com.cxygzl.common.dto.MessageDto messageDto );

}
