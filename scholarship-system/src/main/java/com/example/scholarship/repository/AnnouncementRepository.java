package com.example.scholarship.repository;

import com.example.scholarship.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 公告仓库接口
 *
 * @author System
 * @version 1.0.0
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // 可以添加自定义查询方法
}
