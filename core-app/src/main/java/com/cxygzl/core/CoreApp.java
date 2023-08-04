package com.cxygzl.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.cxygzl.core"})
public class CoreApp {
    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class, args);
        log.info("=====================Core APP  Start========================");
    }

}
