package com.example.scholarship.service;

import com.example.scholarship.entity.UserEntity;
import com.example.scholarship.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 * 
 * @author System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 保存用户
     */
    public UserEntity saveUser(UserEntity user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置创建时间
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userEntityRepository.save(user);
    }

    /**
     * 根据ID查找用户
     */
    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userEntityRepository.findById(id);
    }

    /**
     * 根据用户名查找用户
     */
    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUsername(String username) {
        return userEntityRepository.findByUsername(username);
    }

    /**
     * 根据邮箱查找用户
     */
    @Transactional(readOnly = true)
    public Optional<UserEntity> findByEmail(String email) {
        return userEntityRepository.findByEmail(email);
    }

    /**
     * 用户登录
     */
    @Transactional(readOnly = true)
    public Optional<UserEntity> login(String username, String password) {
        Optional<UserEntity> userOpt = userEntityRepository.findByUsername(username);
        
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            // 更新最后登录时间
            UserEntity user = userOpt.get();
            user.setLastLoginAt(LocalDateTime.now());
            userEntityRepository.save(user);
            return userOpt;
        }
        
        return Optional.empty();
    }

    /**
     * 检查用户名是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userEntityRepository.existsByUsername(username);
    }

    /**
     * 检查邮箱是否存在
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userEntityRepository.existsByEmail(email);
    }

    /**
     * 查找所有用户
     */
    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return userEntityRepository.findAll();
    }

    /**
     * 查找指定角色的用户
     */
    @Transactional(readOnly = true)
    public List<UserEntity> findByRole(UserEntity.UserRole role) {
        return userEntityRepository.findByRole(role);
    }

    /**
     * 查找活跃用户
     */
    @Transactional(readOnly = true)
    public List<UserEntity> findActiveUsers() {
        return userEntityRepository.findByIsActiveTrue();
    }

    /**
     * 更新用户状态
     */
    public void updateUserStatus(Long userId, Boolean isActive) {
        userEntityRepository.findById(userId).ifPresent(user -> {
            user.setIsActive(isActive);
            user.setUpdatedAt(LocalDateTime.now());
            userEntityRepository.save(user);
        });
    }

    /**
     * 更新用户信息
     */
    public UserEntity updateUser(Long userId, UserEntity updatedUser) {
        return userEntityRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPhone(updatedUser.getPhone());
                    existingUser.setRole(updatedUser.getRole());
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    return userEntityRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long userId) {
        userEntityRepository.deleteById(userId);
    }

    /**
     * 统计各角色用户数量
     */
    @Transactional(readOnly = true)
    public List<Object[]> getUserStatistics() {
        return userEntityRepository.countUsersByRole();
    }
}