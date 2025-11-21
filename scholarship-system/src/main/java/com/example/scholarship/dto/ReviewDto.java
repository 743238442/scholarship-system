package com.example.scholarship.dto;

import com.example.scholarship.entity.Review;
import com.example.scholarship.entity.ReviewStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评审申请的数据传输对象
 */
@Data
public class ReviewDto {
    
    private Long id;
    private Long studentId;
    private Long scholarshipTypeId;
    private String scholarshipTypeName;
    private String reviewerName;
    private ReviewStatus reviewStatus;
    private String comments;
    private LocalDateTime createdAt;
    
    /**
     * 将Review实体转换为ReviewDto
     */
    public static ReviewDto fromReview(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setStudentId(review.getStudent().getId());
        dto.setScholarshipTypeId(review.getScholarshipType().getId());
        dto.setScholarshipTypeName(review.getScholarshipType().getName());
        dto.setReviewStatus(review.getReviewStatus());
        dto.setComments(review.getComments());
        dto.setCreatedAt(review.getCreatedAt());
        
        // 添加评审员信息
        if (review.getReviewerId() != null) {
            // 这里简化处理，实际可能需要通过userService获取用户名
            dto.setReviewerName("待分配");
        }
        
        return dto;
    }
}