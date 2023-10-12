package com.cxygzl.core.config;

import cn.hutool.core.thread.ThreadUtil;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.vo.MqQueueObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-12 11:02
 */
@Component
@Slf4j
public class MqQueue {



    //队列
    private static final ArrayBlockingQueue<MqQueueObject> ARRAY_BLOCKING_QUEUE=new ArrayBlockingQueue(10000,true);

    @PostConstruct
    public void init(){
        log.info("初始化队列");
        ThreadUtil.execute(()->{
            try {
                while (true){
                    MqQueueObject take = ARRAY_BLOCKING_QUEUE.take();
                    log.info("开始处理队列数据:{}",JsonUtil.toJSONString(take));
                    BizHttpUtil.post(take.getObject(),take.getUrl());
                }
            }catch (Exception e){
                log.error("Error",e);
            }
        });
    }

    @SneakyThrows
    public static void put(Object o,String url){
        log.info("存入队列数据：{} {}", JsonUtil.toJSONString(o),url);
        MqQueueObject mqQueueObject=new MqQueueObject();
        mqQueueObject.setObject(o);
        mqQueueObject.setUrl(url);
        ARRAY_BLOCKING_QUEUE.put(mqQueueObject);

    }

}
