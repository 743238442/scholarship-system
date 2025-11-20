package com.example.scholarship.config;

import com.example.scholarship.entity.User;
import com.example.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // 尝试通过用户名或邮箱查找用户
        com.example.scholarship.entity.User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("用户未找到: " + usernameOrEmail)));

        // 检查用户是否被删除
        if (user.getIsDeleted()) {
            throw new UsernameNotFoundException("用户已被删除: " + usernameOrEmail);
        }
        
        // 检查用户状态
        if (user.getStatus() == com.example.scholarship.entity.User.UserStatus.DISABLED || 
            user.getStatus() == com.example.scholarship.entity.User.UserStatus.INACTIVE) {
            throw new UsernameNotFoundException("用户已被禁用或未激活: " + usernameOrEmail);
        }

        // 转换角色为GrantedAuthority
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getUserType().name())
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getStatus() == com.example.scholarship.entity.User.UserStatus.DISABLED)
                .build();
    }

    /**
     * 创建测试用户（可选，用于开发测试）
     * 注意：由于Lombok配置问题，暂时注释此方法
     */
    /*
    public void createTestUsers() {
        // 创建管理员用户
        if (!userRepository.existsByUsername("admin")) {
            com.example.scholarship.entity.User admin = new com.example.scholarship.entity.User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@scholarship.edu.cn");
            admin.setPhone("13800000000");
            admin.setUserType(com.example.scholarship.entity.User.UserType.ADMIN);
            admin.setStatus(com.example.scholarship.entity.User.UserStatus.ACTIVE);
            admin.setIsDeleted(false);
            userRepository.save(admin);
        }

        // 创建学生用户
        if (!userRepository.existsByUsername("student")) {
            com.example.scholarship.entity.User student = new com.example.scholarship.entity.User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setEmail("student@scholarship.edu.cn");
            student.setPhone("13900000000");
            student.setUserType(com.example.scholarship.entity.User.UserType.STUDENT);
            student.setStatus(com.example.scholarship.entity.User.UserStatus.ACTIVE);
            student.setIsDeleted(false);
            userRepository.save(student);
        }
    }
    */
}