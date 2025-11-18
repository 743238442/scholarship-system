package com.example.scholarship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.example.scholarship.config.CustomUserDetailsService;

/**
 * Spring Security配置类
 * 基于表单的登录，支持角色权限控制
 * 
 * @author System
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 认证提供者
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    /**
     * 登录成功处理器 - 根据角色跳转
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    /**
     * 自定义认证成功处理器
     */
    public static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, 
                                          HttpServletResponse response,
                                          org.springframework.security.core.Authentication authentication) 
                throws IOException {
            
            String targetUrl = "/dashboard"; // 默认跳转地址
            
            // 根据用户角色决定跳转地址
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                targetUrl = "/admin/reviews";
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"))) {
                targetUrl = "/student/dashboard";
            }
            
            response.sendRedirect(targetUrl);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 启用CSRF防护
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**") // API接口可以禁用CSRF（如果需要）
            )
            
            // 配置请求授权
            .authorizeHttpRequests(authorize -> authorize
                // 允许公开访问的路径
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/api/auth/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/static/**",
                    "/favicon.ico",
                    "/webjars/**"
                ).permitAll()
                
                // 学生角色可访问的路径
                .requestMatchers(
                    "/student/**",
                    "/api/student/**"
                ).hasRole("STUDENT")
                
                // 管理员角色可访问的路径
                .requestMatchers(
                    "/admin/**",
                    "/api/admin/**",
                    "/api/system/**"
                ).hasRole("ADMIN")
                
                // 教师角色可访问的路径（如果需要）
                .requestMatchers(
                    "/teacher/**",
                    "/api/teacher/**"
                ).hasRole("TEACHER")
                
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            
            // 配置登录页面
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            // 配置登出
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            )
            
            // 配置会话管理
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            );
        
        return http.build();
    }
}