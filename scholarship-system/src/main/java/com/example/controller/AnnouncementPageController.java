package com.example.controller;

import com.example.entity.Announcement;
import com.example.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class AnnouncementPageController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping("/announcements")
    public String announcementPage(Model model) {
        // 获取所有公告，按发布时间降序排列
        List<Announcement> announcements = announcementRepository.findAllByOrderByPublishedAtDesc();
        model.addAttribute("announcements", announcements);
        return "announcements";
    }
    
    @GetMapping("/announcements/{id}")
    public String announcementDetail(@PathVariable("id") Long id, Model model) {
        // 通过id获取公告详情
        Optional<Announcement> announcement = announcementRepository.findById(id);
        model.addAttribute("announcement", announcement.orElse(null));
        return "announcement-detail";
    }
}