package com.example.scholarship.repository;

import com.example.scholarship.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * 根据学号查找学生
     */
    Optional<Student> findByStudentNo(String studentNo);

    /**
     * 根据姓名查找学生
     */
    List<Student> findByNameContaining(String name);

    /**
     * 根据院系查找学生
     */
    List<Student> findByDepartment(String department);

    /**
     * 根据专业查找学生
     */
    List<Student> findByMajor(String major);

    /**
     * 根据班级查找学生
     */
    List<Student> findByClassName(String className);

    /**
     * 根据年级查找学生
     */
    List<Student> findByGrade(String grade);

    /**
     * 根据状态查找学生
     */
    List<Student> findByStatus(Student.StudentStatus status);

    /**
     * 分页查找学生
     */
    Page<Student> findAll(Pageable pageable);

    /**
     * 查找指定院系的所有学生
     */
    @Query("SELECT s FROM Student s WHERE s.department = :department AND s.status = :status")
    List<Student> findByDepartmentAndStatus(@Param("department") String department, 
                                          @Param("status") Student.StudentStatus status);

    /**
     * 查找指定年级和专业的学生
     */
    @Query("SELECT s FROM Student s WHERE s.grade = :grade AND s.major = :major AND s.status = :status")
    List<Student> findByGradeAndMajorAndStatus(@Param("grade") String grade, 
                                             @Param("major") String major, 
                                             @Param("status") Student.StudentStatus status);

    /**
     * 根据姓名和学号模糊查找
     */
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:keyword% OR s.studentNo LIKE %:keyword%")
    List<Student> findByKeyword(@Param("keyword") String keyword);

    /**
     * 查找指定入学年份的学生
     */
    @Query("SELECT s FROM Student s WHERE s.enrollmentDate >= :startDate AND s.enrollmentDate <= :endDate")
    List<Student> findByEnrollmentYear(@Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);

    /**
     * 统计各院系学生数量
     */
    @Query("SELECT s.department, COUNT(s) FROM Student s WHERE s.status = :status GROUP BY s.department")
    List<Object[]> countStudentsByDepartment(@Param("status") Student.StudentStatus status);

    /**
     * 统计各年级学生数量
     */
    @Query("SELECT s.grade, COUNT(s) FROM Student s WHERE s.status = :status GROUP BY s.grade")
    List<Object[]> countStudentsByGrade(@Param("status") Student.StudentStatus status);

    /**
     * 查找有用户关联的学生
     */
    @Query("SELECT s FROM Student s WHERE s.user.id IS NOT NULL")
    List<Student> findStudentsWithUser();

    /**
     * 检查学号是否存在
     */
    boolean existsByStudentNo(String studentNo);

    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard);

    /**
     * 查找将要毕业的学生（距离毕业日期不足6个月）
     */
    @Query("SELECT s FROM Student s WHERE s.graduationDate <= :cutoffDate AND s.status = :activeStatus")
    List<Student> findStudentsNearGraduation(@Param("cutoffDate") LocalDate cutoffDate, 
                                           @Param("activeStatus") Student.StudentStatus activeStatus);
}