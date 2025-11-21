package com.example.scholarship.controller;

import com.example.scholarship.entity.User;
import com.example.scholarship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证API控制器
 * 处理REST API形式的登录请求
 * 
 * @author System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * API登录接口
     * POST: /api/auth/login
     * 
     * @param loginRequest 登录请求参数
     * @return 登录响应
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 执行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            // 将认证结果设置到SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 获取用户信息
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            if (!userOptional.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            User user = userOptional.get();

            // 检查用户是否被删除
            if (user.getIsDeleted()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户已被删除");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            // 检查用户状态
            if (user.getStatus() == User.UserStatus.DISABLED || user.getStatus() == User.UserStatus.INACTIVE) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户已被禁用或未激活，请联系管理员");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // 构建成功响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("username", user.getUsername());
            response.put("userId", user.getId());
            response.put("role", user.getRoles() != null && !user.getRoles().isEmpty() 
                ? user.getRoles().iterator().next().getName() : "USER");

            // 登录成功后的JSESSIONID会自动在Cookie中设置
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * 登录请求参数类
     */
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}