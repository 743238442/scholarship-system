package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "announcements")
public class Announcement extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "published_by", nullable = false)
    private Long publishedBy;
    
    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;
    
    /**
     * 设置默认发布时间
     */
    @PrePersist
    protected void onCreate() {
        if (publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }
}