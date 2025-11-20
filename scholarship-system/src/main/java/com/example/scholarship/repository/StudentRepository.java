package com.example.scholarship.repository;

import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
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
     * 根据学院查找学生
     */
    List<Student> findByCollege(String college);

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
     * 根据毕业状态查找学生
     */
    List<Student> findByIsGraduated(Boolean isGraduated);

    /**
     * 分页查找学生
     */
    Page<Student> findAll(Pageable pageable);

    /**
     * 查找指定学院的所有学生
     */
    @Query("SELECT s FROM Student s WHERE s.college = :college AND s.isGraduated = :isGraduated")
    List<Student> findByCollegeAndIsGraduated(@Param("college") String college, 
                                           @Param("isGraduated") Boolean isGraduated);

    /**
     * 查找指定年级和专业的学生
     */
    @Query("SELECT s FROM Student s WHERE s.grade = :grade AND s.major = :major AND s.isGraduated = :isGraduated")
    List<Student> findByGradeAndMajorAndIsGraduated(@Param("grade") String grade, 
                                                 @Param("major") String major, 
                                                 @Param("isGraduated") Boolean isGraduated);

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
     * 统计各学院学生数量
     */
    @Query("SELECT s.college, COUNT(s) FROM Student s WHERE s.isGraduated = :isGraduated GROUP BY s.college")
    List<Object[]> countStudentsByCollege(@Param("isGraduated") Boolean isGraduated);

    /**
     * 统计各年级学生数量
     */
    @Query("SELECT s.grade, COUNT(s) FROM Student s WHERE s.isGraduated = :isGraduated GROUP BY s.grade")
    List<Object[]> countStudentsByGrade(@Param("isGraduated") Boolean isGraduated);

    /**
     * 查找有用户关联的学生
     */
    @Query("SELECT s FROM Student s WHERE s.user IS NOT NULL")
    List<Student> findStudentsWithUser();

    /**
     * 检查学号是否存在
     */
    boolean existsByStudentNo(String studentNo);

    /**
     * 根据用户查找学生
     */
    Optional<Student> findByUser(User user);

    /**
     * 根据用户名查找学生
     */
    @Query("SELECT s FROM Student s WHERE s.user.username = :username")
    Optional<Student> findByUserUsername(@Param("username") String username);

    /**
     * 查找已毕业的学生
     */
    @Query("SELECT s FROM Student s WHERE s.isGraduated = true")
    List<Student> findGraduatedStudents();

    /**
     * 查找未毕业的学生
     */
    @Query("SELECT s FROM Student s WHERE s.isGraduated = false")
    List<Student> findActiveStudents();
}