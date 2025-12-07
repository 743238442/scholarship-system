package com.example.scholarship.repository;

import com.example.scholarship.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评审数据访问层接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    /**
     * 检查学生是否已经对特定奖学金类型有非拒绝状态的申请
     */
    boolean existsByStudentIdAndScholarshipTypeIdAndReviewStatusNot(Long studentId, Long scholarshipTypeId, String reviewStatus);
    
    /**
     * 根据学生ID和奖学金类型ID查找评审记录
     */
    Review findByStudentIdAndScholarshipTypeId(Long studentId, Long scholarshipTypeId);
    
    /**
     * 查找所有申请记录，按创建时间倒序
     */
    List<Review> findAllByOrderByCreatedAtDesc();
    
    /**
     * 根据学生ID查找评审记录，按创建时间倒序
     */
    List<Review> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    /**
     * 根据关键词搜索评审记录，按创建时间倒序
     * 关键词匹配学生姓名或奖学金类型名称
     */
    @Query("SELECT r FROM Review r JOIN r.student s JOIN r.scholarshipType st WHERE s.name LIKE %:keyword% OR st.name LIKE %:keyword% ORDER BY r.createdAt DESC")
    List<Review> searchReviewsByKeyword(@Param("keyword") String keyword);
    
    /**
     * 查询特定学生当前有效申请（非rejected状态）的数量
     */
    long countByStudentIdAndReviewStatusNot(Long studentId, String reviewStatus);
    
    /**
     * 查询特定学生已经通过的奖学金类型ID列表
     */
    @Query("SELECT r.scholarshipType.id FROM Review r WHERE r.student.id = :studentId AND r.reviewStatus = 'approved'")
    List<Long> findApprovedScholarshipTypeIdsByStudentId(@Param("studentId") Long studentId);
    
    /**
     * 检查学生是否已经通过该奖学金类型的申请
     */
    boolean existsByStudentIdAndScholarshipTypeIdAndReviewStatus(Long studentId, Long scholarshipTypeId, String reviewStatus);
}
