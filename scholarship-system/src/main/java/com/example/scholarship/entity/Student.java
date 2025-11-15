package com.example.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class Student extends BaseEntity {

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
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotBlank(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "身份证号必须是18位")
    @Pattern(regexp = "^[0-9]{17}[0-9X]$", message = "身份证号格式不正确")
    @Column(name = "id_card", unique = true, nullable = false, length = 18)
    private String idCard;

    @NotBlank(message = "院系不能为空")
    @Size(max = 100, message = "院系长度不能超过100个字符")
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @NotBlank(message = "专业不能为空")
    @Size(max = 100, message = "专业长度不能超过100个字符")
    @Column(name = "major", nullable = false, length = 100)
    private String major;

    @NotBlank(message = "班级不能为空")
    @Size(max = 50, message = "班级长度不能超过50个字符")
    @Column(name = "class_name", nullable = false, length = 50)
    private String className;

    @NotBlank(message = "年级不能为空")
    @Pattern(regexp = "^20\\d{2}$", message = "年级格式不正确，必须是20XX年格式")
    @Column(name = "grade", nullable = false, length = 4)
    private String grade;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Size(max = 20, message = "民族长度不能超过20个字符")
    @Column(name = "ethnicity", length = 20)
    private String ethnicity;

    @Size(max = 50, message = "政治面貌长度不能超过50个字符")
    @Column(name = "political_status", length = 50)
    private String politicalStatus;

    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    @Column(name = "phone", length = 20)
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 200, message = "家庭地址长度不能超过200个字符")
    @Column(name = "address", length = 200)
    private String address;

    @NotNull(message = "入学时间不能为空")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @NotNull(message = "学制不能为空")
    @Column(name = "study_years", nullable = false)
    private Integer studyYears;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentStatus status = StudentStatus.ACTIVE;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "emergency_contact", length = 50)
    private String emergencyContact;

    @Column(name = "emergency_phone", length = 20)
    private String emergencyPhone;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    @Column(name = "remark", length = 1000)
    private String remark;

    // 关联用户
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    /**
     * 性别枚举
     */
    public enum Gender {
        MALE("男"),
        FEMALE("女"),
        UNKNOWN("未知");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 学生状态枚举
     */
    public enum StudentStatus {
        ACTIVE("在读"),
        GRADUATED("毕业"),
        SUSPENDED("休学"),
        DROPPED("退学"),
        TRANSFERRED("转学");

        private final String description;

        StudentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 计算学生当前年级（如大一、大二等）
     */
    public String getCurrentGradeLevel() {
        if (enrollmentDate == null) {
            return "未知";
        }
        
        int currentYear = LocalDate.now().getYear();
        int enrollmentYear = enrollmentDate.getYear();
        int gradeLevel = currentYear - enrollmentYear + 1;
        
        if (gradeLevel <= 0) {
            return "未知";
        }
        
        String[] gradeNames = {"大一", "大二", "大三", "大四", "大五"};
        if (gradeLevel > gradeNames.length) {
            return "研究生";
        }
        
        return gradeNames[gradeLevel - 1];
    }
}