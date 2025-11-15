package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_permission")
@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称长度不能超过100个字符")
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Size(max = 200, message = "权限描述长度不能超过200个字符")
    @Column(name = "description", length = 200)
    private String description;

    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    @Column(name = "code", unique = true, nullable = false, length = 100)
    private String code;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_type", nullable = false)
    private String menuType = "MENU";

    @Column(name = "menu_path", length = 200)
    private String menuPath;

    @Column(name = "menu_icon", length = 50)
    private String menuIcon;

    @Column(name = "menu_sort")
    private Integer menuSort = 0;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "is_system")
    private Boolean isSystem = false;

    // 权限角色关联
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    /**
     * 菜单类型枚举
     */
    public enum MenuType {
        MENU("菜单"),
        BUTTON("按钮"),
        API("接口"),
        DIRECTORY("目录");

        private final String description;

        MenuType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}