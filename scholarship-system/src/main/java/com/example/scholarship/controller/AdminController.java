package com.example.scholarship.controller;

import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.ReviewRepository;
import com.example.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 管理员控制器
 * 
 * @author System
 * @version 1.0.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理员仪表盘
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        // 获取当前登录的管理员用户
        String username = authentication.getName();
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员用户信息不存在"));
        
        model.addAttribute("adminName", adminUser.getRealName());
        model.addAttribute("pageTitle", "管理员仪表盘");
        
        return "admin/dashboard";
    }

    /**
     * 查看所有学生申请
     */
    @GetMapping("/reviews")
    public String viewAllApplications(Model model) {
        // 获取所有申请记录，按创建时间倒序排列
        List<Review> allApplications = reviewRepository.findAllByOrderByCreatedAtDesc();
        
        // 计算各状态的申请数量
        long pendingCount = allApplications.stream().filter(r -> "pending".equals(r.getReviewStatus())).count();
        long approvedCount = allApplications.stream().filter(r -> "approved".equals(r.getReviewStatus())).count();
        long rejectedCount = allApplications.stream().filter(r -> "rejected".equals(r.getReviewStatus())).count();
        long totalCount = allApplications.size();
        
        // 将数据添加到模型中
        model.addAttribute("applications", allApplications);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("rejectedCount", rejectedCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageTitle", "申请审核");
        
        return "admin/reviews";
    }
    
    /**
     * 查看审核详情
     */
    @GetMapping("/review/{id}/detail")
    public String viewReviewDetail(@PathVariable("id") Long reviewId, Model model) {
        // 查找审核记录
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("审核记录不存在"));
        
        model.addAttribute("review", review);
        model.addAttribute("pageTitle", "审核详情");
        
        return "admin/review-detail";
    }
    
    /**
     * 审核申请并添加评审意见
     */
    @PostMapping("/review/{reviewId}/process")
    public String processReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("action") String action,
            @RequestParam(value = "comments", required = false) String comments,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            // 获取当前登录的管理员用户
            String username = authentication.getName();
            User adminUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("管理员用户信息不存在"));
            
            // 查找审核记录
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("审核记录不存在"));
            
            // 检查申请状态是否为待审核
            if (!"pending".equals(review.getReviewStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage", "该申请已被处理，无法重复操作");
                return "redirect:/admin/reviews";
            }
            
            // 更新审核状态
            if ("approve".equals(action)) {
                review.setReviewStatus("approved");
                redirectAttributes.addFlashAttribute("successMessage", "申请已通过审核");
            } else if ("reject".equals(action)) {
                review.setReviewStatus("rejected");
                redirectAttributes.addFlashAttribute("successMessage", "申请已拒绝");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "无效的操作类型");
                return "redirect:/admin/reviews";
            }
            
            // 设置评审人ID
            review.setReviewerId(adminUser.getId());
            
            // 保存评审意见
            if (comments != null && !comments.trim().isEmpty()) {
                review.setComments(comments);
            }
            
            // 保存更新后的审核记录
            reviewRepository.save(review);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "处理申请时发生错误: " + e.getMessage());
        }
        
        return "redirect:/admin/reviews";
    }
    
    /**
     * 显示用户管理页面
     */
    @GetMapping("/user-management")
    public String userManagement(Model model) {
        // 获取所有用户
        List<User> allUsers = userRepository.findAll();
        
        // 统计各类型用户数量
        long adminCount = allUsers.stream().filter(u -> User.UserType.ADMIN.equals(u.getUserType())).count();
        long teacherCount = allUsers.stream().filter(u -> User.UserType.TEACHER.equals(u.getUserType())).count();
        long studentCount = allUsers.stream().filter(u -> User.UserType.STUDENT.equals(u.getUserType())).count();
        
        // 统计用户状态数量
        long activeCount = allUsers.stream().filter(u -> User.UserStatus.ACTIVE.equals(u.getStatus())).count();
        long inactiveCount = allUsers.stream().filter(u -> User.UserStatus.INACTIVE.equals(u.getStatus())).count();
        
        // 添加到模型
        model.addAttribute("users", allUsers);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("teacherCount", teacherCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("inactiveCount", inactiveCount);
        model.addAttribute("totalCount", allUsers.size());
        model.addAttribute("pageTitle", "用户管理");
        
        return "admin/user-management";
    }
    
    /**
     * 更新用户状态
     */
    @PostMapping("/user/{userId}/update-status")
    public String updateUserStatus(
            @PathVariable("userId") Long userId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        try {
            // 查找用户
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 更新用户状态
            user.setStatus(User.UserStatus.valueOf(status));
            userRepository.save(user);
            
            redirectAttributes.addFlashAttribute("successMessage", "用户状态已更新");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "更新用户状态时发生错误: " + e.getMessage());
        }
        
        return "redirect:/admin/user-management";
    }
    
    /**
     * 更新用户权限
     */
    @PostMapping("/user/{userId}/update-role")
    public String updateUserRole(
            @PathVariable("userId") Long userId,
            @RequestParam("userType") String userType,
            RedirectAttributes redirectAttributes) {
        try {
            // 查找用户
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 更新用户类型
            user.setUserType(User.UserType.valueOf(userType));
            userRepository.save(user);
            
            redirectAttributes.addFlashAttribute("successMessage", "用户权限已更新");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "更新用户权限时发生错误: " + e.getMessage());
        }
        
        return "redirect:/admin/user-management";
    }
    
    /**
     * 重置用户密码为默认密码123456
     */
    @PostMapping("/user/{userId}/reset-password")
    public String resetPassword(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 将密码重置为123456并加密
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
        
        redirectAttributes.addFlashAttribute("message", "用户密码已成功重置为默认密码123456");
        return "redirect:/admin/user-management";
    }
}
