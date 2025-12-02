package com.example.scholarship.repository;

import com.example.scholarship.entity.Announcement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 公告仓库接口
 *
 * @author System
 * @version 1.0.0
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // 查询未删除的公告
    List<Announcement> findByDeletedIsFalse(Sort sort);
    
    // 根据ID查询未删除的公告
    Announcement findByIdAndDeletedIsFalse(Long id);
}
