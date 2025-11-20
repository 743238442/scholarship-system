package com.example.scholarship.controller;

import com.example.scholarship.entity.AcademicRecord;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
import com.example.scholarship.repository.AcademicRecordRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicRecordRepository academicRecordRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 学生个人资料页面
     * 只允许STUDENT角色访问
     */
    @GetMapping("/student/profile")
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

            // 查找关联的学生信息
            Student student = studentRepository.findByUser(currentUser)
                    .orElseThrow(() -> {
                        log.error("找不到用户 {} 对应的学生信息", username);
                        return new RuntimeException("学生信息不存在");
                    });
            log.info("成功找到学生信息: {}, 学生ID: {}", student.getName(), student.getId());

            // 获取学生的学业记录
            List<AcademicRecord> academicRecords = academicRecordRepository.findByStudentId(student.getId());
            log.info("找到学业记录数量: {}", academicRecords.size());

            // 计算GPA平均分
            Double averageGpa = academicRecordRepository.calculateAverageGpa(student.getId());
            if (averageGpa == null) {
                averageGpa = 0.0;
            }
            log.info("平均GPA: {}", averageGpa);

            // 计算总学分
            Double totalCredits = academicRecordRepository.calculateTotalCredits(student.getId());
            if (totalCredits == null) {
                totalCredits = 0.0;
            }
            log.info("总学分: {}", totalCredits);

            // 统计信息
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCourses", academicRecords.size());
            stats.put("averageGpa", Math.round(averageGpa * 100.0) / 100.0);
            stats.put("totalCredits", totalCredits);
            
            // 优秀课程（GPA >= 3.5）
            List<AcademicRecord> excellentCourses = academicRecordRepository.findExcellentCourses(student.getId());
            stats.put("excellentCourses", excellentCourses.size());
            log.info("优秀课程数量: {}", excellentCourses.size());

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