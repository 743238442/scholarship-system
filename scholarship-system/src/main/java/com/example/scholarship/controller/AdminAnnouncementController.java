package com.example.scholarship.controller;

import com.example.scholarship.entity.Announcement;
import com.example.scholarship.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Sort;

/**
 * 管理员公告管理控制器
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    /**
     * 管理员公告管理页面
     */
    @GetMapping("/announcements")
    public String announcementsPage(Model model, Principal principal) {
        // 获取当前管理员用户名
        String adminName = principal.getName();
        model.addAttribute("adminName", adminName);
        
        // 获取所有公告，按发布时间降序排列
        Sort sort = Sort.by(Sort.Direction.DESC, "publishedAt");
        List<Announcement> announcements = announcementRepository.findAll(sort);
        
        // 转换公告列表，为模板提供正确的数据格式
        for (Announcement announcement : announcements) {
            // 这里可以添加必要的转换逻辑
            announcement.setPublishedBy(1L); // 临时设置为管理员ID 1
        }
        
        model.addAttribute("announcements", announcements);
        return "admin/announcements";
    }
    
    /**
     * 处理发布新公告的API请求
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/announcements")
    public ResponseEntity<?> publishAnnouncement(@RequestBody AnnouncementRequest request, Principal principal) {
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle(request.getTitle());
            announcement.setContent(request.getContent());
            announcement.setPublishedBy(1L); // 临时设置为管理员ID 1
            announcement.setPublishedAt(LocalDateTime.now());
            
            announcementRepository.save(announcement);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 公告请求DTO
     */
    static class AnnouncementRequest {
        private String title;
        private String content;
        
        // Getters
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}