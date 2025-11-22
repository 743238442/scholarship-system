package com.example.scholarship.service.impl;

import com.example.scholarship.entity.Student;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 学生服务实现类
 * 
 * @author System
 * @version 1.0.0
 */
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByUserId(Long userId) {
        // 通过自定义查询方法根据userId查找学生
        return studentRepository.findByUserId(userId);
    }
}
