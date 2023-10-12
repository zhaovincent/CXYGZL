package com.cxygzl.core.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cxygzl.common.utils.JsonUtil;
import com.cxygzl.core.utils.BizHttpUtil;
import com.cxygzl.core.vo.MqQueueObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
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
    @SneakyThrows
    public void init(){
        log.info("初始化数据");
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        Map<Object, Object> cxygzlQueue = redisTemplate.opsForHash().entries("cxygzl_queue");
        for (Map.Entry<Object, Object> entry : cxygzlQueue.entrySet()) {
            MqQueueObject mqQueueObject = BeanUtil.toBean(entry.getValue(), MqQueueObject.class);
            ARRAY_BLOCKING_QUEUE.put(mqQueueObject);
        }
        log.info("初始化队列数据结束，总共：{}",cxygzlQueue.size());
        ThreadUtil.execute(()->{
            try {
                while (true){
                    MqQueueObject take = ARRAY_BLOCKING_QUEUE.take();
                    log.info("开始处理队列数据:{}",JsonUtil.toJSONString(take));
                    BizHttpUtil.post(take.getObject(),take.getUrl());

                    redisTemplate.opsForHash().delete("cxygzl_queue",take.getKey());
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
        String key = IdUtil.fastSimpleUUID();
        mqQueueObject.setKey(key);
        ARRAY_BLOCKING_QUEUE.put(mqQueueObject);

        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        redisTemplate.opsForHash().put("cxygzl_queue",key,mqQueueObject);
    }

}
