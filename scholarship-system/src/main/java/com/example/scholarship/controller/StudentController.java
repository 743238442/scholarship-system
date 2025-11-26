package com.example.scholarship.controller;

import com.example.scholarship.entity.AcademicRecord;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.UserRepository;
import com.example.scholarship.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生信息控制器
 * 
 * @author System
 * @version 1.0.0
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentService studentService;

    /**
     * 学生申请记录页面
     * 只允许STUDENT角色访问
     */
    // 学生申请记录功能已在StudentScholarshipController中实现

    /**
     * 学生个人资料页面
     * 只允许STUDENT角色访问
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentProfile(Authentication authentication, Model model) {
        try {
            // 获取当前登录用户
            String username = authentication.getName();
            log.info("开始处理学生个人资料页面请求，用户: {}", username);
            
            // 通过用户名查找用户
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("找不到用户: {}", username);
                        return new RuntimeException("用户信息不存在");
                    });
            log.info("成功找到用户: {}, 用户ID: {}", username, currentUser.getId());

            // 使用StudentService通过userId获取学生信息及其关联的学业记录
            Student student = studentService.findByUserId(currentUser.getId())
                    .orElseThrow(() -> {
                        log.error("找不到用户 {} 对应的学生信息", username);
                        return new RuntimeException("学生信息不存在");
                    });
            log.info("成功找到学生信息: {}, 学生ID: {}", student.getName(), student.getId());
            
            // 从Student对象中获取关联的学业记录，确保不为null
            List<AcademicRecord> academicRecords = student.getAcademicRecords();
            if (academicRecords == null) {
                academicRecords = java.util.Collections.emptyList();
            }
            log.info("找到学业记录数量: {}", academicRecords.size());

            // 计算GPA平均分、总学分和优秀课程数量
            double averageGpa = 0.0;
            double totalCredits = 0.0;
            int excellentCourseCount = 0;
            
            if (!academicRecords.isEmpty()) {
                double totalGpa = 0.0;
                for (AcademicRecord record : academicRecords) {
                    if (record.getGpa() != null) {
                        totalGpa += record.getGpa().doubleValue();
                        if (record.getGpa().compareTo(new BigDecimal("3.5")) >= 0) {
                            excellentCourseCount++;
                        }
                    }
                    if (record.getCredit() != null) {
                        totalCredits += record.getCredit().doubleValue();
                    }
                }
                averageGpa = totalGpa / academicRecords.size();
            }
            
            averageGpa = Math.round(averageGpa * 100.0) / 100.0;
            totalCredits = Math.round(totalCredits * 100.0) / 100.0;
            
            log.info("平均GPA: {}, 总学分: {}, 优秀课程数量: {}", averageGpa, totalCredits, excellentCourseCount);
            
            // 统计信息
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCourses", academicRecords.size());
            stats.put("averageGpa", averageGpa);
            stats.put("totalCredits", totalCredits);
            stats.put("excellentCourses", excellentCourseCount);

            // 添加个性化欢迎信息
            String welcomeMessage = "欢迎回来，" + student.getName() + "同学！";
            model.addAttribute("welcomeMessage", welcomeMessage);
            model.addAttribute("studentName", student.getName()); // 为顶部欢迎区域提供学生姓名
            
            // 添加模型属性
            model.addAttribute("student", student);
            model.addAttribute("academicRecords", academicRecords);
            model.addAttribute("stats", stats);
            model.addAttribute("pageTitle", "个人资料");

            log.info("成功处理学生个人资料页面请求，返回视图");
            return "student/profile";
            
        } catch (Exception e) {
            log.error("处理学生个人资料页面时发生错误: {}", e.getMessage(), e);
            model.addAttribute("error", "加载个人资料时发生错误: " + e.getMessage());
            model.addAttribute("pageTitle", "错误");
            return "error"; // 或者返回一个错误页面
        }
    }


}