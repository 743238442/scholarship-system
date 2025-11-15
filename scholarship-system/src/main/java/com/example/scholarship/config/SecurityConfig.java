package com.example.scholarship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security配置类
 * 
 * @author System
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（开发环境）
            .csrf(AbstractHttpConfigurer::disable)
            
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
                    "/favicon.ico"
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
                
                // 教师角色可访问的路径
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
                .defaultSuccessUrl("/dashboard", true)
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