package com.example.scholarship.controller;

import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.entity.User;
import com.example.scholarship.entity.Student;
import com.example.scholarship.repository.ReviewRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.service.ReviewService;
import com.example.scholarship.service.impl.ScholarshipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;

import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * 学生奖学金申请控制器
 * 
 * @author System
 * @version 1.0.0
 */
@Controller
@RequestMapping("/student")
public class StudentScholarshipController {

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ScholarshipTypeService scholarshipTypeService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * 显示奖学金申请页面，包含所有可申请的奖学金类型
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/apply")
    public String showApplyPage(Model model, Authentication authentication) {
        // 获取奖学金类型列表
        List<ScholarshipType> scholarshipTypes = scholarshipTypeService.findAll();
        model.addAttribute("scholarshipTypes", scholarshipTypes);
        
        try {
            // 获取当前登录用户
            String username = authentication.getName();
            
            // 根据用户名查找学生信息
            Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new Exception("未找到学生信息，请联系管理员"));
            
            // 创建用户对象，设置真实的学生信息
            User currentUser = student.getUser();
            // 确保realName使用学生的真实姓名，username使用学号
            currentUser.setRealName(student.getName());
            currentUser.setUsername(student.getStudentNo());
            model.addAttribute("currentUser", currentUser);
        } catch (Exception e) {
            // 如果获取失败，提供默认信息
            model.addAttribute("errorMessage", "获取学生信息失败: " + e.getMessage());
            
            // 创建默认用户对象，避免页面错误
            User defaultUser = new User();
            defaultUser.setUsername("未知");
            defaultUser.setRealName("未知");
            model.addAttribute("currentUser", defaultUser);
        }
        
        return "student/apply";
    }
    
    /**
     * 提交奖学金申请
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/apply")
    public String submitApplication(Long scholarshipTypeId, RedirectAttributes redirectAttributes) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 根据用户名查找学生信息
            Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new Exception("未找到学生信息，请联系管理员"));
            
            // 获取用户ID
            Long userId = student.getUser().getId();
            
            // 提交申请，传入用户ID
            reviewService.submitApplication(userId, scholarshipTypeId);
            
            // 添加成功消息
            redirectAttributes.addFlashAttribute("successMessage", "奖学金申请提交成功！");
            
            // 重定向到申请列表页面（使用绝对路径）
            return "redirect:/student/my-applications";
        } catch (Exception e) {
            // 设置错误消息
            redirectAttributes.addFlashAttribute("errorMessage", "申请提交失败: " + e.getMessage());
            return "redirect:/student/apply";
        }
    }
    
    /**
     * 查询学生的所有申请记录
     */
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('STUDENT')")
    public String getMyApplications(Model model) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 根据用户名查找学生信息
            Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new Exception("未找到学生信息，请联系管理员"));
            
            // 先获取所有申请记录，然后过滤出当前学生的申请
            List<Review> allApplications = reviewRepository.findAllByOrderByCreatedAtDesc();
            List<Review> studentApplications = allApplications.stream()
                .filter(review -> review.getStudent().getId().equals(student.getId()))
                .collect(Collectors.toList());
            
            // 添加到model中，使用reviewDtos名称以匹配模板
            model.addAttribute("reviewDtos", studentApplications);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "加载申请记录失败: " + e.getMessage());
            model.addAttribute("reviewDtos", List.of()); // 提供空列表以避免页面错误
        }
        
        return "student/my-applications";
    }
}