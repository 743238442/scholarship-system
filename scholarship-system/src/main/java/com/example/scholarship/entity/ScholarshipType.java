package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 奖学金类型实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_scholarship_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
@Data
@EqualsAndHashCode(callSuper = true)
public class ScholarshipType extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "奖学金名称不能为空")
    @Size(max = 100, message = "奖学金名称长度不能超过100个字符")
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @NotNull(message = "奖学金金额不能为空")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 1000, message = "申请条件长度不能超过1000个字符")
    @Column(name = "eligibility_criteria", length = 1000)
    private String eligibilityCriteria;
}