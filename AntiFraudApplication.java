package com.antifraud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.antifraud.mapper")
public class AntiFraudApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntiFraudApplication.class, args);
        System.out.println("=========================================");
        System.out.println("  老年人数字反诈教育平台 启动成功!");
        System.out.println("=========================================");
    }
}
