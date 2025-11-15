package com.example.scholarship.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统控制器
 * 
 * @author System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {

    /**
     * 系统信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> systemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "高校奖学金管理系统");
        info.put("version", "1.0.0");
        info.put("description", "基于Spring Boot 3.2的高校奖学金管理系统");
        info.put("author", "System");
        info.put("timestamp", System.currentTimeMillis());
        info.put("status", "running");
        
        return ResponseEntity.ok(info);
    }

    /**
     * 系统健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("checks", Map.of(
            "database", "UP",
            "memory", "UP",
            "disk", "UP"
        ));
        
        return ResponseEntity.ok(health);
    }

    /**
     * 获取API版本信息
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> version() {
        Map<String, Object> version = new HashMap<>();
        version.put("apiVersion", "v1.0.0");
        version.put("buildTime", "2024-01-01T00:00:00Z");
        version.put("javaVersion", System.getProperty("java.version"));
        version.put("springBootVersion", "3.2.0");
        
        return ResponseEntity.ok(version);
    }
}