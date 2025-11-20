package com.example.scholarship.repository;

import com.example.scholarship.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学业记录数据访问层接口
 * 
 * @author System
 * @version 1.0.0
 */
@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {

    /**
     * 根据学生ID查找所有学业记录
     */
    List<AcademicRecord> findByStudentId(Long studentId);

    /**
     * 查找指定学期的学业记录
     */
    List<AcademicRecord> findByStudentIdAndSemester(Long studentId, String semester);

    /**
     * 计算学生的平均GPA
     */
    @Query("SELECT AVG(ar.gpa) FROM AcademicRecord ar WHERE ar.student.id = :studentId")
    Double calculateAverageGpa(@Param("studentId") Long studentId);

    /**
     * 计算指定学期的平均GPA
     */
    @Query("SELECT AVG(ar.gpa) FROM AcademicRecord ar WHERE ar.student.id = :studentId AND ar.semester = :semester")
    Double calculateAverageGpaBySemester(@Param("studentId") Long studentId, @Param("semester") String semester);

    /**
     * 查找GPA大于等于指定值的学生记录
     */
    @Query("SELECT ar FROM AcademicRecord ar WHERE ar.student.id = :studentId AND ar.gpa >= :minGpa")
    List<AcademicRecord> findByGpaGreaterThanEqual(@Param("studentId") Long studentId, @Param("minGpa") Double minGpa);

    /**
     * 统计学生的总学分
     */
    @Query("SELECT SUM(ar.credit) FROM AcademicRecord ar WHERE ar.student.id = :studentId")
    Double calculateTotalCredits(@Param("studentId") Long studentId);

    /**
     * 查找优秀课程（GPA >= 3.5）
     */
    @Query("SELECT ar FROM AcademicRecord ar WHERE ar.student.id = :studentId AND ar.gpa >= 3.5 ORDER BY ar.gpa DESC")
    List<AcademicRecord> findExcellentCourses(@Param("studentId") Long studentId);
}