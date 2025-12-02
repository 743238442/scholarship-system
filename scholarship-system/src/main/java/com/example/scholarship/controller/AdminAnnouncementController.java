package com.example.scholarship.controller;

import com.example.scholarship.entity.Announcement;
import com.example.scholarship.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
     * 管理员公告管理页面（发布公告）
     */
    @GetMapping("/announcements")
    public String announcementsPage(Model model, Principal principal) {
        // 获取当前管理员用户名
        String adminName = principal.getName();
        model.addAttribute("adminName", adminName);
        
        return "admin/announcements";
    }
    
    /**
     * 已发布公告列表页面
     */
    @GetMapping("/published-announcements")
    public String publishedAnnouncementsPage(Model model, Principal principal) {
        // 获取当前管理员用户名
        String adminName = principal.getName();
        model.addAttribute("adminName", adminName);
        
        // 获取所有未删除的公告，按发布时间降序排列
        Sort sort = Sort.by(Sort.Direction.DESC, "publishedAt");
        List<Announcement> announcements = announcementRepository.findByDeletedIsFalse(sort);
        
        // 转换公告列表，为模板提供正确的数据格式
        for (Announcement announcement : announcements) {
            // 这里可以添加必要的转换逻辑
            announcement.setPublishedBy(1L); // 临时设置为管理员ID 1
        }
        
        model.addAttribute("announcements", announcements);
        return "admin/published-announcements";
    }
    
    /**
     * 处理发布新公告的API请求
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/announcements")
    public ResponseEntity<?> publishAnnouncement(@RequestBody AnnouncementRequest request, Principal principal) {
        System.out.println("收到发布公告请求: " + request.getTitle());
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle(request.getTitle());
            announcement.setContent(request.getContent());
            announcement.setPublishedBy(1L); // 临时设置为管理员ID 1
            announcement.setPublishedAt(LocalDateTime.now());
            
            Announcement savedAnnouncement = announcementRepository.save(announcement);
            System.out.println("公告保存成功，ID: " + savedAnnouncement.getId());
            
            // 返回成功响应，便于前端进行跳转
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"公告发布成功\"}");
        } catch (Exception e) {
            System.err.println("公告发布失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"公告发布失败\"}");
        }
    }

    /**
     * 删除公告
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcements/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // 查找未删除的公告
            Announcement announcement = announcementRepository.findByIdAndDeletedIsFalse(id);
            if (announcement == null) {
                throw new IllegalArgumentException("公告不存在或已被删除: " + id);
            }
            
            // 执行软删除
            announcement.setDeleted(true);
            announcement.setDeletedAt(LocalDateTime.now());
            
            // 保存更新
            announcementRepository.save(announcement);
            
            // 添加成功消息
            redirectAttributes.addFlashAttribute("successMessage", "公告删除成功");
        } catch (Exception e) {
            System.err.println("删除公告失败: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "删除公告失败: " + e.getMessage());
        }
        
        // 重定向回已发布公告页面
        return "redirect:/admin/published-announcements";
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