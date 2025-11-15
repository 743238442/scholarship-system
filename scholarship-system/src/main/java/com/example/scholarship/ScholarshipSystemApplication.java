package com.example.scholarship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 高校奖学金管理系统主启动类
 * 
 * @author System
 * @version 1.0.0
 */
@SpringBootApplication
public class ScholarshipSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScholarshipSystemApplication.class, args);
        System.out.println("高校奖学金管理系统启动成功！");
    }
}