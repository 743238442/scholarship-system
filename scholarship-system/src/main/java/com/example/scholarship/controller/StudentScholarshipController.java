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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.lang.Exception;

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
     * 显示奖学金申请页面，包含所有可申请的奖学金类型（排除已通过的类型）
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/apply")
    public String showApplyPage(Model model, Authentication authentication) {
        List<ScholarshipType> scholarshipTypes = new java.util.ArrayList<>();
        
        try {
            // 获取当前登录用户
            String username = authentication.getName();
            
            // 根据用户名查找学生信息
            Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new Exception("未找到学生信息，请联系管理员"));
            
            // 添加学生姓名用于欢迎信息
            model.addAttribute("studentName", student.getName());
            
            // 创建用户对象，设置真实的学生信息
            User currentUser = student.getUser();
            // 确保realName使用学生的真实姓名，username使用学号
            currentUser.setRealName(student.getName());
            currentUser.setUsername(student.getStudentNo());
            model.addAttribute("currentUser", currentUser);
            
            // 获取学生ID
            Long studentId = student.getId();
            
            // 获取学生已经通过的奖学金类型ID列表
            List<Long> approvedScholarshipTypeIds = reviewRepository.findApprovedScholarshipTypeIdsByStudentId(studentId);
            
            // 获取所有奖学金类型
            List<ScholarshipType> allScholarshipTypes = scholarshipTypeService.findAll();
            
            // 过滤出学生尚未通过申请的奖学金类型
            scholarshipTypes = allScholarshipTypes.stream()
                .filter(type -> !approvedScholarshipTypeIds.contains(type.getId()))
                .collect(java.util.stream.Collectors.toList());
            
        } catch (Exception e) {
            // 如果获取失败，提供默认信息
            model.addAttribute("errorMessage", "获取学生信息失败: " + e.getMessage());
            model.addAttribute("studentName", "未知用户");
            
            // 创建默认用户对象，避免页面错误
            User defaultUser = new User();
            defaultUser.setUsername("未知");
            defaultUser.setRealName("未知");
            model.addAttribute("currentUser", defaultUser);
            
            // 如果出现错误，返回所有奖学金类型
            scholarshipTypes = scholarshipTypeService.findAll();
        }
        
        model.addAttribute("scholarshipTypes", scholarshipTypes);
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
            
            // 获取当前学生的姓名，用于页面显示
            model.addAttribute("studentName", student.getName());
            
            // 创建用户对象，设置真实的学生信息
            User currentUser = student.getUser();
            currentUser.setRealName(student.getName());
            currentUser.setUsername(student.getStudentNo());
            model.addAttribute("currentUser", currentUser);
            
            // 获取学生ID
            Long studentId = student.getId();
            
            // 根据学生ID直接查询申请记录，避免N+1查询问题
            List<Review> studentApplications = reviewRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
            
            // 转换为DTO对象列表，确保字段名和格式与模板一致
            List<Object> reviewDtos = studentApplications.stream().map(review -> {
                // 创建一个Map对象来模拟DTO
                java.util.Map<String, Object> dto = new java.util.HashMap<>();
                dto.put("id", review.getId());
                
                // 处理奖学金类型信息
                if (review.getScholarshipType() != null) {
                    dto.put("scholarshipType", java.util.Map.of("name", review.getScholarshipType().getName()));
                } else {
                    dto.put("scholarshipType", java.util.Map.of("name", "未知类型"));
                }
                
                // 将LocalDateTime转换为java.util.Date以兼容Thymeleaf的#dates.format
                if (review.getCreatedAt() != null) {
                    dto.put("createdAt", Date.from(review.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                } else {
                    dto.put("createdAt", new Date());
                }
                // 将reviewStatus转换为大写的status，以匹配模板期望
                dto.put("status", review.getReviewStatus().toUpperCase());
                // 将comments转换为comment，以匹配模板期望
                dto.put("comment", review.getComments());
                // 保留原始reviewStatus用于删除按钮的条件判断
                dto.put("reviewStatus", review.getReviewStatus());
                
                return dto;
            }).collect(Collectors.toList());
            
            // 添加到model中，使用reviewDtos名称以匹配模板
            model.addAttribute("reviewDtos", reviewDtos);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "加载申请记录失败: " + e.getMessage());
            model.addAttribute("reviewDtos", List.of()); // 提供空列表以避免页面错误
            // 添加默认的学生信息以避免页面错误
            model.addAttribute("studentName", "未知用户");
            User defaultUser = new User();
            defaultUser.setUsername("未知");
            defaultUser.setRealName("未知");
            model.addAttribute("currentUser", defaultUser);
        }
        
        return "student/my-applications";
    }
    
    /**
     * 删除待审核的奖学金申请
     */
    @PostMapping("/my-applications/delete/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public String deleteApplication(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // 获取当前登录用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // 根据用户名查找学生信息
            Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new Exception("未找到学生信息，请联系管理员"));
            
            // 查找申请记录
            Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new Exception("未找到申请记录"));
            
            // 检查申请是否属于当前学生
            if (!review.getStudent().getId().equals(student.getId())) {
                throw new Exception("您无权删除此申请记录");
            }
            
            // 检查申请状态是否为待审核
            if (!review.getReviewStatus().equals("pending")) {
                throw new Exception("只能删除待审核的申请记录");
            }
            
            // 删除申请记录
            reviewRepository.delete(review);
            
            // 添加成功消息
            redirectAttributes.addFlashAttribute("successMessage", "申请记录删除成功！");
        } catch (Exception e) {
            // 设置错误消息
            redirectAttributes.addFlashAttribute("errorMessage", "删除申请失败: " + e.getMessage());
        }
        
        // 重定向到申请列表页面
        return "redirect:/student/my-applications";
    }
    

}