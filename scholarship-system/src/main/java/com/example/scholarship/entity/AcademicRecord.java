package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 学业记录实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_academic_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class AcademicRecord extends BaseEntity {

    @NotNull(message = "学生ID不能为空")
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @NotBlank(message = "课程名称不能为空")
    @Size(max = 200, message = "课程名称长度不能超过200个字符")
    @Column(name = "course_name", nullable = false, length = 200)
    private String courseName;

    @DecimalMin(value = "0.5", message = "学分必须大于等于0.5")
    @DecimalMax(value = "10.0", message = "学分必须小于等于10.0")
    @Column(name = "credit", nullable = false, precision = 3, scale = 1)
    private BigDecimal credit;

    @DecimalMin(value = "0.0", message = "绩点必须大于等于0.0")
    @DecimalMax(value = "4.0", message = "绩点必须小于等于4.0")
    @Column(name = "gpa", nullable = false, precision = 3, scale = 2)
    private BigDecimal gpa;

    @NotBlank(message = "学期不能为空")
    @Size(max = 20, message = "学期长度不能超过20个字符")
    @Column(name = "semester", nullable = false, length = 20)
    private String semester;

    // 与Student的多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    // 手动添加getter和setter方法
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}