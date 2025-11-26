package com.example.controller;

import com.example.entity.Announcement;
import com.example.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByPublishedAtDesc();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createAnnouncement(@RequestBody Map<String, String> requestBody, Authentication authentication) {
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        
        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            return ResponseEntity.badRequest().body("标题和内容不能为空");
        }
        
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setPublishedBy(authentication.getName());
        announcement.setPublishedAt(LocalDateTime.now());
        
        announcementRepository.save(announcement);
        
        return ResponseEntity.ok(announcement);
    }
}