package com.example.scholarship.dto;

import java.util.Date;

/**
 * 奖学金申请记录DTO
 * 
 * @author System
 * @version 1.0.0
 */
public class ReviewDto {
    private String scholarshipName;
    private String status;
    private Date appliedAt;
    private String comments;
    
    // 构造函数
    public ReviewDto() {
    }
    
    public ReviewDto(String scholarshipName, String status, Date appliedAt, String comments) {
        this.scholarshipName = scholarshipName;
        this.status = status;
        this.appliedAt = appliedAt;
        this.comments = comments;
    }
    
    // Getters and Setters
    public String getScholarshipName() {
        return scholarshipName;
    }
    
    public void setScholarshipName(String scholarshipName) {
        this.scholarshipName = scholarshipName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getAppliedAt() {
        return appliedAt;
    }
    
    public void setAppliedAt(Date appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
}