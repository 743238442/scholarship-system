package com.example.scholarship.controller;

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

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 首页
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    /**
     * 学生仪表板
     */
    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model) {
        model.addAttribute("pageTitle", "学生仪表板");
        return "student/dashboard";
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