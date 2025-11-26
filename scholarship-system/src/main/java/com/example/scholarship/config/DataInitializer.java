package com.example.scholarship.config;

import com.example.scholarship.entity.User;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.repository.UserRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.ScholarshipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 数据初始化配置
 * 应用启动时创建测试用户
 * 
 * @author System
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ScholarshipTypeRepository scholarshipTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("开始初始化数据...");
            
            // 短暂延迟以确保数据库初始化完成
            Thread.sleep(2000);
            
            // 创建管理员用户
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRealName("管理员");
                admin.setEmail("admin@example.com");
                admin.setUserType(User.UserType.ADMIN);
                admin.setStatus(User.UserStatus.ACTIVE);
                userRepository.save(admin);
                System.out.println("创建管理员用户: admin");
            } else {
                System.out.println("管理员用户已存在，跳过创建");
            }

            // 初始化学生用户1
            initializeStudent("lixm", "123456", "李小明", "lixm@example.com", "20210001", 
                Student.Gender.MALE, "计算机科学与技术学院", "计算机科学与技术", "计科2021-1", 
                "2021", "13800000001", LocalDate.parse("2021-09-01"), "110101200101010001");
            
            // 初始化学生用户2
            initializeStudent("zhangxm", "123456", "张小明", "zhangxm@example.com", "20210002", 
                Student.Gender.MALE, "计算机科学与技术学院", "软件工程", "软工2021-1", 
                "2021", "13800000002", LocalDate.parse("2021-09-01"), "110101200101010002");
            
            // 初始化奖学金类型数据
            initScholarshipTypes();
            
            // 验证创建结果
            System.out.println("数据初始化完成，当前用户总数: " + userRepository.count());
            System.out.println("数据初始化完成！");
            
        } catch (Exception e) {
            System.err.println("数据初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化奖学金类型数据
     */
    private void initScholarshipTypes() {
        System.out.println("开始初始化奖学金类型数据...");
        
        // 创建奖学金类型数据
        createScholarshipType("国家奖学金", new BigDecimal("8000.00"), "奖励特别优秀学生", "GPA≥3.8，综合排名前5%");
        createScholarshipType("国家励志奖学金", new BigDecimal("5000.00"), "奖励品学兼优的家庭经济困难学生", "GPA≥3.5，家庭经济困难，综合排名前10%");
        createScholarshipType("校级一等奖学金", new BigDecimal("3000.00"), "奖励学习成绩优异的学生", "GPA≥3.7，综合排名前15%");
        createScholarshipType("校级二等奖学金", new BigDecimal("2000.00"), "奖励学习成绩良好的学生", "GPA≥3.5，综合排名前25%");
        createScholarshipType("校级三等奖学金", new BigDecimal("1000.00"), "奖励学习成绩合格的学生", "GPA≥3.2，综合排名前40%");
        
        System.out.println("奖学金类型数据初始化完成");
    }
    
    private void createScholarshipType(String name, BigDecimal amount, String description, String eligibilityCriteria) {
        if (!scholarshipTypeRepository.existsByName(name)) {
            ScholarshipType type = new ScholarshipType();
            type.setName(name);
            type.setAmount(amount);
            type.setDescription(description);
            type.setEligibilityCriteria(eligibilityCriteria);
            scholarshipTypeRepository.save(type);
            System.out.println("已创建奖学金类型: " + name);
        } else {
            System.out.println("奖学金类型已存在: " + name);
        }
    }
    
    /**
     * 初始化学生用户及其对应的学生记录
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param email 邮箱
     * @param studentNo 学号
     * @param gender 性别
     * @param college 学院
     * @param major 专业
     * @param clazz 班级
     * @param grade 年级
     * @param contact 联系方式
     * @param enrollmentDate 入学日期
     * @param idCard 身份证号
     */
    private void initializeStudent(String username, String password, String realName, String email, String studentNo,
                                  Student.Gender gender, String college, String major, String clazz,
                                  String grade, String contact, LocalDate enrollmentDate, String idCard) {
        try {
            // 首先检查邮箱是否已存在（防止email唯一约束冲突）
            if (userRepository.existsByEmail(email)) {
                System.out.println("邮箱已存在: " + email + "，跳过创建用户");
                return;
            }
            
            // 检查用户是否已存在
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                // 创建新用户
                user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                user.setRealName(realName);
                user.setEmail(email);
                user.setUserType(User.UserType.STUDENT);
                user.setStatus(User.UserStatus.ACTIVE);
                try {
                    user = userRepository.save(user);
                    System.out.println("创建学生用户: " + username + ", 邮箱: " + email);
                } catch (Exception e) {
                    System.err.println("保存用户时出错: " + e.getMessage());
                    if (e.getMessage().contains("constraint")) {
                        System.err.println("可能是邮箱重复导致的约束冲突");
                    }
                    return; // 如果用户创建失败，不再继续
                }
            } else {
                System.out.println("学生用户已存在: " + username);
                // 确保邮箱一致性
                if (!email.equals(user.getEmail())) {
                    System.out.println("警告: 用户邮箱不一致，当前邮箱: " + user.getEmail() + ", 预期邮箱: " + email);
                }
            }
            
            // 检查学生记录是否已存在
            Optional<Student> existingStudent = studentRepository.findByUser(user);
            if (existingStudent.isPresent()) {
                System.out.println("学生记录已存在: " + user.getUsername() + " - " + user.getRealName());
            } else if (studentRepository.existsByStudentNo(studentNo)) {
                System.out.println("学号已存在: " + studentNo + "，跳过创建学生记录");
            } else {
                try {
                    // 创建学生记录
                    Student student = new Student();
                    student.setStudentNo(studentNo);
                    student.setName(realName);
                    student.setGender(gender);
                    student.setCollege(college);
                    student.setMajor(major);
                    student.setClazz(clazz);
                    student.setGrade(grade);
                    student.setContact(contact);
                    student.setEnrollmentDate(enrollmentDate);
                    student.setIsGraduated(false);
                    student.setIdCard(idCard);
                    student.setStatus(Student.StudentStatus.ACTIVE);
                    student.setStudyYears(4);
                    student.setUser(user);
                    studentRepository.save(student);
                    System.out.println("创建学生记录: " + studentNo + " - " + realName);
                } catch (Exception e) {
                    System.err.println("创建学生记录时出错: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("初始化学生信息失败: " + username);
            e.printStackTrace();
        }
    }
}