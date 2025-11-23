package com.example.scholarship.service.impl;

import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.repository.ScholarshipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 奖学金类型业务服务类
 * 
 * @author System
 * @version 1.0.0
 */
@Service("scholarshipTypeService")
public class ScholarshipTypeService {

    @Autowired
    private ScholarshipTypeRepository scholarshipTypeRepository;
    
    public List<ScholarshipType> findAll() {
        return scholarshipTypeRepository.findAll();
    }
    
    public Optional<ScholarshipType> findById(Long id) {
        return scholarshipTypeRepository.findById(id);
    }
    
    public List<ScholarshipType> getAvailableScholarships() {
        // 简单实现，返回所有奖学金类型
        return scholarshipTypeRepository.findAll();
    }
    
    public ScholarshipType save(ScholarshipType scholarshipType) {
        return scholarshipTypeRepository.save(scholarshipType);
    }
}
