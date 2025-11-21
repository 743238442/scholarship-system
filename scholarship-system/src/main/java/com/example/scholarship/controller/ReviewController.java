package com.example.scholarship.controller;

// 使用完全限定名以避免导入问题
// import com.example.scholarship.common.ApiResponse;
// import com.example.scholarship.exception.BusinessException;
// import com.example.scholarship.common.ErrorCode;
import com.example.scholarship.dto.ReviewDto;
import com.example.scholarship.dto.ReviewRequestDto;
import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.entity.User;
import com.example.scholarship.entity.ReviewStatus;
import com.example.scholarship.repository.ReviewRepository;
import com.example.scholarship.repository.ScholarshipTypeRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 奖学金评审控制器
 * 
 * @author System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ScholarshipTypeRepository scholarshipTypeRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * 提交奖学金申请
     * 由当前认证的学生提交申请，检查是否重复申请
     */
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitReview(
            @Valid @RequestBody ReviewRequestDto requestDto,
            Authentication authentication) {
        
        try {
            // 获取当前认证用户的用户名
            String username = authentication.getName();
            
            // 查找对应的用户和学生信息
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.example.scholarship.exception.BusinessException("用户不存在", com.example.scholarship.common.ErrorCode.USER_NOT_EXIST));
            
            Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new com.example.scholarship.exception.BusinessException("学生信息不存在", com.example.scholarship.common.ErrorCode.STUDENT_NOT_EXIST));
            
            // 验证奖学金类型是否存在
            ScholarshipType scholarshipType = scholarshipTypeRepository.findById(requestDto.getScholarshipTypeId())
                .orElseThrow(() -> new com.example.scholarship.exception.BusinessException("奖学金类型不存在", com.example.scholarship.common.ErrorCode.SCHOLARSHIP_TYPE_NOT_EXIST));
            
            // 检查是否已存在该学生对同一奖学金类型的申请（避免重复）
            Optional<Review> existingReview = reviewRepository.findByStudentIdAndScholarshipTypeId(
                student.getId(), scholarshipType.getId());
            
            if (existingReview.isPresent()) {
                throw new com.example.scholarship.exception.BusinessException("您已经申请过该类型的奖学金", com.example.scholarship.common.ErrorCode.APPLICATION_DUPLICATE);
            }
            
            // 创建新的评审申请记录
            Review review = new Review();
            review.setStudent(student);
            review.setScholarshipType(scholarshipType);
            review.setReviewStatus(ReviewStatus.PENDING);
            // 设置评审员ID为默认值，后续可以分配给具体管理员
            review.setReviewerId(1L); // 假设1是默认管理员ID
            review.setCreatedBy(user.getId());
            review.setUpdatedBy(user.getId());
            
            // 保存评审申请
            Review savedReview = reviewRepository.save(review);
            
            // 转换为DTO并返回
            ReviewDto reviewDto = ReviewDto.fromReview(savedReview);
            return ResponseEntity.ok(reviewDto);
            
        } catch (com.example.scholarship.exception.BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(com.example.scholarship.common.ApiResponse.error(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(com.example.scholarship.common.ApiResponse.error(com.example.scholarship.common.ErrorCode.SYSTEM_ERROR, "提交申请失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取当前学生的申请列表
     * 分页返回，按创建时间倒序排序
     */
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMyApplications(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            // 获取当前登录用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new com.example.scholarship.exception.BusinessException("用户不存在", com.example.scholarship.common.ErrorCode.USER_NOT_EXIST));
            
            // 查找对应的学生信息
            Student student = studentRepository.findByUser(user)
                    .orElseThrow(() -> new com.example.scholarship.exception.BusinessException("学生信息不存在", com.example.scholarship.common.ErrorCode.STUDENT_NOT_EXIST));
            
            // 使用新添加的分页查询方法获取当前学生的申请记录
            Page<Review> reviewPage = reviewRepository.findByStudentIdOrderByCreatedAtDesc(student.getId(), pageable);
            
            // 将Review实体转换为ReviewDto
            Page<ReviewDto> reviewDtoPage = reviewPage.map(ReviewDto::fromReview);
            
            return ResponseEntity.ok(reviewDtoPage);
        } catch (com.example.scholarship.exception.BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.example.scholarship.common.ApiResponse.error(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(com.example.scholarship.common.ApiResponse.error(com.example.scholarship.common.ErrorCode.SYSTEM_ERROR, "获取申请列表失败: " + e.getMessage()));
        }
    }
}