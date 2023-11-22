package com.cxygzl.biz;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@MapperScan(basePackages = "com.cxygzl.biz.mapper")
@SpringBootApplication(scanBasePackages = {"com.cxygzl.biz"})
@EnableCaching
public class BizApp {
    public static void main(String[] args) {
        SpringApplication.run(BizApp.class, args);
        log.info("=====================Biz App  Start========================");
    }
}
