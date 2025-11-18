package com.example.scholarship.repository;

import com.example.scholarship.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<UserEntity> findByPhone(String phone);

    /**
     * 根据角色查找用户
     */
    List<UserEntity> findByRole(UserEntity.UserRole role);

    /**
     * 查找活跃用户
     */
    List<UserEntity> findByIsActiveTrue();

    /**
     * 查找非活跃用户
     */
    List<UserEntity> findByIsActiveFalse();

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据用户名或邮箱查找用户
     */
    @Query("SELECT u FROM UserEntity u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<UserEntity> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    /**
     * 统计各角色用户数量
     */
    @Query("SELECT u.role, COUNT(u) FROM UserEntity u GROUP BY u.role")
    List<Object[]> countUsersByRole();

    /**
     * 查找最近登录的用户
     */
    @Query("SELECT u FROM UserEntity u WHERE u.lastLoginAt >= :startDate ORDER BY u.lastLoginAt DESC")
    List<UserEntity> findRecentLoginUsers(@Param("startDate") LocalDateTime startDate);
}