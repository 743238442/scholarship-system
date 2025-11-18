package com.example.scholarship.config;

import com.example.scholarship.entity.UserEntity;
import com.example.scholarship.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 自定义用户详情服务
 * 用于Spring Security的用户认证
 * 
 * @author System
 * @version 1.0.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // 尝试通过用户名或邮箱查找用户
        UserEntity user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("用户未找到: " + usernameOrEmail)));

        // 检查用户是否激活
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("用户已被禁用: " + usernameOrEmail);
        }

        // 转换角色为GrantedAuthority
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive())
                .build();
    }

    /**
     * 创建测试用户（可选，用于开发测试）
     */
    public void createTestUsers() {
        // 创建管理员用户
        if (!userRepository.existsByUsername("admin")) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@scholarship.edu.cn");
            admin.setPhone("13800000000");
            admin.setRole(UserEntity.UserRole.ADMIN);
            admin.setIsActive(true);
            userRepository.save(admin);
        }

        // 创建学生用户
        if (!userRepository.existsByUsername("student")) {
            UserEntity student = new UserEntity();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setEmail("student@scholarship.edu.cn");
            student.setPhone("13900000000");
            student.setRole(UserEntity.UserRole.STUDENT);
            student.setIsActive(true);
            userRepository.save(student);
        }
    }
}