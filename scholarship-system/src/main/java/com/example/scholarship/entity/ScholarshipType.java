package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 奖学金类型实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_scholarship_type")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScholarshipType extends BaseEntity {

    @NotBlank(message = "奖学金名称不能为空")
    @Size(max = 100, message = "奖学金名称长度不能超过100个字符")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "奖学金金额不能为空")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 500, message = "奖学金描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    @NotBlank(message = "申请条件不能为空")
    @Column(name = "eligibility_criteria", nullable = false, columnDefinition = "TEXT")
    private String eligibilityCriteria;

    // 手动添加getter方法，解决Lombok问题
    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    // 手动添加setter方法
    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }
}