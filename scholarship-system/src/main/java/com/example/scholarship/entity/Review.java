package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 评审实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_review")
@Data
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // 关联学生
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 关联奖学金类型
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_type_id", nullable = false)
    private ScholarshipType scholarshipType;

    // 评审人ID
    @Column(name = "reviewer_id")
    private Long reviewerId;

    // 评审状态，默认为pending
    @Size(max = 20, message = "评审状态长度不能超过20个字符")
    @Column(name = "review_status", nullable = false, length = 20)
    private String reviewStatus = "pending";

    // 评审意见
    @Size(max = 1000, message = "评审意见长度不能超过1000个字符")
    @Column(name = "comments", length = 1000)
    private String comments;
    
    // 学年，用于唯一约束
    @Column(name = "academic_year", length = 20, nullable = false)
    private String academicYear = "2024-2025"; // 设置默认值为当前学年
}