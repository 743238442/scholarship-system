package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 奖学金评审实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_review")
@Data
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity {

    // 关联学生 - 多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 关联奖学金类型 - 多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_type_id", nullable = false)
    private ScholarshipType scholarshipType;

    // 评审员ID（管理员ID）
    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;

    // 评审状态
    @NotNull(message = "评审状态不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false)
    private ReviewStatus reviewStatus = ReviewStatus.PENDING;

    // 评审意见
    @Size(max = 1000, message = "评审意见长度不能超过1000个字符")
    @Column(name = "comments", length = 1000)
    private String comments;



    // 手动添加getter方法，解决Lombok问题
    public Student getStudent() {
        return student;
    }

    public ScholarshipType getScholarshipType() {
        return scholarshipType;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public String getComments() {
        return comments;
    }

    // 手动添加setter方法
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setScholarshipType(ScholarshipType scholarshipType) {
        this.scholarshipType = scholarshipType;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}