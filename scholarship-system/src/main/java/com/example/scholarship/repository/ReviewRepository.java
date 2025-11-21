package com.example.scholarship.repository;

import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 奖学金评审数据访问接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 根据学生ID查询评审记录
     * 
     * @param studentId 学生ID
     * @return 评审记录列表
     */
    List<Review> findByStudentId(Long studentId);

    /**
     * 根据学生ID分页查询评审记录，按创建时间倒序
     * 
     * @param studentId 学生ID
     * @param pageable 分页参数
     * @return 分页的评审记录
     */
    Page<Review> findByStudentIdOrderByCreatedAtDesc(Long studentId, Pageable pageable);

    /**
     * 根据奖学金类型ID查询评审记录
     * 
     * @param scholarshipTypeId 奖学金类型ID
     * @return 评审记录列表
     */
    List<Review> findByScholarshipTypeId(Long scholarshipTypeId);

    /**
     * 根据评审状态查询评审记录
     * 
     * @param reviewStatus 评审状态
     * @return 评审记录列表
     */
    List<Review> findByReviewStatus(ReviewStatus reviewStatus);

    /**
     * 根据学生ID和奖学金类型ID查询特定评审记录
     * 
     * @param studentId 学生ID
     * @param scholarshipTypeId 奖学金类型ID
     * @return 评审记录对象
     */
    Optional<Review> findByStudentIdAndScholarshipTypeId(Long studentId, Long scholarshipTypeId);

    /**
     * 根据评审员ID查询评审记录
     * 
     * @param reviewerId 评审员ID
     * @return 评审记录列表
     */
    List<Review> findByReviewerId(Long reviewerId);

    /**
     * 根据学生ID和评审状态查询评审记录
     * 
     * @param studentId 学生ID
     * @param reviewStatus 评审状态
     * @return 评审记录列表
     */
    List<Review> findByStudentIdAndReviewStatus(Long studentId, ReviewStatus reviewStatus);

    /**
     * 根据奖学金类型ID和评审状态查询评审记录
     * 
     * @param scholarshipTypeId 奖学金类型ID
     * @param reviewStatus 评审状态
     * @return 评审记录列表
     */
    List<Review> findByScholarshipTypeIdAndReviewStatus(Long scholarshipTypeId, ReviewStatus reviewStatus);
}