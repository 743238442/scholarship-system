package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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
@EntityListeners(AuditingEntityListener.class)
public class Announcement extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "published_by", nullable = false)
    private Long publishedBy;
    
    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;
    
    // 软删除相关字段
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted = false;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
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