package com.example.scholarship.service.impl;

import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.entity.Student;
import com.example.scholarship.repository.ReviewRepository;
import com.example.scholarship.repository.ScholarshipTypeRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 评审业务服务实现类
 * 
 * @author System
 * @version 1.0.0
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ScholarshipTypeRepository scholarshipTypeRepository;
    
    /**
     * 提交奖学金申请，实现防重复申请逻辑
     * 同一学生对同一奖学金类型只能有一个非rejected状态的申请
     */
    @Override
    @Transactional
    public Review submitApplication(Long userId, Long scholarshipTypeId) throws Exception {
        // 根据用户ID查找学生信息
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("未找到学生信息，请联系管理员"));
        
        // 验证奖学金类型是否存在
        ScholarshipType scholarshipType = scholarshipTypeRepository.findById(scholarshipTypeId)
                .orElseThrow(() -> new Exception("奖学金类型不存在"));
        
        // 检查学生当前有效申请数量是否已经达到2个（非rejected状态）
        long currentApplicationCount = reviewRepository.countByStudentIdAndReviewStatusNot(
                student.getId(), "rejected");
        if (currentApplicationCount >= 2) {
            throw new Exception("每位学生最多只能申请两种奖学金，请不要超过限制");
        }
        
        // 检查是否已经通过该奖学金的申请
        if (reviewRepository.existsByStudentIdAndScholarshipTypeIdAndReviewStatus(
                student.getId(), scholarshipTypeId, "approved")) {
            throw new Exception("您已经通过该奖学金的申请，无需再次申请");
        }
        
        // 检查是否已存在非rejected状态的申请
        if (reviewRepository.existsByStudentIdAndScholarshipTypeIdAndReviewStatusNot(
                student.getId(), scholarshipTypeId, "rejected")) {
            throw new Exception("您已经提交过该奖学金的申请，请不要重复申请");
        }
        
        // 创建新的评审记录
        Review review = new Review();
        review.setStudent(student);
        review.setScholarshipType(scholarshipType);
        // 使用实体类默认的reviewStatus值，不手动设置
        review.setCreatedBy(userId);
        review.setUpdatedBy(userId);
        // 设置默认评审人ID为1（系统管理员），避免违反数据库非空约束
        review.setReviewerId(1L);
        // 时间戳和版本号由AuditingEntityListener自动处理，不需要手动设置
        
        // 保存评审记录
        return reviewRepository.save(review);
    }
}
