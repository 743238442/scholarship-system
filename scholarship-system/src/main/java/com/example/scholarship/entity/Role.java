package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "is_system")
    private Boolean isSystem = false;

    // 角色权限关联
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    // 角色权限关联
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tbl_role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}