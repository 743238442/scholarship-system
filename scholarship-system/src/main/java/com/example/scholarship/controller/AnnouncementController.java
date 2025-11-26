package com.example.scholarship.controller;

import com.example.scholarship.entity.Announcement;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.AnnouncementRepository;
import com.example.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 公告控制器
 *
 * @author System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有公告（公开，无需权限）
     */
    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        return ResponseEntity.ok(announcements);
    }

    /**
     * 创建新公告（仅ROLE_ADMIN可访问）
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        
        // 获取当前登录的管理员用户
        String username = authentication.getName();
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员用户信息不存在"));
        
        // 创建公告对象
        Announcement announcement = new Announcement();
        announcement.setTitle(requestBody.get("title"));
        announcement.setContent(requestBody.get("content"));
        announcement.setPublishedBy(adminUser.getId());
        announcement.setPublishedAt(LocalDateTime.now());
        
        // 保存公告
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        
        return ResponseEntity.ok(savedAnnouncement);
    }
}
