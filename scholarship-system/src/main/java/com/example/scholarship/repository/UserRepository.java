package com.example.scholarship.repository;

import com.example.scholarship.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据真实姓名查找用户
     */
    List<User> findByRealNameContaining(String realName);

    /**
     * 根据用户类型查找用户
     */
    List<User> findByUserType(User.UserType userType);

    /**
     * 根据用户状态查找用户
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * 查找未删除的用户
     */
    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    List<User> findAllActiveUsers();

    /**
     * 分页查找用户
     */
    Page<User> findByIsDeletedFalse(Pageable pageable);

    /**
     * 查找指定角色下的用户
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.isDeleted = false")
    List<User> findUsersByRole(@Param("roleName") String roleName);

    /**
     * 查找最近登录的用户
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginTime >= :startDate AND u.isDeleted = false")
    List<User> findUsersLoggedInSince(@Param("startDate") LocalDate startDate);

    /**
     * 统计用户数量（按类型分组）
     */
    @Query("SELECT u.userType, COUNT(u) FROM User u WHERE u.isDeleted = false GROUP BY u.userType")
    List<Object[]> countUsersByType();

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据用户名或邮箱查找用户
     */
    @Query("SELECT u FROM User u WHERE (u.username = :usernameOrEmail OR u.email = :usernameOrEmail) AND u.isDeleted = false")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}