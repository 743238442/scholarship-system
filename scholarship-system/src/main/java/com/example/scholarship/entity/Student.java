package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 学生实体类
 * 
 * @author System
 * @version 1.0.0
 */
@Entity
@Table(name = "tbl_student")
@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    @Pattern(regexp = "^[0-9]{8,20}$", message = "学号必须是8-20位数字")
    @Column(name = "student_no", unique = true, nullable = false, length = 20)
    private String studentNo;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "性别不能为空")
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotBlank(message = "学院不能为空")
    @Size(max = 100, message = "学院长度不能超过100个字符")
    @Column(name = "college", nullable = false, length = 100)
    private String college;

    @NotBlank(message = "专业不能为空")
    @Size(max = 100, message = "专业长度不能超过100个字符")
    @Column(name = "major", nullable = false, length = 100)
    private String major;

    @NotBlank(message = "班级不能为空")
    @Size(max = 50, message = "班级长度不能超过50个字符")
    @Column(name = "class", nullable = false, length = 50)
    private String clazz;

    @NotBlank(message = "年级不能为空")
    @Pattern(regexp = "^20\\d{2}$", message = "年级格式不正确，必须是20XX年格式")
    @Column(name = "grade", nullable = false, length = 4)
    private String grade;

    @Size(max = 20, message = "联系方式长度不能超过20个字符")
    @Column(name = "contact", length = 20)
    private String contact;

    @NotNull(message = "入学日期不能为空")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "is_graduated", nullable = false)
    private Boolean isGraduated = false;

    // 学制年限
    @Column(name = "study_years")
    private Integer studyYears = 4;

    // 关联用户 - 一对一关系
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
    
    // 关联学业记录 - 一对多关系
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicRecord> academicRecords;
    
    // 添加获取userId的便捷方法
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    /**
     * 性别枚举
     */
    public enum Gender {
        MALE("男"),
        FEMALE("女");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @NotBlank(message = "身份证号不能为空")
    @Size(max = 18, message = "身份证号长度不能超过18个字符")
    @Pattern(regexp = "^[0-9]{17}[0-9Xx]$", message = "身份证号格式不正确")
    @Column(name = "id_card", nullable = false, length = 18)
    private String idCard;

    @NotNull(message = "状态不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentStatus status = StudentStatus.ACTIVE;

    /**
     * 学生状态枚举
     */
    public enum StudentStatus {
        ACTIVE("在读"),
        GRADUATED("已毕业"),
        SUSPENDED("休学"),
        DROPPED_OUT("退学");

        private final String description;

        StudentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 手动添加getter方法，解决Lombok问题
    public String getStudentNo() {
        return studentNo;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getCollege() {
        return college;
    }

    public String getMajor() {
        return major;
    }

    // 注意：className字段已被移除，使用clazz字段代替

    public String getClazz() {
        return clazz;
    }

    public String getGrade() {
        return grade;
    }

    public String getContact() {
        return contact;
    }

    // 注意：department字段已被移除

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Boolean getIsGraduated() {
        return isGraduated;
    }

    public Integer getStudyYears() {
        return studyYears;
    }

    public User getUser() {
        return user;
    }

    public String getIdCard() {
        return idCard;
    }

    public StudentStatus getStatus() {
        return status;
    }

    // 手动添加setter方法
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    // 注意：className字段已被移除，使用clazz字段代替

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // 注意：department字段已被移除

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setIsGraduated(Boolean isGraduated) {
        this.isGraduated = isGraduated;
    }

    public void setStudyYears(Integer studyYears) {
        this.studyYears = studyYears;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }
    
    public List<AcademicRecord> getAcademicRecords() {
        return academicRecords;
    }
    
    public void setAcademicRecords(List<AcademicRecord> academicRecords) {
        this.academicRecords = academicRecords;
    }
}