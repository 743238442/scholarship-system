package com.example.scholarship.controller;

import com.example.scholarship.entity.Announcement;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.AnnouncementRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * 登录控制器
 * 
 * @author System
 * @version 1.0.0
 */
@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AnnouncementRepository announcementRepository;

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 首页 - 根据用户状态决定显示
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * 学生仪表板
     */
    @GetMapping("/student/dashboard")
    public String studentDashboard(Authentication authentication, Model model) {
        try {
            // 获取当前登录用户
            String username = authentication.getName();
            
            // 通过用户名查找用户
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户信息不存在"));

            // 查找关联的学生信息
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("学生信息不存在"));

            // 添加学生姓名到模型
            model.addAttribute("studentName", student.getName());
            model.addAttribute("pageTitle", "学生仪表板");
            
            // 获取公告列表，按发布时间倒序排列
            List<Announcement> announcements = announcementRepository.findAll(Sort.by(Sort.Direction.DESC, "publishedAt"));
            model.addAttribute("announcements", announcements);
            
            return "student/dashboard";
            
        } catch (Exception e) {
            // 如果获取失败，显示默认信息
            model.addAttribute("studentName", "学生");
            model.addAttribute("pageTitle", "学生仪表板");
            return "student/dashboard";
        }
    }

    /**
     * 查看公告详情
     */
    @GetMapping("/student/announcement/{id}")
    public String viewAnnouncement(@PathVariable Long id, Authentication authentication, Model model) {
        try {
            // 获取当前登录用户
            String username = authentication.getName();
            
            // 通过用户名查找用户
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户信息不存在"));

            // 查找关联的学生信息
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> new RuntimeException("学生信息不存在"));

            // 添加学生姓名到模型
            model.addAttribute("studentName", student.getName());
            model.addAttribute("pageTitle", "公告详情");
            
            // 获取公告详情
            Announcement announcement = announcementRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("公告不存在"));
            model.addAttribute("announcement", announcement);
            
            return "student/announcement-detail";
            
        } catch (Exception e) {
            // 如果获取失败，显示默认信息
            model.addAttribute("studentName", "学生");
            model.addAttribute("pageTitle", "公告详情");
            return "student/announcement-detail";
        }
    }

    /**
     * 管理员审核页面
     */
    // 注意：此路径已在AdminController中实现，这里不再重复映射
    // 如需使用，可取消注释并修改为其他路径
    /*
    @GetMapping("/admin/reviews-old")
    public String adminReviews(Model model) {
        model.addAttribute("pageTitle", "申请审核");
        return "admin/reviews";
    }
    */

    /**
     * 默认仪表板
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "仪表板");
        return "dashboard";
    }
}