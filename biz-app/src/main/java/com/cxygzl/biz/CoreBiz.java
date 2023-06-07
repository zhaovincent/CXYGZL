package com.cxygzl.biz;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan(basePackages = "com.cxygzl.biz.mapper")
@SpringBootApplication(scanBasePackages = "com.cxygzl.biz")
public class CoreBiz {
    public static void main(String[] args) {
        SpringApplication.run(CoreBiz.class, args);
        log.info("=====================Core Biz  Start========================");
    }
}
