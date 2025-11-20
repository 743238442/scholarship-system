package com.example.scholarship.controller;

import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
            
            return "student/dashboard";
            
        } catch (Exception e) {
            // 如果获取失败，显示默认信息
            model.addAttribute("studentName", "学生");
            model.addAttribute("pageTitle", "学生仪表板");
            return "student/dashboard";
        }
    }

    /**
     * 管理员审核页面
     */
    @GetMapping("/admin/reviews")
    public String adminReviews(Model model) {
        model.addAttribute("pageTitle", "申请审核");
        return "admin/reviews";
    }

    /**
     * 默认仪表板
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "仪表板");
        return "dashboard";
    }
}