package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Message;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.mapper.MessageMapper;
import com.cxygzl.biz.service.IMessageService;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.common.constants.MessageTypeEnum;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.third.MessageDto;
import com.cxygzl.common.dto.third.UserDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    /**
     * 查询未读数量
     *
     * @return
     */
    @Override
    public R<Long> queryUnreadNum() {
        String userId = StpUtil.getLoginIdAsString();
        Long num = this.lambdaQuery().eq(Message::getReaded,false).eq(Message::getUserId, userId).count();
        return R.success(num);
    }

    /**
     * 保存消息
     *
     * @param messageDto
     * @return
     */
    @Override
    public R saveMessage(MessageDto messageDto) {
        Message message = BeanUtil.copyProperties(messageDto, Message.class);
        if(StrUtil.equals(message.getType(), MessageTypeEnum.TODO_TASK.getType())){
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, message.getProcessInstanceId()).one();

            String userId = processInstanceRecord.getUserId();
            UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);
            //待办
            message.setTitle("您有一条待办任务");
            message.setContent(StrUtil.format("[\uD83D\uDE38{}]提交的任务[\uD83D\uDC8C{}]需要您来处理，请及时查看",user.getName(),
                    processInstanceRecord.getName()));
        }
        this.save(message);
        return R.success();
    }

    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryList(com.cxygzl.common.dto.MessageDto pageDto) {
        Page<Message> messagePage = this.lambdaQuery()
                .eq(Message::getUserId, StpUtil.getLoginIdAsString())
                .eq(pageDto.getReaded()!=null,Message::getReaded,pageDto.getReaded())
                .orderByDesc(Message::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));
        return R.success(messagePage);
    }

    /**
     * 删除消息
     *
     * @param messageDto
     * @return
     */
    @Override
    public R delete(com.cxygzl.common.dto.MessageDto messageDto) {
        this.removeById(messageDto.getId());
        return R.success();
    }

    /**
     * 置为已读
     *
     * @param messageDto
     * @return
     */
    @Override
    public R read(com.cxygzl.common.dto.MessageDto messageDto) {
        this.lambdaUpdate().set(Message::getReaded,true)
                .eq(Message::getId,messageDto.getId())
                .eq(Message::getReaded,false)
                .update(new Message());
        return R.success();
    }
}
