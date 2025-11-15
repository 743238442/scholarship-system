package com.example.scholarship.service;

import com.example.scholarship.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 * 
 * @author System
 * @version 1.0.0
 */
public interface UserService {

    /**
     * 保存用户
     */
    User save(User user);

    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);

    /**
     * 查找所有用户
     */
    List<User> findAll();

    /**
     * 分页查找用户
     */
    Page<User> findAll(Pageable pageable);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    User findByEmail(String email);

    /**
     * 根据真实姓名查找用户
     */
    List<User> findByRealName(String realName);

    /**
     * 根据用户类型查找用户
     */
    List<User> findByUserType(User.UserType userType);

    /**
     * 根据用户状态查找用户
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * 删除用户（软删除）
     */
    void deleteById(Long id);

    /**
     * 验证用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 验证邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 用户登录验证
     */
    boolean login(String username, String password);

    /**
     * 增加用户登录次数
     */
    void increaseLoginCount(Long userId);

    /**
     * 重置用户密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 修改用户状态
     */
    void changeUserStatus(Long userId, User.UserStatus status);

    /**
     * 分配角色给用户
     */
    void assignRole(Long userId, Long roleId);

    /**
     * 移除用户角色
     */
    void removeRole(Long userId, Long roleId);

    /**
     * Spring Security用户详情加载
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 注册新用户
     */
    User register(User user);

    /**
     * 用户名或邮箱登录
     */
    User loginByUsernameOrEmail(String usernameOrEmail, String password);

    /**
     * 获取用户统计数据
     */
    Object[] getUserStatistics();

    /**
     * 查找指定角色的所有用户
     */
    List<User> findUsersByRole(String roleName);

    /**
     * 查找最近登录的用户
     */
    List<User> findRecentLoginUsers(int days);

    /**
     * 批量导入用户
     */
    List<User> batchImportUsers(List<User> users);
}