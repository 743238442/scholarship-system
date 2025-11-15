package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    @Column(name = "real_name", nullable = false, length = 100)
    private String realName;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20个字符")
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Column(name = "last_login_time")
    private LocalDate lastLoginTime;

    @Column(name = "login_count")
    private Integer loginCount = 0;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "remark", length = 1000)
    private String remark;

    // 用户角色关联
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tbl_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        ACTIVE("活跃"),
        INACTIVE("非活跃"),
        LOCKED("锁定"),
        DISABLED("禁用");

        private final String description;

        UserStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 用户类型枚举
     */
    public enum UserType {
        STUDENT("学生"),
        TEACHER("教师"),
        ADMIN("管理员");

        private final String description;

        UserType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 检查用户是否具有指定角色
     */
    public boolean hasRole(String roleName) {
        return roles != null && roles.stream()
                .anyMatch(role -> roleName.equals(role.getName()));
    }

    /**
     * 增加登录次数
     */
    public void increaseLoginCount() {
        this.loginCount = (this.loginCount == null ? 0 : this.loginCount) + 1;
        this.lastLoginTime = LocalDate.now();
    }
}