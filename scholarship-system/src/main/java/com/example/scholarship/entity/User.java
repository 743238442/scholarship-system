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

    // 学生信息关联 - 一对一关系
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Student student;

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

    // 手动添加getter方法，解决Lombok问题
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserType getUserType() {
        return userType;
    }

    public LocalDate getLastLoginTime() {
        return lastLoginTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public String getRemark() {
        return remark;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Student getStudent() {
        return student;
    }

    // 手动添加setter方法
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setLastLoginTime(LocalDate lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}